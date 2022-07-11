package bpai.dicoding.storyapss.presentation.story.create

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bpai.dicoding.storyapss.model.CreateStory
import bpai.dicoding.storyapss.domain.use_case.create_story.ICreateStoryUseCase
import bpai.dicoding.storyapss.presentation.utils.ImageUtils.reduceSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateStoryViewModel @Inject constructor(
    private val useCase: ICreateStoryUseCase
):ViewModel() {
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Empty)
    val uploadState:StateFlow<UploadState> = _uploadState
    sealed class UploadState{
        data class Success(val msg:String): UploadState()
        data class Error(val msg:String?): UploadState()
        object Empty: UploadState()
        data class Loading(val msg:String): UploadState()
    }

    private var location:Location?=null
    fun setLocation(location: Location?){
        this.location = location
    }

    fun upload(image: File,desc:String) = viewModelScope.launch{
        _uploadState.value = UploadState.Loading("Loading...")
        val photo = image.reduceSize()
        if(photo != null){
            val payload = CreateStory(
                photo,
                desc,
                location?.latitude?.toFloat(),
                location?.longitude?.toFloat()
            )

            useCase.create(payload).collect{ result->
                if(result.isSuccess){
                    val data = result.getOrNull()
                    if(data != null){
                        if(data.first){
                            _uploadState.value = UploadState.Success(data.second)
                        }else{
                            _uploadState.value = UploadState.Loading(data.second)
                        }
                    }else{
                        _uploadState.value =
                            UploadState.Error("Something when wrong,please try again")
                    }
                }else{
                    val msg = result.exceptionOrNull()
                    _uploadState.value = UploadState.Error(msg?.message)
                }
            }
        }else{
            _uploadState.value = UploadState.Error("Image crash please take picture again")
        }
    }
}