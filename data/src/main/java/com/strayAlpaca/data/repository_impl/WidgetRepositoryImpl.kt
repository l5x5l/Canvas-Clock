package com.strayAlpaca.data.repository_impl

import com.strayAlpaca.data.datasource.room.database.ClockDatabase
import com.strayAlpaca.data.datasource.room.entities.ClockWidgetEntity
import com.strayAlpaca.domain.repository.WidgetRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetRepositoryImpl @Inject constructor() : WidgetRepository {
    override suspend fun getWidgetClockId(widgetId: Int): Int {
        return ClockDatabase.getInstance().getWidgetClockIdx(widgetIdx = widgetId)
    }

    override suspend fun setWidgetClockId(widgetId: Int, clockId : Int) {
        val clockWidgetEntity = ClockWidgetEntity(clockWidgetIdx = widgetId, clockIdx = clockId)
        ClockDatabase.getInstance().setWidgetClock(clockWidgetEntity)
    }

    override suspend fun removeWidgetClockIdx(widgetId: Int) {
        val clockWidgetEntity = ClockWidgetEntity(clockWidgetIdx = widgetId, clockIdx = 0)
        ClockDatabase.getInstance().deleteWidgetClock(clockWidgetEntity)
    }
}