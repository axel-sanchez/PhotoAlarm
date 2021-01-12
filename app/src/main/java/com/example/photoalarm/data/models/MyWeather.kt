package com.example.photoalarm.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity class MyWeather(@PrimaryKey(autoGenerate = true) var id: Long,
                        var temp: Int,
                        var timeLastRequest: Date)