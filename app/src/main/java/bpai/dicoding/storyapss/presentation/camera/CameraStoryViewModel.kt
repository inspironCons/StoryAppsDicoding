package bpai.dicoding.storyapss.presentation.camera

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraStoryViewModel @Inject constructor(
    private val app:Application
):ViewModel() {
    private val _galleryState = MutableStateFlow<GalleryState>(GalleryState.Empty)
    val galleryState: StateFlow<GalleryState> = _galleryState
    sealed class GalleryState{
        data class Success(val photo:File): GalleryState()
        data class Error(val message:String?): GalleryState()
        object Loading: GalleryState()
        object Empty: GalleryState()
    }

    /**
     * Getting All Images Path
     * But its required read external storage permission
     * @Return ArrayList<String>
     */
    fun getLastImage(){
        _galleryState.value = GalleryState.Loading
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor?
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
        )
        cursor = app.contentResolver.query(
            uri,
            projection,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")
        try {
            while (cursor!!.moveToNext()){
                val lastImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                val file = File(lastImage)
                if(file.exists()){
                    _galleryState.value = GalleryState.Success(file)
                    break
                }
            }

        }catch (e:Exception){
            _galleryState.value = GalleryState.Error(e.localizedMessage)
        }
    }
}