package bpai.dicoding.storyapss.domain.use_case.register

import bpai.dicoding.storyapss.domain.repository.IRegisterRepository
import bpai.dicoding.storyapss.model.RegistrationPayload
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val repository:IRegisterRepository
):IRegisterUseCase {
    override fun signUp(payload: RegistrationPayload): Flow<Result<String>> = repository
        .createNewAccount(payload)
}