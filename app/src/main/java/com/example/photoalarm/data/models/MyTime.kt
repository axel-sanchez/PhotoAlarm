package com.example.photoalarm.data.models

import androidx.room.Entity

/**
 * @author Axel Sanchez
 */
@Entity class MyTime(var milliSeconds: Int,
                         var seconds: Int,
                         var minutes: Int,
                         var hour: Int) {
}