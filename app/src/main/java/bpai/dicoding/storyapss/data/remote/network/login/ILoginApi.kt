package bpai.dicoding.storyapss.data.remote.network.login

import retrofit2.http.Body
import retrofit2.http.POST


interface ILoginApi {
    @POST("login")
    suspend fun login(@Body request:LoginDto.BodyRequest):LoginDto.ResponseLoginApi
}