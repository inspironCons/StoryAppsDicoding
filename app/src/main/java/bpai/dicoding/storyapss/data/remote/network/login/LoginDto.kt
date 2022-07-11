package bpai.dicoding.storyapss.data.remote.network.login

import bpai.dicoding.storyapss.model.Sessions
import com.google.gson.annotations.SerializedName

object LoginDto {
    data class BodyRequest(
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String
    )

    data class ResponseLoginApi(
        @SerializedName("error") var error: Boolean,
        @SerializedName("message") var message: String,
        @SerializedName("loginResult") var loginResult: DataLogin
    )

    data class DataLogin(
        @SerializedName("userId") var userId: String,
        @SerializedName("name") var name: String,
        @SerializedName("token") var token: String
    ){
        fun toDataSession(): Sessions = Sessions(name,token)
    }
}