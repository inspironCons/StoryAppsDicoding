package bpai.dicoding.storyapss.model

import bpai.dicoding.storyapss.data.remote.network.login.LoginDto

data class LoginPayload(
    var email:String="",
    var password:String=""
){
    fun toDto(): LoginDto.BodyRequest{
        return LoginDto.BodyRequest(email,password)
    }

}
