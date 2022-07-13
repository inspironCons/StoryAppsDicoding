package bpai.dicoding.storyapss.domain.use_case.map

import bpai.dicoding.storyapss.domain.repository.IStoriesRepository
import bpai.dicoding.storyapss.model.Stories
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoryOnMapUseCaseImpl @Inject constructor(
    private val storiesRepo:IStoriesRepository
):IStoryOnMapUseCase {
    override fun getListStoryByLocation(): Flow<Result<List<Stories>>> = storiesRepo.getListStoryByLocation()
}