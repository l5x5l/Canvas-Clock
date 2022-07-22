package com.example.domain.repository

import com.example.domain.models.ClockData

interface ClockRepository {
    suspend fun getRecentClockList(amount : Int ?= null) : ArrayList<ClockData>

    suspend fun getRandomClockList(amount : Int) : ArrayList<ClockData>
}