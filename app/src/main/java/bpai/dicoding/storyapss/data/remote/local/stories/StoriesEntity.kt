package bpai.dicoding.storyapss.data.remote.local.stories

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stories")
data class StoriesEntity(
    @PrimaryKey
    @field:SerializedName("id")
    val id:String,

    @field:SerializedName("username")
    val username:String,

    @field:SerializedName("photoUrl")
    val photoUrl:String,

    @field:SerializedName("latitude")
    val latitude:Float,

    @field:SerializedName("longitude")
    val longitude:Float,
)