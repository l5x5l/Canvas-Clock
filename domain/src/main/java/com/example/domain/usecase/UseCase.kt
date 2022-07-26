package com.example.domain.usecase

import com.example.domain.repository.ClockRepository
import javax.inject.Inject

class UseCaseGetMainClock @Inject constructor(private val repository : ClockRepository) {
    suspend fun execute(amount : Int) = repository.getRandomClockList(amount = amount)
}

class UseCaseGetRecentClock @Inject constructor(private val repository: ClockRepository) {
    suspend fun execute(amount : Int?) = repository.getRecentClockList(amount = amount)
}