package bpai.dicoding.storyapss.data.remote.network.register

import retrofit2.http.Body
import retrofit2.http.POST

interface IRegisterApi {
    @POST("register")
    suspend fun register(@Body request: RegisterDto.PayloadRegister): RegisterDto.ResponseRegister
}