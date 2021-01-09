package com.example.photoalarm.viewmodel

import androidx.lifecycle.*
import com.example.photoalarm.data.models.MyTime
import com.example.photoalarm.domain.ChronometerUseCase
import kotlinx.coroutines.launch

/**
 * View model de [MyFragment]
 * @author Axel Sanchez
 */
class ChronometerViewModel(private val chronometerUseCase: ChronometerUseCase) : ViewModel() {

    private val listData = MutableLiveData<MyTime>()

    private fun setListData(time: MyTime) {
        listData.postValue(time)
    }

    fun getTime() {
        viewModelScope.launch {
            setListData(chronometerUseCase.getTime())
        }
    }

    fun getTimeLiveData(): LiveData<MyTime> {
        return listData
    }

    fun getIsPlayed(): Boolean{
        return chronometerUseCase.isPlaying
    }

    fun doIsPlayedTrue(){
        chronometerUseCase.isPlaying = true
    }

    fun doIsPlayedFalse(){
        chronometerUseCase.isPlaying = false
    }

    fun resetTime(){
        chronometerUseCase.time = MyTime(0, 0, 0, 0)
    }

    class ChronometerViewModelFactory(private val chronometerUseCase: ChronometerUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(ChronometerUseCase::class.java).newInstance(chronometerUseCase)
        }
    }
}