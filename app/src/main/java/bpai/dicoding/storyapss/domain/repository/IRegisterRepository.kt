package bpai.dicoding.storyapss.domain.repository

import bpai.dicoding.storyapss.model.RegistrationPayload
import kotlinx.coroutines.flow.Flow

interface IRegisterRepository {
    fun createNewAccount(payload: RegistrationPayload): Flow<Result<String>>
}