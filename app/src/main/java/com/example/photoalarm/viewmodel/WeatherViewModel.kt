package com.example.photoalarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photoalarm.data.models.Result
import com.example.photoalarm.domain.WeatherUseCase

/**
 * View model de [WeatherFragment]
 * @author Axel Sanchez
 */
class WeatherViewModel(private val weatherUseCase: WeatherUseCase) : ViewModel() {

    private val listData = MutableLiveData<Result?>()

    private fun setListData(result: Result?) {
        listData.value = result
    }

    suspend fun getWeather(lat: String, lon: String) {
        setListData(weatherUseCase.getWeather(lat, lon))
    }

    fun getWeatherLiveData(): LiveData<Result?> {
        return listData
    }

    class WeatherViewModelFactory(private val weatherUseCase: WeatherUseCase) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(WeatherUseCase::class.java).newInstance(weatherUseCase)
        }
    }
}
