package com.example.photoalarm.viewmodel

import androidx.lifecycle.*
import com.example.photoalarm.data.models.MyTime
import com.example.photoalarm.domain.ChronometerUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * View model de [MyFragment]
 * @author Axel Sanchez
 */
class ChronometerViewModel(private val chronometerUseCase: ChronometerUseCase) : ViewModel() {

    var time: MyTime = MyTime(0, 0, 0, 0)

    var isPlaying = false

    private val listData = MutableLiveData<MyTime>()

    private fun setListData(time: MyTime) {
        listData.postValue(time)
    }

    fun getTime() {
        viewModelScope.launch {
            countTimer()
        }
    }

    fun getTimeLiveData(): LiveData<MyTime> {
        return listData
    }

    private suspend fun countTimer() {
        while (isPlaying) {
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
            setListData(time)
        }
    }

    class ChronometerViewModelFactory(private val chronometerUseCase: ChronometerUseCase) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(ChronometerUseCase::class.java)
                .newInstance(chronometerUseCase)
        }
    }
}