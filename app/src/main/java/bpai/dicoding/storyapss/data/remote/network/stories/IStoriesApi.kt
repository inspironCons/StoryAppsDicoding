package bpai.dicoding.storyapss.data.remote.network.stories

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface IStoriesApi {
    @GET("stories")
    suspend fun stories(
        @Query("page") page:Int = 1,
        @Query("size") size:Int = 10,
        @Query("location") location:Byte = 0
    ): StoriesDto.StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun createdStories(
        @Part("description") description: RequestBody,
        @Part photo:MultipartBody.Part,
        @Part("lat") lat:RequestBody? = null,
        @Part("lon") lon:RequestBody? = null
    ):StoriesDto.ResponseCreate
}