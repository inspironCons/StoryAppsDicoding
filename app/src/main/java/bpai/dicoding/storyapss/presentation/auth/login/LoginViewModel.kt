package bpai.dicoding.storyapss.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bpai.dicoding.storyapss.domain.use_case.login.ILoginUseCase
import bpai.dicoding.storyapss.model.LoginPayload
import bpai.dicoding.storyapss.model.Sessions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase:ILoginUseCase
):ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Empty)
    val loginState:StateFlow<LoginState> = _loginState
    private val data = LoginPayload()

    sealed class LoginState{
        data class Success(val sessions: Sessions?): LoginState()
        data class Error(val message:String?): LoginState()
        object Loading: LoginState()
        object Empty: LoginState()
    }

    fun setEmail(email:String){
        data.email = email
    }

    fun setPassword(pass:String){
        data.password = pass
    }

    fun login() = viewModelScope.launch {
        _loginState.value = LoginState.Loading
        if(data.email.isEmpty() && data.password.isEmpty()){
            _loginState.value = LoginState.Empty
            return@launch
        }
        loginUseCase.login(data).collectLatest { result->
            if(result.isSuccess){
                val data = result.getOrNull()
                _loginState.value = LoginState.Success(data)
            }else{
                val msg = result.exceptionOrNull()
                _loginState.value = LoginState.Error(msg?.message)
            }
        }
    }
}