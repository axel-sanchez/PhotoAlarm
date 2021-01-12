package com.example.photoalarm.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity class Alarm(@PrimaryKey(autoGenerate = true) var id: Long,
                    var label: String,
                    var time: String,
                    var song: String,
                    var days: MutableList<String>,
                    var isActive: Boolean,
                    var requireVibrate: Boolean)
