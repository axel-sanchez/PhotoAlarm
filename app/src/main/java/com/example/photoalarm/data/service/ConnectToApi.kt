package com.example.photoalarm.data.service

import androidx.lifecycle.MutableLiveData
import com.example.photoalarm.data.models.Result
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Esta clase es la encargada de conectarse a las api's
 * @author Axel Sanchez
 */
class ConnectToApi: KoinComponent {
    private val apiId = "3cfc1d5c1a8a4e9709fd07398c77d1af"

    private val service: ApiService by inject()

    suspend fun getWeather(lat: String, lon: String): MutableLiveData<Result?> {
        var mutableLiveData = MutableLiveData<Result?>()
        var response = service.getWeather(lat, lon, apiId, "metric")
        if(response.isSuccessful) mutableLiveData.value = response.body()
        else mutableLiveData.value = null
        return mutableLiveData
    }
}