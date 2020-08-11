package com.example.photoalarm.ui.view.interfaces

import com.example.photoalarm.data.models.Welcome
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("weather")
    fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") id: String, @Query("units") units: String): Call<Welcome>
}