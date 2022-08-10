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
        setOriginalClock(newClock)
        setMiddleSaveClock(newClock)
    }

    fun getOriginalClock() = originalClock

    fun getMiddleSaveClock() = middleSaveClock

    fun setOriginalClock(newClock : ClockData) {
        originalClock = newClock
    }

    fun setMiddleSaveClock(newClock : ClockData) {
        middleSaveClock = newClock
    }

}