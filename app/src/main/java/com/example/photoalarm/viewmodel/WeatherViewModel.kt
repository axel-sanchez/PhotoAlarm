package com.example.photoalarm.viewmodel

import androidx.lifecycle.*
import com.example.photoalarm.data.models.Result
import com.example.photoalarm.domain.WeatherUseCase
import kotlinx.coroutines.launch

/**
 * View model de [WeatherFragment]
 * @author Axel Sanchez
 */
class WeatherViewModel(private val weatherUseCase: WeatherUseCase, private val latitude: String, private val longitude: String) : ViewModel() {

    private val listData: MutableLiveData<Result?> by lazy {
        MutableLiveData<Result?>().also {
            getWeather()
        }
    }

    private fun setListData(result: Result?) {
        listData.value = result
    }

    private fun getWeather() {
        viewModelScope.launch {
            setListData(weatherUseCase.getWeather(latitude, longitude))
        }
    }

    fun getWeatherLiveData(): LiveData<Result?> {
        return listData
    }

    class WeatherViewModelFactory(private val weatherUseCase: WeatherUseCase, private val latitude: String, private val longitude: String) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(WeatherUseCase::class.java, String::class.java,String::class.java).newInstance(weatherUseCase, latitude, longitude)
        }
    }
}
