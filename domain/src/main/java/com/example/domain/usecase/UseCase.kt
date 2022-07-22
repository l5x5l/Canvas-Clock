package com.example.domain.usecase

import com.example.domain.repository.ClockRepository
import javax.inject.Inject

class UseCaseGetMainClock @Inject constructor(private val repository : ClockRepository) {
    suspend fun execute(amount : Int) = repository.getRandomClockList(amount = amount)
}

class UseCaseGetRandomClock @Inject constructor(private val repository: ClockRepository) {
    suspend fun execute() = repository.getRecentClockList(5)
}