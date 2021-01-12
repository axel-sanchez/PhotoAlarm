package com.example.photoalarm.di

import android.app.AlarmManager
import android.content.Context
import androidx.room.Room
import com.example.photoalarm.data.models.Day
import com.example.photoalarm.data.room.Database
import com.example.photoalarm.data.service.ApiService
import com.example.photoalarm.data.service.ConnectToApi
import com.example.photoalarm.domain.ChronometerUseCase
import com.example.photoalarm.helpers.AlarmHelper
import com.example.photoalarm.viewmodel.ChronometerViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

private const val END_POINT = "https://api.openweathermap.org/data/2.5/"
val moduleApp = module {
    single {
        Retrofit.Builder()
            .baseUrl(END_POINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { (get() as Retrofit).create(ApiService::class.java) }
    single { ConnectToApi() }

    single {
        listOf(
            Day(1, "Lunes"),
            Day(2, "Martes"),
            Day(3, "Miércoles"),
            Day(4, "Jueves"),
            Day(5, "Viernes"),
            Day(6, "Sábado"),
            Day(7, "Domingo")
        )
    }
    single { AlarmHelper() }
    single { Calendar.getInstance() }
    single { androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    single { ChronometerUseCase() }
    single { ChronometerViewModel.ChronometerViewModelFactory(get()) }

    single { Room
        .databaseBuilder(androidContext(), Database::class.java, "photoAlarmDB.db3")
        .build() }

    single { Gson() }
}