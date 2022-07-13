package bpai.dicoding.storyapss.presentation.story.base_on_maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bpai.dicoding.storyapss.domain.use_case.map.IStoryOnMapUseCase
import bpai.dicoding.storyapss.model.Stories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryBasedOnMapViewModel @Inject constructor(
    private val useCase: IStoryOnMapUseCase
):ViewModel() {
    private val _mapState = MutableStateFlow<MarkState>(MarkState.Empty)
    val mapState:StateFlow<MarkState> = _mapState

    sealed class MarkState{
        data class Success(val stories:List<Stories>):MarkState()
        data class Error(val message:String?):MarkState()
        object Loading:MarkState()
        object Empty:MarkState()
    }

    fun getStoriesByLocation() = viewModelScope.launch{
        _mapState.value = MarkState.Loading
        useCase.getListStoryByLocation().collect{ result->
            if(result.isSuccess){
                val list = result.getOrNull()
                if(list != null){
                    _mapState.value = MarkState.Success(list)
                }else{
                    _mapState.value = MarkState.Empty
                }
            }else{
                val msg = result.exceptionOrNull()
                _mapState.value = MarkState.Error(msg?.message)
            }
        }
    }
}