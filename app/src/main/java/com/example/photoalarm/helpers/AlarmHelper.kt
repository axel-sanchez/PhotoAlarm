package com.example.photoalarm.helpers

import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

/**
 * Clase que contiene funciones de ayuda para las alarmas
 * @author Axel Sanchez
 */
class AlarmHelper: KoinComponent {

    private val calendar: Calendar by inject()

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

    companion object{
        fun getTimeRest(alarmHour: Int, alarmMinute: Int): String{
            val innerCalendar: Calendar = Calendar.getInstance()
            val hour = innerCalendar.get(Calendar.HOUR_OF_DAY)
            //val hour = 4 //Test
            val minute = innerCalendar.get(Calendar.MINUTE)
            //val minute = 30 //Test

            var diferenceHour = alarmHour - hour
            var diferenceMinute = alarmMinute - minute

            if(diferenceMinute < 0){
                diferenceHour--
                diferenceMinute += 60
            }

            if(diferenceHour < 0) diferenceHour += 24

            return "Alarma en $diferenceHour horas y $diferenceMinute minutos"
        }
    }
}