package com.strayAlpaca.domain.usecase

import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.models.ClockPartData
import com.strayAlpaca.domain.repository.ClockRepository
import com.strayAlpaca.domain.repository.FaqRepository
import com.strayAlpaca.domain.repository.WidgetRepository
import javax.inject.Inject

class UseCaseGetMainClock @Inject constructor(private val repository : ClockRepository) {
    suspend fun execute(amount : Int) = repository.getRandomClockList(amount = amount)
}

class UseCaseGetRecentClock @Inject constructor(private val repository: ClockRepository) {
    suspend fun execute(amount : Int?) = repository.getRecentClockList(amount = amount)
}

class UseCaseGetClock @Inject constructor(private val repository: ClockRepository) {
    suspend fun execute(clockIdx : Int) = repository.getClockById(clockIdx)
}

class UseCaseUpdateClockPart @Inject constructor(private val repository: ClockRepository) {
    suspend fun execute(clockPartList : ArrayList<ClockPartData>) = repository.updateClockPartList(clockPartList = clockPartList)
}

class UseCaseUpdateClock @Inject constructor(private val repository : ClockRepository) {
    suspend fun execute(clock : ClockData) = repository.updateClock(clock)
}

class UseCaseGetRecentClockPage @Inject constructor(private val repository: ClockRepository) {
    suspend fun execute(pageIdx : Int, pageSize : Int) = repository.getRecentClockPage(pageIdx = pageIdx, pageSize = pageSize)
}

class UseCaseDeleteClockParts @Inject constructor(private val repository: ClockRepository) {
    suspend fun execute(clockParts : ArrayList<ClockPartData>) = repository.deleteClockParts(clockParts = clockParts)
}

class UseCaseGetClockCount @Inject constructor(private val repository : ClockRepository) {
    suspend fun execute() = repository.getClockCount()
}

class UseCaseDeleteClock @Inject constructor(private val repository : ClockRepository) {
    suspend fun execute(clockIdx : Int) = repository.deleteClock(clockIdx = clockIdx)
}

class UseCaseInsertClock @Inject constructor(private val repository: ClockRepository) {
    suspend fun execute(clock : ClockData) = repository.insertClock(clock)
}

class UseCaseWidgetClock @Inject constructor(private val widgetRepository : WidgetRepository, private val clockRepository: ClockRepository) {
    suspend fun getWidgetClock(widgetId : Int) = widgetRepository.getWidgetClockId(widgetId)

    suspend fun setWidgetClock(widgetId : Int, clockId : Int) = widgetRepository.setWidgetClockId(widgetId = widgetId, clockId = clockId)

    suspend fun removeWidgetClock(widgetId : Int) = widgetRepository.removeWidgetClockIdx(widgetId = widgetId)

    suspend fun getWidgetIdsByClockIdx(clockIdx : Int) = widgetRepository.getWidgetIdsByClockIdx(clockIdx = clockIdx)

    suspend fun setRandomClockToRemoveClockWidget(removedClockIdx : Int) {
        val newClockIdx = clockRepository.getRandomClockList(1)[0].clockIdx
        widgetRepository.changeWidgetClock(prevClockIdx = removedClockIdx, newClockIdx = newClockIdx)
    }
}

class UseCaseFaq @Inject constructor(private val repository : FaqRepository) {
    suspend fun getFaqList() = repository.getFaqList()
}