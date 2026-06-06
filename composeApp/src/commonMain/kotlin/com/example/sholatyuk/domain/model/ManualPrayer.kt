package com.example.sholatyuk.domain.model

data class ManualPrayer(
    val id: Long = 0,
    val namaSholat: String,
    val waktu: String,
    val lokasi: String,
    val tanggalHijriah: String
)
