package bpai.dicoding.storyapss.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import bpai.dicoding.storyapss.domain.use_case.home.IHomeUseCase
import bpai.dicoding.storyapss.model.Stories
import bpai.dicoding.storyapss.utils.ConstantName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: IHomeUseCase
) : ViewModel() {
    fun fetchList():Flow<PagingData<Stories>> = Pager(
        config = PagingConfig(pageSize = ConstantName.PAGE_SIZE_HISTORY),
        pagingSourceFactory = {
            useCase.getListStory()
        }
    ).flow.cachedIn(viewModelScope)
}