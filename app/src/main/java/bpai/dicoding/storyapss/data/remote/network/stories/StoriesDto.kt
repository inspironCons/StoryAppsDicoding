package bpai.dicoding.storyapss.data.remote.network.stories

import bpai.dicoding.storyapss.data.remote.local.stories.StoriesEntity
import bpai.dicoding.storyapss.model.Stories
import com.google.gson.annotations.SerializedName

object StoriesDto {
    data class StoriesResponse(
        @SerializedName("error") var error: Boolean,
        @SerializedName("message") var message: String,
        @SerializedName("listStory") var listStory: ArrayList<ListStory> = arrayListOf()
    )

    data class ListStory(
        @SerializedName("id") var id: String,
        @SerializedName("name") var name: String,
        @SerializedName("description") var description: String,
        @SerializedName("photoUrl") var photoUrl: String,
        @SerializedName("createdAt") var createdAt: String,
        @SerializedName("lat") var lat: Double,
        @SerializedName("lon") var lon: Double
    ){
        fun toStories(): Stories = Stories(id,name,photoUrl,description,lat,lon)

        fun toStoriesEntity():StoriesEntity = StoriesEntity(
            id = id,
            username = name,
            photoUrl = photoUrl,
            description = description,
            latitude = lat,
            longitude = lon
        )
    }

    data class ResponseCreate(
        @SerializedName("error") val error: Boolean,
        @SerializedName("message") val message: String,
    )
}