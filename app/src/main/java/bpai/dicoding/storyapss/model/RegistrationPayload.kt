package bpai.dicoding.storyapss.model

import bpai.dicoding.storyapss.data.remote.network.register.RegisterDto

data class RegistrationPayload(
    var name:String = "",
    var email:String = "",
    var password:String = ""
){
    fun toRegisterDto(): RegisterDto.PayloadRegister =
        RegisterDto.PayloadRegister(name, email, password)
}