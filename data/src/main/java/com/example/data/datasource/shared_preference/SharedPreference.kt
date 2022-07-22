package com.example.data.datasource.shared_preference

import android.content.SharedPreferences

object SharedPreference {
    private lateinit var globalSharedPreferences: SharedPreferences

    fun setSharedPreference(sharedPreferences: SharedPreferences){
        globalSharedPreferences = sharedPreferences
    }

    fun getInstance() = globalSharedPreferences
}