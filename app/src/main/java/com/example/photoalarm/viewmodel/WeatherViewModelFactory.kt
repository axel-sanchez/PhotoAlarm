package com.example.photoalarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.photoalarm.domain.WeatherUseCase

/**
 * Factory de nuestro [WeatherViewModel]
 * @author Axel Sanchez
 */
class WeatherViewModelFactory(private val weatherUseCase: WeatherUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(WeatherUseCase::class.java).newInstance(weatherUseCase)
    }
}