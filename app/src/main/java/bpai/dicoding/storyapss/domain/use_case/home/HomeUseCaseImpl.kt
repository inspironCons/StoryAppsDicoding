package bpai.dicoding.storyapss.domain.use_case.home

import androidx.paging.PagingSource
import bpai.dicoding.storyapss.domain.repository.IStoriesRepository
import bpai.dicoding.storyapss.model.Stories
import javax.inject.Inject

class HomeUseCaseImpl @Inject constructor(
    private val storiesRepo: IStoriesRepository
):IHomeUseCase {
    override fun getListStory(): PagingSource<Int, Stories> = storiesRepo.getListStories()
}