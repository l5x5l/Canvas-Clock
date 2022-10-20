package com.strayAlpaca.canvasclock.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext


class ClockWidgetManager private constructor(@ApplicationContext private val context : Context) {

    private val clockWidgetUpdateShape = "com.strayAlpaca.canvasclock.UPDATE_SHAPE"
    private val clockWidgetUpdateTimeHand = "com.strayAlpaca.canvasclock.UPDATE_TIME"

    companion object {
        private var instance : ClockWidgetManager ?= null

        fun init(@ApplicationContext context : Context) {
            if (instance == null)
                instance = ClockWidgetManager(context = context)
        }

        fun getInstance() : ClockWidgetManager {
            return instance!!
        }
    }

    fun updateClockWidget(appWidgetIds: List<Int>) {
        updateClockWidgetShape(appWidgetIds)
        updateClockWidgetTimeHand(appWidgetIds)
    }

    fun updateClockWidgetShape(appWidgetIds : List<Int>) {
        val intent = Intent(context, ClockWidgetProvider::class.java)
        intent.action = clockWidgetUpdateShape
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds.toIntArray())
        context.sendBroadcast(intent)
    }

    fun updateClockWidgetTimeHand(appWidgetIds: List<Int>) {
        val intent = Intent(context, ClockWidgetProvider::class.java)
        intent.action = clockWidgetUpdateTimeHand
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, arrayListOf(appWidgetIds))
        context.sendBroadcast(intent)
    }
}