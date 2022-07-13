package bpai.dicoding.storyapss.presentation.story.create

import android.Manifest
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import bpai.dicoding.storyapss.R
import bpai.dicoding.storyapss.databinding.ActivityCreateStoryBinding
import bpai.dicoding.storyapss.presentation.BaseActivity
import bpai.dicoding.storyapss.presentation.camera.CameraStoryActivity
import bpai.dicoding.storyapss.presentation.utils.DialogsUtils
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class CreateStoryActivity : BaseActivity() {
    private lateinit var binding: ActivityCreateStoryBinding
    
    //image
    private lateinit var image:File
    
    //dialog
    private lateinit var dialog:Dialog
    private lateinit var loaderState: LottieAnimationView
    private lateinit var descriptionDialog:TextView
    private lateinit var btnLoader:Button

    //fused location
    private lateinit var fusedLocation: FusedLocationProviderClient

    private val viewModel: CreateStoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getStateUpload()
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        //create dialog
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val extra = intent.extras
        if(extra != null){
            image = extra.getSerializable(CameraStoryActivity.EXTRA_IMAGE) as File
            Glide.with(this)
                .load(image)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(binding.previewImage)
        }

        binding.geoLocCheck.setOnCheckedChangeListener { _, checked->
            if(checked){
                setMyLocation()
            }else{
                viewModel.setLocation(null)
            }
        }

        binding.btnUpload.setOnClickListener {
            uploadStory()
        }
    }

    /**
     * get my location precise or approx
     * before that check permission
     */
    private fun setMyLocation() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if(p0?.isAnyPermissionPermanentlyDenied == true){
                        goesToSetting(p0.deniedPermissionResponses.joinToString(","))
                        binding.geoLocCheck.isChecked = false
                    }else if(p0?.areAllPermissionsGranted() == true){
                        getFusedLocation()
                    }
                }

            }).check()
    }

    /**
     * get know location automation
     */
    private fun getFusedLocation() {
        fusedLocation.lastLocation.addOnSuccessListener { _loc->
            if(_loc != null){
                viewModel.setLocation(_loc)
            }else{
                viewModel.setLocation(null)
                binding.geoLocCheck.isChecked = false
                Toast.makeText(this@CreateStoryActivity,getString(R.string.location_not_found),
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * upload story to server
     */
    private fun uploadStory() {
        showDialog()
        setLottie(R.raw.loading,true)
        val description = binding.etDescription.text.toString()
        viewModel.upload(image,description)
    }

    private fun getStateUpload(){
        lifecycleScope.launchWhenCreated {
            viewModel.uploadState.collect{ state->
                when(state){
                    is CreateStoryViewModel.UploadState.Success ->{
                        buttonDialog(true)
                        setLottie(R.raw.success,false)
                        descriptionDialog.text = state.msg

                    }
                    is CreateStoryViewModel.UploadState.Error ->{
                        buttonDialog(false)
                        setLottie(R.raw.failure,false)
                        descriptionDialog.text = state.msg
                    }
                    is CreateStoryViewModel.UploadState.Loading ->{
                        descriptionDialog.text = state.msg
                    }
                    else -> Unit
                }
            }
        }
    }

    /**
     * function for close activity module camera
     */
    private fun closeCreateActivity(){
        setResult(RESULT_OK)
        finish()
    }

    /**
     * show dialog loader
     */
    private fun showDialog(){
        dialog.setContentView(R.layout.dialog_view_loader)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(false)
        descriptionDialog = dialog.findViewById(R.id.tv_deskripsi)
        loaderState = dialog.findViewById(R.id.loader_state)
        btnLoader = dialog.findViewById(R.id.btn_back)

        dialog.show()
    }

    private fun buttonDialog(isAllowBack:Boolean){
        btnLoader.setOnClickListener {
            dialog.dismiss()
            if(isAllowBack)closeCreateActivity()
        }
    }

    private fun setLottie(raw:Int,loop:Boolean){
        loaderState.setAnimation(raw)
        loaderState.playAnimation()
        loaderState.repeatCount = if(loop) ValueAnimator.INFINITE else 0
        btnLoader.isEnabled = !loop
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
}