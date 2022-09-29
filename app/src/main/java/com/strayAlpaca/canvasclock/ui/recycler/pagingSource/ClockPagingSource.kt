package com.strayAlpaca.canvasclock.ui.recycler.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.usecase.UseCaseGetRecentClockPage
import java.lang.Exception

class ClockPagingSource(private val useCaseGetRecentClockPage: UseCaseGetRecentClockPage, private val pageSize : Int = 10) : PagingSource<Int, ClockData>() {
    override fun getRefreshKey(state: PagingState<Int, ClockData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?:
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClockData> {
        return try {
            val pageIdx = params.key ?: 0
            val response = useCaseGetRecentClockPage.execute(pageIdx = pageIdx, pageSize = pageSize)
            val nextKey = if (response.isEmpty()) null else pageIdx + 1
            val prevKey = if (pageIdx == 0) null else pageIdx - 1
            LoadResult.Page(data = response, prevKey = prevKey, nextKey = nextKey)
        } catch (e : Exception) {
            LoadResult.Error(e)
        }
    }
}