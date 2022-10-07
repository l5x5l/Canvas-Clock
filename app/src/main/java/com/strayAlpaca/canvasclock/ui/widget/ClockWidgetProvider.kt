package com.strayAlpaca.canvasclock.ui.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.SystemClock
import android.widget.RemoteViews
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.util.WidgetSizeProvider
import com.strayAlpaca.canvasclock.util.drawClock
import com.strayAlpaca.canvasclock.util.drawTimeHand
import com.strayAlpaca.domain.usecase.UseCaseGetClock
import com.strayAlpaca.domain.usecase.UseCaseWidgetClock
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.math.min

@AndroidEntryPoint
class ClockWidgetProvider : AppWidgetProvider() {

    private val firstAlarmId = 2
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)
    @Inject
    lateinit var useCaseWidgetClock : UseCaseWidgetClock
    @Inject
    lateinit var useCaseGetClock: UseCaseGetClock

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE && context != null) {
//            cancelAlarm(context)
//            callAlarm(context, intent)
            val appwidgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = appwidgetManager.getAppWidgetIds(ComponentName(context.packageName, ClockWidgetProvider::class.java.name))
            onUpdate(context, appwidgetManager, widgetIds)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        context?.let {
            callRepeatAlarm(it)
        }
    }

    // 다시 시작시 배경이 안보임 + widget Activity 에서 시계 선택시 시침이 바로 표시안됨
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        if (context != null && appWidgetIds != null && appWidgetManager != null) {
            coroutineScope.launch {
                for (id in appWidgetIds) {
                    val clockIdx = useCaseWidgetClock.getWidgetClock(widgetId = id)
                     if (clockIdx != -1) {
                        val clock = useCaseGetClock.execute(clockIdx)
                        val widgetSize = WidgetSizeProvider(context, appWidgetManager).getWidgetsSize(id)
                        val radius = min(widgetSize.first, widgetSize.second) / 2
                        val handleBitmap = Bitmap.createBitmap(widgetSize.first, widgetSize.second, Bitmap.Config.ARGB_8888)
                        val handleCanvas = Canvas(handleBitmap)

                        val shapeBitmap = Bitmap.createBitmap(widgetSize.first, widgetSize.second, Bitmap.Config.ARGB_8888)
                        val shapeCanvas = Canvas(shapeBitmap)

                        withContext(Dispatchers.Default) {
                            val calendar = Calendar.getInstance()
                            val remoteView = RemoteViews(context.packageName, R.layout.widget_clock)

                            drawTimeHand(canvas = handleCanvas, clock = clock, mx = widgetSize.first / 2, my = widgetSize.second / 2, radius = radius,
                                hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE), second = null, is24HourMode = false )
                            remoteView.setImageViewBitmap(R.id.iv_widget_clock_handle, handleBitmap)
                            appWidgetManager.partiallyUpdateAppWidget(id, remoteView)

                            drawClock(canvas = shapeCanvas, clockPartList = clock.clockPartList, mx = widgetSize.first / 2, my = widgetSize.second / 2, radius = radius)
                            remoteView.setImageViewBitmap(R.id.iv_widget_clock_shape ,shapeBitmap)
                            appWidgetManager.partiallyUpdateAppWidget(id, remoteView)
                        }
                    }
                }
            }
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)

        context?.let {
            //cancelAlarm(context)
            cancelRepeatAlarm(it)
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)

        if (context != null && appWidgetIds != null) {
            coroutineScope.launch {
                for (appWidgetId in appWidgetIds) {
                    useCaseWidgetClock.removeWidgetClock(widgetId = appWidgetId)
                }
            }
        }
    }

    private fun callAlarm(context : Context, intent : Intent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, firstAlarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE )

        val currentMillis = System.currentTimeMillis()
        val nextTime = currentMillis - (currentMillis % 60000) + 60000  //ceil(System.currentTimeMillis() / 60000f).toLong() * 60000

        alarmManager.set(AlarmManager.RTC, nextTime, pendingIntent)
    }

    private fun cancelAlarm(context : Context) {
        val intent = Intent(context, ClockWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, firstAlarmId, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE )

        pendingIntent?.let {
            alarmManager.cancel(it)
        }
    }

    private fun callRepeatAlarm(context : Context) {
        val intent = Intent(context, ClockWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, firstAlarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE )

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 60000 ,60000, pendingIntent)
    }

    private fun cancelRepeatAlarm(context : Context) {
        val intent = Intent(context, ClockWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, firstAlarmId, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE )

        pendingIntent?.let{
            alarmManager.cancel(it)
            it.cancel()
        }
    }

}