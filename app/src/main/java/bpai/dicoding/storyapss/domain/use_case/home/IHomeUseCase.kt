package bpai.dicoding.storyapss.domain.use_case.home

import androidx.paging.PagingSource
import bpai.dicoding.storyapss.data.remote.local.stories.StoriesEntity
import bpai.dicoding.storyapss.model.Stories

interface IHomeUseCase {
    fun getListStory(): PagingSource<Int, StoriesEntity>
}