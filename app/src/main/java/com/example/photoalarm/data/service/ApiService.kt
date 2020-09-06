package com.example.photoalarm.data.service

import com.example.photoalarm.data.models.Result
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("weather")
    suspend fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") id: String, @Query("units") units: String): Response<Result?>
}