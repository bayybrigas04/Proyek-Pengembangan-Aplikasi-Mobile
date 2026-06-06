package com.example.sholatyuk.core.di

import com.example.sholatyuk.core.network.HttpClientFactory
import com.example.sholatyuk.core.util.DatabaseDriverFactory
import com.example.sholatyuk.data.local.SholatYukDatabase
import com.example.sholatyuk.data.local.datastore.DataStoreFactory
import com.example.sholatyuk.data.local.datastore.create
import com.example.sholatyuk.data.remote.api.GeminiService
import com.example.sholatyuk.data.remote.api.AladhanService
import com.example.sholatyuk.data.repository.AIRepositoryImpl
import com.example.sholatyuk.data.repository.PrayerRepositoryImpl
import com.example.sholatyuk.data.repository.ManualPrayerRepositoryImpl
import com.example.sholatyuk.domain.repository.AIRepository
import com.example.sholatyuk.domain.repository.PrayerRepository
import com.example.sholatyuk.domain.repository.ManualPrayerRepository
import com.example.sholatyuk.presentation.screens.home.HomeViewModel
import com.example.sholatyuk.presentation.screens.islamai.IslamAIViewModel
import com.example.sholatyuk.presentation.screens.prayer.PrayerViewModel
import com.example.sholatyuk.presentation.screens.prayer.ManualPrayerViewModel
import com.example.sholatyuk.presentation.screens.doa.DoaViewModel
import com.example.sholatyuk.presentation.screens.profile.ProfileViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

val sharedModules = module {
    // Services
    single { HttpClientFactory.create() }
    singleOf(::GeminiService)
    singleOf(::AladhanService)

    // Storage
    single { SholatYukDatabase(get<DatabaseDriverFactory>().createDriver()) }
    single { get<DataStoreFactory>().create() }

    // Repositories
    singleOf(::AIRepositoryImpl) bind AIRepository::class
    singleOf(::PrayerRepositoryImpl) bind PrayerRepository::class
    singleOf(::ManualPrayerRepositoryImpl) bind ManualPrayerRepository::class

    // ViewModels
    factoryOf(::HomeViewModel)
    factoryOf(::PrayerViewModel)
    factoryOf(::ManualPrayerViewModel)
    factoryOf(::IslamAIViewModel)
    factoryOf(::DoaViewModel)
    singleOf(::ProfileViewModel)
}

fun initKoin(platformModules: List<org.koin.core.module.Module> = emptyList(), config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(platformModules + sharedModules)
    }
}
