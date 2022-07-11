package bpai.dicoding.storyapss.domain.use_case.login

import bpai.dicoding.storyapss.domain.repository.ILoginRepository
import bpai.dicoding.storyapss.model.Sessions
import bpai.dicoding.storyapss.model.LoginPayload
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val loginRepo:ILoginRepository
):ILoginUseCase {
    override fun login(payload: LoginPayload): Flow<Result<Sessions>> = loginRepo
        .login(payload)
}