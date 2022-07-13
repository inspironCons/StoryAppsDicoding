package bpai.dicoding.storyapss.domain.repository

import androidx.paging.PagingSource
import bpai.dicoding.storyapss.data.remote.local.stories.StoriesEntity
import bpai.dicoding.storyapss.model.CreateStory
import bpai.dicoding.storyapss.model.Stories
import kotlinx.coroutines.flow.Flow

interface IStoriesRepository {
    fun getListStories():PagingSource<Int, StoriesEntity>
    suspend fun createStory(payload: CreateStory): Flow<Result<Pair<Boolean,String>>>
    fun getListHistoryToWidget():Flow<ArrayList<Stories>>
    fun getListStoryByLocation():Flow<Result<List<Stories>>>
}