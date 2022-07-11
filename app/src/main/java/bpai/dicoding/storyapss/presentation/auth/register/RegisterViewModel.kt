package bpai.dicoding.storyapss.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bpai.dicoding.storyapss.domain.use_case.register.IRegisterUseCase
import bpai.dicoding.storyapss.model.RegistrationPayload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val useCase: IRegisterUseCase
):ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Empty)
    val registerState: StateFlow<RegisterState> = _registerState
    sealed class RegisterState{
        data class Success(val msg:String): RegisterState()
        data class Error(val msg:String?): RegisterState()
        object Empty: RegisterState()
        data class Loading(val msg:String): RegisterState()
    }
    private val payload = RegistrationPayload()

    fun setName(name:String){
        payload.name =name
    }

    fun setEmail(email:String){
        payload.email =email
    }

    fun setPassword(pass:String){
        payload.password =pass
    }

    fun singUpAccount() = viewModelScope.launch {
        _registerState.value = RegisterState.Loading("Loading...")
        val validate = validateForm()
        if(!validate.first){
            _registerState.value = RegisterState.Error(validate.second)
            return@launch
        }

        useCase.signUp(payload).collect{ result->
            if(result.isSuccess) {
                val data = result.getOrNull()
                if(data != null){
                    _registerState.value = RegisterState.Success(data)
                }else{
                    _registerState.value = RegisterState.Error("Something when wrong,please try again")
                }
            }else{
                val message = result.exceptionOrNull()
                _registerState.value = RegisterState.Error(message?.message)

            }
        }
    }

    private fun validateForm():Pair<Boolean,String>{
        if(payload.email.isEmpty() && payload.name.isEmpty() && payload.password.isEmpty()){
            return Pair(false,"Harap isi data terlebih dahulu")
        }

        if(payload.email.isEmpty()){
            return Pair(false,"Alamat Email masih kosong")
        }

        if(payload.password.isEmpty()){
            return Pair(false,"Password masih kosong")
        }
        return Pair(true,"")
    }

}