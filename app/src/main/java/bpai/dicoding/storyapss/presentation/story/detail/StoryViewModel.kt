package bpai.dicoding.storyapss.presentation.story.detail

import androidx.lifecycle.ViewModel
import bpai.dicoding.storyapss.model.Stories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor():ViewModel() {
    private val _contentUiState = MutableStateFlow<ContentUiState>(ContentUiState.Empty)
    val contentUiState:StateFlow<ContentUiState> = _contentUiState
    sealed class ContentUiState{
        data class Success(val content: Stories): ContentUiState()
        data class Error(val message:String): ContentUiState()
        object Empty: ContentUiState()
        object Loading: ContentUiState()
    }

    fun setContent(data: Stories?){
        _contentUiState.value = ContentUiState.Loading
        if(data == null){
            _contentUiState.value = ContentUiState.Error("opps terjadi kesalahan")
            return
        }
        _contentUiState.value = ContentUiState.Success(data)
    }
}