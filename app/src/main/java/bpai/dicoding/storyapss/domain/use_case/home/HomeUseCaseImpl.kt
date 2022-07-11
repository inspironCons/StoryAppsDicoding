package bpai.dicoding.storyapss.domain.use_case.home

import androidx.paging.PagingSource
import bpai.dicoding.storyapss.data.remote.local.stories.StoriesEntity
import bpai.dicoding.storyapss.domain.repository.IStoriesRepository
import javax.inject.Inject

class HomeUseCaseImpl @Inject constructor(
    private val storiesRepo: IStoriesRepository
):IHomeUseCase {
    override fun getListStory(): PagingSource<Int, StoriesEntity> = storiesRepo.getListStories()
}