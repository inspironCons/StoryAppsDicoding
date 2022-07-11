package bpai.dicoding.storyapss.presentation.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import bpai.dicoding.storyapss.R
import bpai.dicoding.storyapss.databinding.ActivityCameraStoryBinding
import bpai.dicoding.storyapss.presentation.BaseActivity
import bpai.dicoding.storyapss.presentation.story.create.CreateStoryActivity
import bpai.dicoding.storyapss.presentation.utils.DialogsUtils
import bpai.dicoding.storyapss.presentation.utils.ImageUtils
import bpai.dicoding.storyapss.presentation.utils.ImageUtils.toFile
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CameraStoryActivity: BaseActivity() {
    private lateinit var binding: ActivityCameraStoryBinding

    private val viewModel: CameraStoryViewModel by viewModels()
    /**
     * variable for save value image
     */
    private var imageCapture: ImageCapture? = null
    /**
     * variable for select position camera
     * @sample CameraSelector.DEFAULT_BACK_CAMERA
     */
    private var cameraSelector:CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    /**
     * variable for launch gallery intent
     * @return Image
     */
    private val galleryLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode == RESULT_OK){
            val image = result.data?.data as Uri
            val selectedImage = image.toFile(application)
            goesToPreview(selectedImage)
        }
    }

    private val resultCreate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode == RESULT_OK){
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permissionCamera()
        getLastImage()
        binding.flipCamera.setOnClickListener {
            flipCamera()
        }

        binding.takePicture.setOnClickListener {
            takePicture()
        }

        binding.gallery.setOnClickListener {
            goesToGallery()
        }

    }

    private fun getLastImage() {
        viewModel.getLastImage()
        lifecycleScope.launchWhenCreated {
            viewModel.galleryState.collect{ state->
                when(state){
                    is CameraStoryViewModel.GalleryState.Error ->{
                        Toast.makeText(this@CameraStoryActivity,state.message,Toast.LENGTH_LONG).show()
                    }
                    is CameraStoryViewModel.GalleryState.Success ->{
                        Glide.with(this@CameraStoryActivity)
                            .load(state.photo)
                            .apply(RequestOptions.skipMemoryCacheOf(true))
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(binding.gallery)
                    }
                    else -> Unit
                }
            }
        }
    }

    /**
     * check permission for the camera then init camera
     * if granted then start function camera ,
     * if denied then go to info apps to allow manual permission camera ,
     * if allow only once then aks permission again
     */
    private fun permissionCamera() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    startCamera()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    goesToSetting(p0?.permissionName)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
    }

    /**
     * open dialog to navigate user open setting apps
     */
    private fun goesToSetting(permissionName:String?) {
        DialogsUtils.simpleDialog(
            this,
            resources.getString(R.string.need_permission),
            getString(R.string.need_permission_desc,permissionName),
            resources.getString(R.string.go_to_setting)
        ){
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    /**
     * function for initiation camerax
     */
    private fun startCamera(){
        val camProviderInstance = ProcessCameraProvider.getInstance(this)

        camProviderInstance.addListener({
            val camProvider = camProviderInstance.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.surfaceCamera.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                camProvider.unbindAll()
                camProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture)
            }catch (e:Exception){
                Toast.makeText(
                    this,
                    getString(R.string.failed_launch_camera),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * function flip a camera
     */
    private fun flipCamera(){
        cameraSelector = if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
            CameraSelector.DEFAULT_FRONT_CAMERA
        }else{
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    /**
     * take a picture from button
     * @return Unit
     */
    private fun takePicture(){
        val image = imageCapture?: return
        val photoFile = ImageUtils.createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        image.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    goesToPreview(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraStoryActivity,
                        exception.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        )
    }

    /**
     * action for get image from gallery
     * @return Unit
     */
    private fun goesToGallery(){
        val mIntent = Intent(Intent.ACTION_GET_CONTENT)
        mIntent.type = "image/*"
        val chooser = Intent.createChooser(mIntent,getString(R.string.chooser_image))
        galleryLaunch.launch(chooser)
    }

    private fun goesToPreview(file:File){
        val mIntent = Intent(this@CameraStoryActivity, CreateStoryActivity::class.java)
        mIntent.putExtra(EXTRA_IMAGE,file)
        resultCreate.launch(mIntent)
    }

    companion object{
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
    }
}