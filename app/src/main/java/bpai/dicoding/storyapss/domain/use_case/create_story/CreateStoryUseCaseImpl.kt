package bpai.dicoding.storyapss.domain.use_case.create_story

import bpai.dicoding.storyapss.domain.repository.IStoriesRepository
import bpai.dicoding.storyapss.model.CreateStory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateStoryUseCaseImpl @Inject constructor(
    private val repo: IStoriesRepository
): ICreateStoryUseCase {
    override suspend fun create(payload: CreateStory): Flow<Result<Pair<Boolean, String>>> = repo.createStory(payload)
}