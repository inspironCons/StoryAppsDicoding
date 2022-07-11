package bpai.dicoding.storyapss.domain.repository

import bpai.dicoding.storyapss.model.Sessions
import bpai.dicoding.storyapss.model.LoginPayload
import kotlinx.coroutines.flow.Flow

interface ILoginRepository {
    fun login(login: LoginPayload):Flow<Result<Sessions>>
}