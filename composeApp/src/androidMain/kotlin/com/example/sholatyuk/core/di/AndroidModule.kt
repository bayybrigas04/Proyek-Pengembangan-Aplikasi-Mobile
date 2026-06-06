package com.example.sholatyuk.core.di

import com.example.sholatyuk.core.util.DatabaseDriverFactory
import com.example.sholatyuk.data.local.datastore.DataStoreFactory
import com.example.sholatyuk.core.location.LocationService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android-specific Koin module.
 */
val androidModule = module {
    single { DatabaseDriverFactory(androidContext()) }
    single { DataStoreFactory(androidContext()) }
    single { LocationService(androidContext()) }
}
