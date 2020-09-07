package com.example.photoalarm.helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

/**
 * @author Axel Sanchez
 */
class AlarmHelper: KoinComponent {

    private val calendar: Calendar by inject()
    private val innerCalendar: Calendar by inject()
    private val manager: AlarmManager by inject()

    fun getCurrentDay(): String {
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Lunes"
            Calendar.TUESDAY -> "Martes"
            Calendar.WEDNESDAY -> "Miércoles"
            Calendar.THURSDAY -> "Jueves"
            Calendar.FRIDAY -> "Viernes"
            Calendar.SATURDAY -> "Sábado"
            Calendar.SUNDAY -> "Domingo"
            else -> "Lunes"
        }
    }

    fun activateAlarm(hourAlarm: Int, minuteAlarm: Int, context: Context?){
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
        innerCalendar.apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hourAlarm)
            set(Calendar.MINUTE, minuteAlarm)
        }
        manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            innerCalendar.timeInMillis,
            1000 * 60 * 10,
            pendingIntent
        )
    }
}