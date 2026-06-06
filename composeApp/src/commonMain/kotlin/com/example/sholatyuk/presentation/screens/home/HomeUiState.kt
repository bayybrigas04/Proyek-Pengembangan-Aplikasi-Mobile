package com.example.sholatyuk.presentation.screens.home

import com.example.sholatyuk.domain.model.PrayerTime

data class HomeUiState(
    val isLoading: Boolean = false,
    val prayerTime: PrayerTime? = null,
    val error: String? = null,
    val showGpsDialog: Boolean = false
)
