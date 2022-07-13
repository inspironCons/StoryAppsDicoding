package bpai.dicoding.storyapss.domain.use_case.map

import bpai.dicoding.storyapss.model.Stories
import kotlinx.coroutines.flow.Flow

interface IStoryOnMapUseCase {
    fun getListStoryByLocation():Flow<Result<List<Stories>>>
}