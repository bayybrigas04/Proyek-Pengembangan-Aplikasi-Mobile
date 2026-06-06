package com.example.sholatyuk.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sholatyuk.domain.model.PrayerTime
import com.example.sholatyuk.domain.repository.PrayerRepository
import com.example.sholatyuk.core.location.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeViewModel(
    private val prayerRepository: PrayerRepository,
    private val locationService: LocationService
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchPrayerTimes()
    }

    fun fetchPrayerTimes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val location = locationService.getCurrentLocation()
                if (location == null) {
                    _uiState.update { it.copy(isLoading = false, error = "Lokasi tidak tersedia.", showGpsDialog = true) }
                    return@launch
                }
                val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                
                prayerRepository.fetchAndSavePrayerTime(
                    location.latitude,
                    location.longitude,
                    today
                ).onSuccess { prayerTime ->
                    _uiState.update { it.copy(isLoading = false, prayerTime = prayerTime) }
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun dismissGpsDialog() {
        _uiState.update { it.copy(showGpsDialog = false) }
    }

    fun onOpenGpsSettings() {
        // Implementasi buka pengaturan GPS (platform specific)
    }
}
