package com.example.photoalarm.domain

import com.example.photoalarm.data.models.MyTime
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.koin.standalone.KoinComponent

/**
 * Caso de uso para
 * @author Axel Sanchez
 */
class ChronometerUseCase : KoinComponent {

    var time: MyTime = MyTime(0, 0, 0, 0)

    var isPlaying = false

    suspend fun getTime(): MyTime {
        countTimer()
        return time
    }

    private suspend fun countTimer() {
        while (true) {
            if (isPlaying) {
                delay(1)
                if (isPlaying) time.milliSeconds++
                if (time.milliSeconds == 999) {
                    time.seconds++
                    time.milliSeconds = 0
                }
                if (time.seconds == 60) {
                    time.minutes++
                    time.seconds = 0
                }

                if (time.minutes == 60) {
                    time.hour++
                    time.minutes = 0
                }
            }
        }
    }
}