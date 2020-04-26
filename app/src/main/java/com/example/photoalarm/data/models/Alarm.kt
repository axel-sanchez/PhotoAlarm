package com.example.photoalarm.data.models

class Alarm(var id: Long,
            var label: String,
            var time: String,
            var song: String,
            var days: MutableList<String>,
            var isActive: Boolean,
            var requireVibrate: Boolean)
