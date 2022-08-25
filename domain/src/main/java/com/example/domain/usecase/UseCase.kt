package com.example.domain.usecase

import com.example.domain.models.ClockData
import com.example.domain.models.ClockPartData
import com.example.domain.repository.ClockRepository
import javax.inject.Inject

class UseCaseGetMainClock @Inject constructor(private val repository : ClockRepository) {
    suspend fun execute(amount : Int) = repository.getRandomClockList(amount = amount)
}

class UseCaseGetRecentClock @Inject constructor(private val repository: ClockRepository) {
    suspend fun execute(amount : Int?) = repository.getRecentClockList(amount = amount)
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