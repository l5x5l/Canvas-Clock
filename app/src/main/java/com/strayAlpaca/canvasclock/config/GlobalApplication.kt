package com.strayAlpaca.canvasclock.config

import android.app.Application
import com.google.firebase.FirebaseApp
import com.strayAlpaca.canvasclock.ui.widget.ClockWidgetManager
import com.strayAlpaca.data.datasource.room.database.ClockDatabase
import com.strayAlpaca.data.datasource.shared_preference.SharedPreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalApplication : Application() {

    companion object {
        var isClockDBModified : Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        ClockDatabase.initDB(this)
        ClockWidgetManager.init(this)
        FirebaseApp.initializeApp(this)
        SharedPreference.setSharedPreference(getSharedPreferences("CANVAS_CLOCK", MODE_PRIVATE))
    }
}