package com.strayAlpaca.data.repository_impl

import com.strayAlpaca.data.datasource.room.database.ClockDatabase
import com.strayAlpaca.data.datasource.room.entities.ClockWidgetEntity
import com.strayAlpaca.data.mapper.DataLayerMapper
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.models.ClockPartData
import com.strayAlpaca.domain.repository.ClockRepository
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

    override suspend fun getClockById(clockIdx: Int): ClockData {
        val clockEntity = ClockDatabase.getInstance().getClockByIdx(clockIdx)
        val clockPartEntities = ClockDatabase.getInstance().getClockPartList(clockIdx = clockIdx)
        return DataLayerMapper.toClockData(clockEntity = clockEntity, clockPartEntityList = clockPartEntities)
    }

    override suspend fun updateClockPartList(clockPartList: ArrayList<ClockPartData>): List<Long> {
        val changedClockPartEntityList =
            clockPartList.filter { return@filter it.uiState.isSelected || it.uiState.isNew }.map {
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

    override suspend fun getClockCount(): Int {
        return ClockDatabase.getInstance().getClockCount()
    }

    override suspend fun deleteClock(clockIdx: Int): Int {
        return ClockDatabase.getInstance().deleteClock(clockIdx = clockIdx)
    }

    override suspend fun insertClock(clock: ClockData): Boolean {
        val clockIdx = ClockDatabase.getInstance().getAvailableClockIdx()

        val clockPartEntityList = clock.clockPartList.map { DataLayerMapper.toClockPartEntity(it, clockIdx) }
        val clockEntity = DataLayerMapper.toClockEntity(clock, clockIdx)

        return ClockDatabase.getInstance().insertClock(clockEntity = clockEntity, clockPartEntityList = clockPartEntityList)
    }
}