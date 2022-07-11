package bpai.dicoding.storyapss.data.repository_impl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import bpai.dicoding.storyapss.data.remote.network.stories.IStoriesApi
import bpai.dicoding.storyapss.domain.repository.IStoriesRepository
import bpai.dicoding.storyapss.model.CreateStory
import bpai.dicoding.storyapss.model.Stories
import bpai.dicoding.storyapss.utils.ConstantName
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
    private val storiesApi: IStoriesApi
): IStoriesRepository {
    override fun getListStories() = object : PagingSource<Int, Stories>() {
        override fun getRefreshKey(state: PagingState<Int, Stories>): Int? = state
            .anchorPosition?.let { anchorPosition->
                val page = state.closestPageToPosition(anchorPosition)
                page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
            }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Stories> {
            return try {
                val pageNumber= params.key?: ConstantName.FIRST_PAGE_HISTORY
                val history = storiesApi.stories(page = pageNumber, size = ConstantName.PAGE_SIZE_HISTORY)

                LoadResult.Page(
                    data = history.listStory.map { it.toStories() },
                    prevKey = null,
                    nextKey = if(history.listStory.isNotEmpty()) pageNumber +1 else null
                )
            }catch (e:Exception){
                LoadResult.Error(e)
            }
        }

    }

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
            val stories = storiesApi.stories()
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
}