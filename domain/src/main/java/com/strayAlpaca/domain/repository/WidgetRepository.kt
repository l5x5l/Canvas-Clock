package com.strayAlpaca.domain.repository

interface WidgetRepository {
    suspend fun getWidgetClockId(widgetId : Int) : Int

    suspend fun setWidgetClockId(widgetId : Int, clockId : Int)

    suspend fun removeWidgetClockIdx(widgetId : Int)
}