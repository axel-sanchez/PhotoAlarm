package com.example.photoalarm.di

import com.example.photoalarm.data.Database
import com.example.photoalarm.data.repository.GenericRepository
import com.example.photoalarm.data.service.ApiService
import com.example.photoalarm.data.service.ConnectToApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val END_POINT = "https://api.openweathermap.org/data/2.5/"
val moduleApp = module {
    single { Database(androidContext()) }
    single { GenericRepository() }
    single {
        Retrofit.Builder()
        .baseUrl(END_POINT)
        .addConverterFactory(GsonConverterFactory.create())
        .build() }
    single { (get() as Retrofit).create(ApiService::class.java) }
    single { ConnectToApi() }
}