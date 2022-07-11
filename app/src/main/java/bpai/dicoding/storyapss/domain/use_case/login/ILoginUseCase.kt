package bpai.dicoding.storyapss.domain.use_case.login

import bpai.dicoding.storyapss.model.Sessions
import bpai.dicoding.storyapss.model.LoginPayload
import kotlinx.coroutines.flow.Flow


interface ILoginUseCase {
    fun login(payload: LoginPayload): Flow<Result<Sessions>>
}