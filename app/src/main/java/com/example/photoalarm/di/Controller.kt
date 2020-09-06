package com.example.photoalarm.di

import com.example.photoalarm.data.Database
import com.example.photoalarm.data.repository.GenericRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val moduleApp = module {
    single { Database(androidContext()) }
    single { GenericRepository() }
}