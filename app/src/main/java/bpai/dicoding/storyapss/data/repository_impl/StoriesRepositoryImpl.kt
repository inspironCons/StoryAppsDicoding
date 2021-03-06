package bpai.dicoding.storyapss.data.repository_impl

import androidx.paging.PagingSource
import bpai.dicoding.storyapss.data.remote.local.stories.StoriesDao
import bpai.dicoding.storyapss.data.remote.local.stories.StoriesEntity
import bpai.dicoding.storyapss.data.remote.network.stories.IStoriesApi
import bpai.dicoding.storyapss.domain.repository.IStoriesRepository
import bpai.dicoding.storyapss.model.CreateStory
import bpai.dicoding.storyapss.model.Stories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val storiesApi: IStoriesApi,
    private val dao:StoriesDao
): IStoriesRepository {
    override fun getListStories(): PagingSource<Int, StoriesEntity> = dao.getAllStories()

    override suspend fun createStory(payload: CreateStory): Flow<Result<Pair<Boolean, String>>> = flow {
        try {
            emit(Result.success(Pair(false,"Uploading...")))

            val mFile = payload.image.asRequestBody("image/*".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("photo",payload.image.name,mFile)

            val description = payload.description.toRequestBody(MultipartBody.FORM)
            var lat:RequestBody ? = null
            var lon:RequestBody? = null
            if(payload.lat != null && payload.lon != null){
                lat = payload.lat.toString().toRequestBody(MultipartBody.FORM)
                lon = payload.lon.toString().toRequestBody(MultipartBody.FORM)
            }



            val send = storiesApi.createdStories(description,image,lat,lon)
            if(!send.error){
                emit(Result.success(Pair(true,send.message)))
            }else{
                emit(Result.success(Pair(false,send.message)))
            }
        }catch (e:HttpException){
            val errorBody = JSONObject(e.response()!!.errorBody()!!.charStream().readText())
            if(e.code() == 500){
                emit(Result.failure(RuntimeException("Server sedang bermasalah, silahkan coba lagi")))
            }else{
                emit(Result.failure(RuntimeException("Oppps, ${errorBody.getString("message")}")))
            }
        }catch (e:Exception){
            emit(Result.failure(RuntimeException("Oppps, ${e.localizedMessage}}")))

        }
    }

    override fun getListHistoryToWidget(): Flow<ArrayList<Stories>> = flow {
        try {
            val stories = storiesApi.stories(1,10)
            val data = arrayListOf<Stories>()
            stories.listStory.forEach {
                data.add(it.toStories())
            }
            emit(data)
        }catch (e:HttpException){
            emit(arrayListOf())
        }catch (e:Exception){
            emit(arrayListOf())
        }
    }

    override fun getListStoryByLocation(): Flow<Result<List<Stories>>> = flow {
        try {
            val stories = storiesApi.stories(location = 1)
            if(stories.listStory.isNotEmpty()){
                val data = stories.listStory.map { it.toStories() }
                emit(Result.success(data))
            }else{
                emit(Result.success(arrayListOf()))
            }
        }catch (e:HttpException){
            emit(Result.failure(RuntimeException(e.localizedMessage)))
        }catch (e:Exception){
            emit(Result.failure(RuntimeException(e.localizedMessage)))
        }
    }
}