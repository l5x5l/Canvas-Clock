package com.example.data.repository_impl

import com.example.data.datasource.room.database.ClockDatabase
import com.example.data.mapper.Mapper
import com.example.domain.models.ClockData
import com.example.domain.repository.ClockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClockRepositoryImpl @Inject constructor() : ClockRepository {
    override suspend fun getRecentClockList(amount: Int?): ArrayList<ClockData> {
        val clockList = arrayListOf<ClockData>()

        val clockEntities = ClockDatabase.getInstance().getRecentClock(amount = amount)
        for (clockEntity in clockEntities) {
            val clockPartEntities = ClockDatabase.getInstance().getClockPartList(clockIdx = clockEntity.clockIdx)
            clockList.add(Mapper.toClockData(clockEntity = clockEntity, clockPartEntityList = clockPartEntities))
        }

        return clockList
    }

    override suspend fun getRandomClockList(amount: Int): ArrayList<ClockData> {
        val clockList = arrayListOf<ClockData>()

        val clockEntities = ClockDatabase.getInstance().getRandomClock(amount = amount)
        for (clockEntity in clockEntities) {
            val clockPartEntities = ClockDatabase.getInstance().getClockPartList(clockIdx = clockEntity.clockIdx)
            clockList.add(Mapper.toClockData(clockEntity = clockEntity, clockPartEntityList = clockPartEntities))
        }

        return clockList
    }
}