package com.strayAlpaca.canvasclock.ui.page.clock_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.strayAlpaca.canvasclock.models.PagingItemEvent
import com.strayAlpaca.canvasclock.ui.recycler.pagingSource.ClockPagingSource
import com.strayAlpaca.canvasclock.util.applyPagingItemEvent
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.usecase.UseCaseGetRecentClockPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class ClockListViewModel @Inject constructor(
    private val useCaseGetRecentClockPage: UseCaseGetRecentClockPage
) : ViewModel() {
    // list 형식인 이유 : 삭제 후 수정과 같이 crud 작업을 여러 번 수행하는 경우, 이전 작업이 무시된다. (원본 pagingData 가 유지되므로)
    private val pagingItemEvent = MutableStateFlow<List<PagingItemEvent>>(emptyList())

    val pagingData = Pager(PagingConfig(10)) {
        ClockPagingSource(useCaseGetRecentClockPage, pageSize = 10)
    }.flow.cachedIn(viewModelScope).combine(pagingItemEvent) { pagingData, event ->
        event.fold(pagingData) { data, e ->
            applyPagingItemEvent(paging = data, event = e)
        }
    }

    fun applyDelete(targetClock : ClockData){
        pagingItemEvent.value += PagingItemEvent.Delete(targetClock)
    }

    fun applyUpdate(targetClock : ClockData){
        pagingItemEvent.value += PagingItemEvent.Update(targetClock)
    }
}