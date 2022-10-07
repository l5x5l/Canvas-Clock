package com.strayAlpaca.data.datasource.room.dao

import androidx.room.*
import com.strayAlpaca.data.datasource.room.entities.ClockEntity
import com.strayAlpaca.data.datasource.room.entities.ClockPartEntity
import com.strayAlpaca.data.datasource.room.entities.ClockWidgetEntity

@Dao
interface ClockDao {
    @Insert
    suspend fun insertClock(clock : ClockEntity)

    @Insert
    suspend fun insertClockPart(clockPart : ClockPartEntity)

    @Query("SELECT * FROM clock ORDER BY RANDOM() LIMIT :amount")
    suspend fun getRandomClock(amount : Int) : List<ClockEntity>

    @Query("SELECT clockIdx FROM clock")
    suspend fun getClockIdxList() : List<Int>

    @Query("SELECT * FROM clock ORDER BY lastModifiedTime DESC LIMIT :amount")
    suspend fun getRecentClock(amount : Int) : List<ClockEntity>

    @Query("SELECT * FROM clock ORDER BY lastModifiedTime")
    suspend fun getRecentClockAll() : List<ClockEntity>

    @Query("SELECT * FROM clock WHERE clockIdx = :clockIdx")
    suspend fun getClockByIdx(clockIdx : Int) : ClockEntity

    @Query("SELECT * FROM clock_part WHERE clockIdx = :clockIdx")
    suspend fun getClockPart(clockIdx : Int) : List<ClockPartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateClockPartList(clockPartList : List<ClockPartEntity>) : List<Long>

    @Update(entity = ClockEntity::class)
    suspend fun updateClock(clock: ClockEntity) : Int

    @Query("SELECT * FROM clock ORDER BY lastModifiedTime DESC LIMIT :pageSize OFFSET :pageIdx * :pageSize")
    suspend fun getRecentClockPage(pageIdx : Int, pageSize : Int) : List<ClockEntity>

    @Delete
    suspend fun deleteClockPartList(clockParts : List<ClockPartEntity>) : Int

    @Query("SELECT COUNT(clockIdx) FROM clock")
    suspend fun getClockCount() : Int

    @Query("DELETE FROM clock WHERE clockIdx = :clockIdx")
    suspend fun deleteClock(clockIdx : Int) : Int

    @Query("SELECT clockIdx FROM clock ORDER BY clockIdx DESC LIMIT 1")
    suspend fun getLargestClockIdx() : Int

    @Query("SELECT clockIdx FROM clock_widget WHERE clockWidgetIdx = :widgetIdx")
    suspend fun getWidgetUseClock(widgetIdx : Int) : List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWidgetClock(widgetClockPair : ClockWidgetEntity)

    @Update(entity = ClockWidgetEntity::class)
    suspend fun updateWidgetClock(widgetClockPair : ClockWidgetEntity) : Int

    @Delete
    suspend fun deleteWidgetClock(widgetClockPair: ClockWidgetEntity) : Int
}