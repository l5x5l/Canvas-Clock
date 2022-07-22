package com.example.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.datasource.room.entities.ClockEntity
import com.example.data.datasource.room.entities.ClockPartEntity

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
}