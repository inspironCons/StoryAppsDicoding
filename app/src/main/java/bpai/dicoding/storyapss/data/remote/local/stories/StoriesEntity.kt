package bpai.dicoding.storyapss.data.remote.local.stories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import bpai.dicoding.storyapss.model.Stories

@Entity(tableName = "stories")
data class StoriesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id:String,

    @ColumnInfo("username")
    val username:String,

    @ColumnInfo("photoUrl")
    val photoUrl:String,

    @ColumnInfo("description")
    val description:String,

    @ColumnInfo("latitude")
    val latitude:Double,

    @ColumnInfo("longitude")
    val longitude:Double,
){
    fun toStories() = Stories(id,username,photoUrl,description,latitude,longitude)
}