package com.example.canvasclock.models

import com.example.domain.models.ClockData

class ModifyClock private constructor() {
    private var originalClock = ClockData()
    private var middleSaveClock = ClockData()

    companion object {
        private var instance : ModifyClock ?= null

        fun getInstance() : ModifyClock {
            if (instance == null){
                instance = ModifyClock()
            }
            return instance!!
        }

        fun clearInstance() {
            instance = null
        }
    }

    fun initModifyClock(newClock : ClockData){
        val copyNewClock = ClockData.deepCopy(newClock)
        for(clockPart in copyNewClock.clockPartList) { // 부품 추가에 대한 isNew 변수값을 false 로 전환
            clockPart.uiState.isNew = false
        }
        setOriginalClock(copyNewClock)
        setMiddleSaveClock(copyNewClock)
    }

    fun getOriginalClock() = originalClock

    fun getMiddleSaveClock() = middleSaveClock

    fun setOriginalClock(newClock : ClockData) {
        originalClock = ClockData.deepCopy(newClock)
    }

    fun setMiddleSaveClock(newClock : ClockData) {
        middleSaveClock = ClockData.deepCopy(newClock)
    }

    fun save(){
        originalClock = ClockData.deepCopy(middleSaveClock)
    }

}