package bpai.dicoding.storyapss.data.remote.local.stories

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import bpai.dicoding.storyapss.data.remote.local.remote_keys.RemoteKeysEntity
import bpai.dicoding.storyapss.data.remote.network.stories.IStoriesApi
import bpai.dicoding.storyapss.database.StoryDb

@OptIn(ExperimentalPagingApi::class)
class StoriesRemoteMediator(
    private val database: StoryDb,
    private val api:IStoriesApi
): RemoteMediator<Int, StoriesEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoriesEntity>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }

        }
        return try {
            val service = api.stories(page,state.config.pageSize)
            val endOfPaginationReached = service.listStory.isEmpty()

            database.withTransaction {
                if(loadType == LoadType.REFRESH){
                    database.remotesKeysDao().deleteRemoteKeys()
                    database.storiesDao().deleteAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = service.listStory.map {
                    RemoteKeysEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                val listStories = service.listStory.map { it.toStoriesEntity() }
                database.remotesKeysDao().insertAll(keys)
                database.storiesDao().insertAll(listStories)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (e:Exception){
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoriesEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data->
            database.remotesKeysDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoriesEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remotesKeysDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoriesEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remotesKeysDao().getRemoteKeysId(id)
            }
        }
    }



    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }

}