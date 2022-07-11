package bpai.dicoding.storyapss.presentation.story.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import bpai.dicoding.storyapss.databinding.ActivityStoryBinding
import bpai.dicoding.storyapss.model.Stories
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryActivity:AppCompatActivity() {
    private lateinit var binding:ActivityStoryBinding
    private val viewModel: StoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContent()
        getContent()
    }

    private fun setContent() {
        val extra = intent.extras
        if(extra != null){
            val bundle = try {
                extra.getParcelable<Stories>(DATA_BUNDLE)
            }catch (e:Exception){
                null
            }
            viewModel.setContent(bundle)
        }
    }

    private fun getContent(){
        lifecycleScope.launchWhenCreated {
            viewModel.contentUiState.collect{ state->
                when(state){
                    is StoryViewModel.ContentUiState.Loading ->{

                    }
                    is StoryViewModel.ContentUiState.Error ->{

                    }
                    is StoryViewModel.ContentUiState.Success ->{
                        bindingView(state.content)
                    }
                    else->Unit
                }
            }
        }
    }

    private fun bindingView(data: Stories){
        with(binding){
            Glide.with(this@StoryActivity)
                .load(data.image)
                .into(ivImage)

            tvUsername.text = data.name
            tvCaption.text = data.description
        }
    }

    companion object{
        const val DATA_BUNDLE = "BUNDLE_STORY"
    }
}