package com.example.photoalarm.data.room

import androidx.room.TypeConverter
import com.example.photoalarm.data.models.Alarm
import com.example.photoalarm.data.models.Day
import com.example.photoalarm.data.models.MyWeather
import com.google.gson.Gson
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

/**
 * @author Axel Sanchez
 */
class Converters: KoinComponent {
    private val gson: Gson by inject()

    @TypeConverter
    fun fromAlarm(alarm: Alarm?): String? {
        alarm?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toAlarm(alarmString: String?): Alarm? {
        alarmString?.let {
            return gson.fromJson(it, Alarm::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromDay(day: Day?): String? {
        day?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toDate(dateString: String?): Date? {
        dateString?.let {
            return gson.fromJson(it, Date::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromDate(date: Date?): String? {
        date?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toDay(dayString: String?): Day? {
        dayString?.let {
            return gson.fromJson(it, Day::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromMyWeather(myWeather: MyWeather?): String? {
        myWeather?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toMyWeather(myWeatherString: String?): MyWeather? {
        myWeatherString?.let {
            return gson.fromJson(it, MyWeather::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromDays(days: List<String?>?): String? {
        var response = ""
        days?.let {
            for (i in days.indices) {
                response += if (i == 0) days[i]
                else ";${days[i]}"
            }
        } ?: return null
        return response
    }

    @TypeConverter
    fun toDays(concat: String?): List<String?>? {
        val list = concat?.split(";")
        list?.let {
            return it.map { str -> if (str != "null") str else null }
        } ?: return null
    }
}