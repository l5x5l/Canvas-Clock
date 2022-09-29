package com.strayAlpaca.domain.repository

import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.models.ClockPartData

interface ClockRepository {
    suspend fun getRecentClockList(amount : Int ?= null) : ArrayList<ClockData>

    suspend fun getRandomClockList(amount : Int) : ArrayList<ClockData>

    suspend fun getClockById(clockIdx : Int) : ClockData

    suspend fun updateClockPartList(clockPartList : ArrayList<ClockPartData>) : List<Long>

    suspend fun updateClock(clock : ClockData) : Int

    suspend fun getRecentClockPage(pageIdx : Int, pageSize : Int) : ArrayList<ClockData>

    suspend fun deleteClockParts(clockParts : ArrayList<ClockPartData>) : Int

    suspend fun getClockCount() : Int

    suspend fun deleteClock(clockIdx : Int) : Int

    suspend fun insertClock(clock: ClockData) : Boolean

    suspend fun getWidgetClockId(widgetId : Int) : Int

    suspend fun setWidgetClockId(widgetId : Int, clockId : Int)

    suspend fun removeWidgetClockIdx(widgetId : Int)
}