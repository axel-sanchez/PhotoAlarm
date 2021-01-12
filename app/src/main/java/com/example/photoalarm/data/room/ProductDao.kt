package com.example.photoalarm.data.room

import androidx.room.*
import com.example.photoalarm.data.models.Alarm
import com.example.photoalarm.data.models.AlarmXDay
import com.example.photoalarm.data.models.MyWeather

/**
 * Dao of my room database
 * @author Axel Sanchez
 */

@Dao
interface ProductDao {
    @Query("SELECT * FROM Alarm")
    suspend fun getAllAlarms(): List<Alarm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmXDay(alarmXDay: AlarmXDay): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: MyWeather): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAlarm(alarm: Alarm)

    @Query("Delete from Alarm where id = :idAlarm")
    suspend fun deleteAlarm(idAlarm: Long)

    @Query("Delete from AlarmXDay where id = :idAlarm")
    suspend fun deleteAlarmXDay(idAlarm: Long)
}