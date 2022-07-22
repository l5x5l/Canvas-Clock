package com.example.data.datasource.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.datasource.room.dao.ClockDao
import com.example.data.datasource.room.entities.ClockEntity
import com.example.data.datasource.room.entities.ClockPartEntity
import com.example.data.mapper.Mapper
import com.example.domain.models.ClockData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@Database(
    entities = [ ClockEntity::class, ClockPartEntity::class ],
    version = 1
)
abstract class ClockDatabase : RoomDatabase() {
    abstract fun clockDao() : ClockDao

    /**
     * 현재 db 에 저장된 시계의 수가 0인지를 리턴합니다.
     * true 인 경우 0개입니다.
     */
    suspend fun checkDatabaseIsEmpty() : Boolean {
        return (clockDao().getClockIdxList().isEmpty())
    }

    /**
     * 개발자가 지정해놓은 기본 시계 데이터를 db에 저장합니다.
     */
    suspend fun setInitData(){
        val defaultClockList = ClockData.getDefaultClockList()
        for (defaultClock in defaultClockList) {
            clockDao().insertClock(Mapper.toClockEntity(clock =defaultClock))
            for (clockPart in defaultClock.clockPartList){
                clockDao().insertClockPart(Mapper.toClockPartEntity(clockPart = clockPart))
            }
        }
    }

    /**
     * 저장된 시계 중 인자로 전달한 수만큼 랜덤하게 가져옵니다.
     */
    suspend fun getRandomClock(amount : Int) : List<ClockEntity> {
        return (clockDao().getRandomClock(amount = amount))
    }

    /**
     * 저장된 시계 중 인자로 전달한 수만큼 랜덤하게 가져옵니다.
     * 인자가 null 인 경우 모든 데이터를 불러옵니다.
     */
    suspend fun getRecentClock(amount : Int?) : List<ClockEntity>{
        return if (amount == null){
            clockDao().getRecentClockAll()
        } else {
            clockDao().getRecentClock(amount = amount)
        }
    }

    /**
     * 인자로 전달받은 시계에 해당하는 시계 부품들을 전부 가져옵니다.
     */
    suspend fun getClockPartList(clockIdx : Int) : List<ClockPartEntity> {
        return clockDao().getClockPart(clockIdx = clockIdx)
    }

    companion object {
        private var instance : ClockDatabase ?= null

        /**
         * room database 를 세팅합니다.
         * 만약 초기 데이터가 설정되어있지 않은 경우, setInitData를 호출합니다.
         * 아닌 경우, 단순하게 인스턴스를 생성해 리턴합니다.
         */
        fun initDB(context : Context) : ClockDatabase {
            if (instance == null){
                runBlocking(Dispatchers.Default) {
                    instance = Room.databaseBuilder(context.applicationContext, ClockDatabase::class.java, "Clock").build()
                    if (instance!!.checkDatabaseIsEmpty()) { // 만약 db 에 데이터가 없는 경우 (= 처음 접속한 경우) 기본 데이터로 db 를 세팅합니다.
                        instance!!.setInitData()
                    }
                }
            }
            return instance!!
        }

        /**
         * room database 싱글톤 객체를 리턴합니다.
         * 본 함수를 호출하기 전 반드시 initDB 를 호출해야 합니다.
         */
        fun getInstance() : ClockDatabase {
            return instance!!
        }
    }
}