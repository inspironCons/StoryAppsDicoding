package bpai.dicoding.storyapss.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Stories(
    val id:String,
    val name: String,
    val image: String,
    val description:String
):Parcelable

data class CreateStory(
    val image: File,
    val description:String,
    val lat:Float?=null,
    val lon:Float?=null
)