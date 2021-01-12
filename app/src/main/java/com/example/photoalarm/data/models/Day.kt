package com.example.photoalarm.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity class Day(@PrimaryKey(autoGenerate = true) var id: Long, var name: String)
