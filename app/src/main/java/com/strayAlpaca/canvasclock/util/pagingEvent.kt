package com.strayAlpaca.canvasclock.util

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.strayAlpaca.canvasclock.models.PagingItemEvent
import com.strayAlpaca.domain.models.ClockData

fun applyPagingItemEvent(paging : PagingData<ClockData>, event : PagingItemEvent) : PagingData<ClockData> {
    return when (event) {
        is PagingItemEvent.Update<*> -> {
            paging.map {
                if (event.data is ClockData && event.data.clockIdx == it.clockIdx) {
                    return@map ClockData.deepCopy(event.data)
                } else {
                    return@map it
                }
            }
        }
        is PagingItemEvent.Delete<*> -> {
            paging.filter { (event.data is ClockData && event.data.clockIdx != it.clockIdx) }
        }
    }
}