package com.example.photoalarm.application

import android.app.Application
import com.example.photoalarm.di.moduleApp
import org.koin.android.ext.android.startKoin

/**
 * @author Axel Sanchez
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, listOf(moduleApp))
    }
}