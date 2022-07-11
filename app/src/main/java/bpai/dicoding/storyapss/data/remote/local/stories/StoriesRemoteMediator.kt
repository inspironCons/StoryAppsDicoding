package bpai.dicoding.storyapss.data.remote.local.stories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import bpai.dicoding.storyapss.data.remote.network.stories.IStoriesApi
import bpai.dicoding.storyapss.data.remote.network.stories.StoriesDto

@OptIn(ExperimentalPagingApi::class)
class StoriesRemoteMediator(
    private val dao: StoriesDao,
    private val api:IStoriesApi
): RemoteMediator<Int, StoriesEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoriesEntity>
    ): MediatorResult {
        TODO("Not yet implemented")
    }


}