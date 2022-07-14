package bpai.dicoding.storyapss.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.paging.*
import bpai.dicoding.storyapss.data.remote.local.stories.StoriesRemoteMediator
import bpai.dicoding.storyapss.data.remote.network.stories.IStoriesApi
import bpai.dicoding.storyapss.database.StoryDb
import bpai.dicoding.storyapss.domain.use_case.home.IHomeUseCase
import bpai.dicoding.storyapss.model.Stories
import bpai.dicoding.storyapss.utils.ConstantName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: IHomeUseCase,
    private val database: StoryDb,
    private val service:IStoriesApi,
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    fun fetchList():Flow<PagingData<Stories>> = Pager(
        config = PagingConfig(pageSize = ConstantName.PAGE_SIZE_HISTORY),
        remoteMediator = StoriesRemoteMediator(database,service),
        pagingSourceFactory = {
            useCase.getListStory()
        }
    ).flow.map { _list->
        _list.map {
            it.toStories()
        }
    }
}