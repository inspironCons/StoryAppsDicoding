package bpai.dicoding.storyapss.data.remote.local.remote_keys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val id:String,
    val prevKey:Int?,
    val nextKey:Int?
)
