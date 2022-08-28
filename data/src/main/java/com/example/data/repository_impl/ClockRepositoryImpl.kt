package com.example.data.repository_impl

import com.example.data.datasource.room.database.ClockDatabase
import com.example.data.mapper.DataLayerMapper
import com.example.domain.models.ClockData
import com.example.domain.models.ClockPartData
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
            clockList.add(DataLayerMapper.toClockData(clockEntity = clockEntity, clockPartEntityList = clockPartEntities))
        }

        return clockList
    }

    override suspend fun getRandomClockList(amount: Int): ArrayList<ClockData> {
        val clockList = arrayListOf<ClockData>()

        val clockEntities = ClockDatabase.getInstance().getRandomClock(amount = amount)
        for (clockEntity in clockEntities) {
            val clockPartEntities = ClockDatabase.getInstance().getClockPartList(clockIdx = clockEntity.clockIdx)
            clockList.add(DataLayerMapper.toClockData(clockEntity = clockEntity, clockPartEntityList = clockPartEntities))
        }

        return clockList
    }

    override suspend fun updateClockPartList(clockPartList: ArrayList<ClockPartData>): List<Long> {
        val changedClockPartEntityList =
            clockPartList.filter { return@filter it.uiState.isSelected }.map {
                DataLayerMapper.toClockPartEntity(it)
            }
        return ClockDatabase.getInstance().updateClockPartList(changedClockPartEntityList)
    }

    override suspend fun updateClock(clock: ClockData): Int {
        val clockEntity = DataLayerMapper.toClockEntity(clock)
        return ClockDatabase.getInstance().updateClock(clockEntity)
    }

    override suspend fun getRecentClockPage(pageIdx: Int, pageSize: Int): ArrayList<ClockData> {
        val clockList = arrayListOf<ClockData>()

        val clockEntities = ClockDatabase.getInstance().getRecentClockPage(pageIdx = pageIdx, pageSize = pageSize)
        for (clockEntity in clockEntities) {
            val clockPartEntities = ClockDatabase.getInstance().getClockPartList(clockIdx = clockEntity.clockIdx)
            clockList.add(DataLayerMapper.toClockData(clockEntity = clockEntity, clockPartEntityList = clockPartEntities))
        }
        return clockList
    }

    override suspend fun deleteClockParts(clockParts: ArrayList<ClockPartData>): Int {
        val clockPartEntities = clockParts.map { DataLayerMapper.toClockPartEntity(it) }
        return ClockDatabase.getInstance().deleteClockPart(clockParts = clockPartEntities)
    }
}