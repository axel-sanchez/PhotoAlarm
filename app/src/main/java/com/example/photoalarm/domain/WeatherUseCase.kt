package com.example.photoalarm.domain

import com.example.photoalarm.data.models.Result
import com.example.photoalarm.data.service.ConnectToApi
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Caso de uso para [WeatherFragment]
 * @author Axel Sanchez
 */
class WeatherUseCase: KoinComponent {
    private val api: ConnectToApi by inject()

    /**
     * Obtengo el clima
     * @return devuelve un [Result]
     */
    suspend fun getWeather(lat: String, lon: String): Result? {
        var response = api.getWeather(lat, lon)
        return response.value
    }
}