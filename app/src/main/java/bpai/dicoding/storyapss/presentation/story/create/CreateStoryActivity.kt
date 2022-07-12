package bpai.dicoding.storyapss.presentation.story.create

import android.animation.ValueAnimator
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import bpai.dicoding.storyapss.R
import bpai.dicoding.storyapss.databinding.ActivityCreateStoryBinding
import bpai.dicoding.storyapss.presentation.BaseActivity
import bpai.dicoding.storyapss.presentation.camera.CameraStoryActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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

    
    private val viewModel: CreateStoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getStateUpload()
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

        binding.btnUpload.setOnClickListener {
            uploadStory()
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

}