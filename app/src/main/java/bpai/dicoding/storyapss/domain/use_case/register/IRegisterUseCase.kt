package bpai.dicoding.storyapss.domain.use_case.register

import bpai.dicoding.storyapss.model.RegistrationPayload
import kotlinx.coroutines.flow.Flow

interface IRegisterUseCase {
    fun signUp(payload: RegistrationPayload): Flow<Result<String>>
}