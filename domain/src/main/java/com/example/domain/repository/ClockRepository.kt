package com.example.domain.repository

import com.example.domain.models.ClockData
import com.example.domain.models.ClockPartData

interface ClockRepository {
    suspend fun getRecentClockList(amount : Int ?= null) : ArrayList<ClockData>

    suspend fun getRandomClockList(amount : Int) : ArrayList<ClockData>

    suspend fun updateClockPartList(clockPartList : ArrayList<ClockPartData>) : List<Long>
}