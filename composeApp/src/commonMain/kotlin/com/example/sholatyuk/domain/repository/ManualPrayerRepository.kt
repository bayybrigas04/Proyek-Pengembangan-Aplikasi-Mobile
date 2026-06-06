package com.example.sholatyuk.domain.repository

import com.example.sholatyuk.domain.model.ManualPrayer
import kotlinx.coroutines.flow.Flow

interface ManualPrayerRepository {
    fun getAllManualPrayers(): Flow<List<ManualPrayer>>
    suspend fun insertManualPrayer(prayer: ManualPrayer)
    suspend fun updateManualPrayer(prayer: ManualPrayer)
    suspend fun deleteManualPrayer(id: Long)
}
