package bpai.dicoding.storyapss.data.repository_impl

import bpai.dicoding.storyapss.data.remote.network.register.IRegisterApi
import bpai.dicoding.storyapss.domain.repository.IRegisterRepository
import bpai.dicoding.storyapss.model.RegistrationPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val registerApi: IRegisterApi
): IRegisterRepository {
    override fun createNewAccount(payload: RegistrationPayload): Flow<Result<String>> = flow {
        try {
            val create = registerApi.register(payload.toRegisterDto())
            emit(Result.success(create.message))
        }catch (e:HttpException){
            val body = JSONObject(e.response()?.errorBody()?.charStream()?.readText() as String)
            emit(Result.failure(RuntimeException(body.getString("message"))))
        }catch (e:Exception){
            emit(Result.failure(RuntimeException("exception test ${e.localizedMessage}")))
        }
    }
}