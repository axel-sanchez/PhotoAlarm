package com.example.photoalarm.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Axel Sanchez
 */
@Entity class AlarmXDay(@PrimaryKey(autoGenerate = true) var id: Long, var idAlarm: Long, var idDay: Int) {
}