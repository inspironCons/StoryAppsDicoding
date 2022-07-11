package bpai.dicoding.storyapss.data.remote.network.register

import com.google.gson.annotations.SerializedName

object RegisterDto {
    data class PayloadRegister(
        @SerializedName("name") val name: String,
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String
    )

    data class ResponseRegister(
        @SerializedName("error") val error: Boolean,
        @SerializedName("message") val message: String
    )
}