package bpai.dicoding.storyapss.domain.use_case.create_story

import bpai.dicoding.storyapss.model.CreateStory
import kotlinx.coroutines.flow.Flow

interface ICreateStoryUseCase {
    suspend fun create(payload: CreateStory): Flow<Result<Pair<Boolean,String>>>
}