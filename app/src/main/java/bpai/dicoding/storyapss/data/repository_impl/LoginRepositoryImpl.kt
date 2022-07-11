package bpai.dicoding.storyapss.data.repository_impl

import bpai.dicoding.storyapss.data.remote.network.login.ILoginApi
import bpai.dicoding.storyapss.domain.repository.ILoginRepository
import bpai.dicoding.storyapss.model.Sessions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi:ILoginApi
):ILoginRepository {
    override fun login(login: bpai.dicoding.storyapss.model.LoginPayload): Flow<Result<Sessions>> = flow {
        try {
            val request = loginApi.login(login.toDto())
            emit(Result.success(request.loginResult.toDataSession()))
        }catch(e:HttpException){
            val body = JSONObject(e.response()?.errorBody()?.charStream()?.readText() as String)
            emit(Result.failure(RuntimeException(body.getString("message"))))
        }catch (e:Exception){
            emit(Result.failure(RuntimeException("exception test ${e.localizedMessage}")))
        }
    }
}