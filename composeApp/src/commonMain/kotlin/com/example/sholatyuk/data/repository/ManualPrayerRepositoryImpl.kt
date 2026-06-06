package com.example.sholatyuk.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.sholatyuk.data.local.SholatYukDatabase
import com.example.sholatyuk.domain.model.ManualPrayer
import com.example.sholatyuk.domain.repository.ManualPrayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ManualPrayerRepositoryImpl(
    private val database: SholatYukDatabase
) : ManualPrayerRepository {
    private val queries = database.prayerTimeQueries

    override fun getAllManualPrayers(): Flow<List<ManualPrayer>> {
        return queries.getAllManualPrayers()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { entity ->
                    ManualPrayer(
                        id = entity.id,
                        namaSholat = entity.namaSholat,
                        waktu = entity.waktu,
                        lokasi = entity.lokasi,
                        tanggalHijriah = entity.tanggalHijriah
                    )
                }
            }
    }

    override suspend fun insertManualPrayer(prayer: ManualPrayer) {
        withContext(Dispatchers.IO) {
            queries.insertManualPrayer(
                namaSholat = prayer.namaSholat,
                waktu = prayer.waktu,
                lokasi = prayer.lokasi,
                tanggalHijriah = prayer.tanggalHijriah
            )
        }
    }

    override suspend fun updateManualPrayer(prayer: ManualPrayer) {
        withContext(Dispatchers.IO) {
            queries.updateManualPrayer(
                namaSholat = prayer.namaSholat,
                waktu = prayer.waktu,
                lokasi = prayer.lokasi,
                tanggalHijriah = prayer.tanggalHijriah,
                id = prayer.id
            )
        }
    }

    override suspend fun deleteManualPrayer(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteManualPrayer(id)
        }
    }
}
