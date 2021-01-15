package com.example.photoalarm.data.room

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters
import com.example.photoalarm.data.models.Alarm
import com.example.photoalarm.data.models.AlarmXDay
import com.example.photoalarm.data.models.Day
import com.example.photoalarm.data.models.MyWeather

/**
 * Room Database
 * @author Axel Sanchez
 */
@Database(
    entities = [Alarm::class, Day::class, MyWeather::class, AlarmXDay::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun productDao(): AlarmDao
}