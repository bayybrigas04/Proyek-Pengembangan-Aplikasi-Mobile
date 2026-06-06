package com.example.sholatyuk.presentation.screens.prayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sholatyuk.domain.model.ManualPrayer
import com.example.sholatyuk.domain.repository.ManualPrayerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManualPrayerViewModel(
    private val repository: ManualPrayerRepository
) : ViewModel() {

    val manualPrayers: StateFlow<List<ManualPrayer>> = repository.getAllManualPrayers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addManualPrayer(namaSholat: String, waktu: String, lokasi: String, tanggalHijriah: String) {
        viewModelScope.launch {
            repository.insertManualPrayer(
                ManualPrayer(
                    namaSholat = namaSholat,
                    waktu = waktu,
                    lokasi = lokasi,
                    tanggalHijriah = tanggalHijriah
                )
            )
        }
    }

    fun updateManualPrayer(id: Long, namaSholat: String, waktu: String, lokasi: String, tanggalHijriah: String) {
        viewModelScope.launch {
            repository.updateManualPrayer(
                ManualPrayer(
                    id = id,
                    namaSholat = namaSholat,
                    waktu = waktu,
                    lokasi = lokasi,
                    tanggalHijriah = tanggalHijriah
                )
            )
        }
    }

    fun deleteManualPrayer(id: Long) {
        viewModelScope.launch {
            repository.deleteManualPrayer(id)
        }
    }
}
