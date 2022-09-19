package com.example.canvasclock.ui.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.domain.utils.getCurrentTime
import kotlin.math.ceil

class ClockWidgetProvider : AppWidgetProvider() {

    private val firstAlarmId = 2
    private val repeatAlarmId = 3

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val action = intent?.action ?: return

        if (action == AppWidgetManager.ACTION_APPWIDGET_UPDATE && context != null) {
            cancelRepeatAlarm(context)
            callRepeatAlarm(context, intent)

        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

        if (context != null){
            callFirstAlarm(context)
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        Log.d("onUpdate", "testing...")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)

        if (context != null){
            cancelRepeatAlarm(context)
            cancelFirstAlarm(context)
        }
    }

    private fun callFirstAlarm(context : Context) {
        Log.d("call FirstAlarm", getCurrentTime())
        val intent = Intent(context, ClockWidgetProvider::class.java)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, firstAlarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE )

        val nextTime = ceil(System.currentTimeMillis() / 60000f).toLong() * 60000

        alarmManager.set(AlarmManager.RTC, nextTime, pendingIntent)
    }

    private fun cancelFirstAlarm(context : Context) {

        Log.d("cancel FirstAlarm",getCurrentTime())
        val intent = Intent(context, ClockWidgetProvider::class.java)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, firstAlarmId, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE )

        pendingIntent?.let {
            alarmManager.cancel(it)
        }
    }

    private fun callRepeatAlarm(context : Context, intent : Intent) {
        Log.d("call RepeatAlarm", getCurrentTime())
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, repeatAlarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE )

        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 60000, pendingIntent)
    }

    private fun cancelRepeatAlarm(context : Context){
        Log.d("cancel RepeatAlarm", getCurrentTime())
        val intent = Intent(context, ClockWidgetProvider::class.java)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, repeatAlarmId, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE )

        pendingIntent?.let {
            alarmManager.cancel(it)
        }
    }

}