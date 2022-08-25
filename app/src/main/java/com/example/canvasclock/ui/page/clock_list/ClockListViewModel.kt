package com.example.canvasclock.ui.page.clock_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.canvasclock.ui.recycler.pagingSource.ClockPagingSource
import com.example.domain.usecase.UseCaseGetRecentClockPage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClockListViewModel @Inject constructor(
    private val useCaseGetRecentClockPage: UseCaseGetRecentClockPage
) : ViewModel() {
    val pagingData = Pager(PagingConfig(10)) {
        ClockPagingSource(useCaseGetRecentClockPage, pageSize = 10)
    }.flow.cachedIn(viewModelScope)
}