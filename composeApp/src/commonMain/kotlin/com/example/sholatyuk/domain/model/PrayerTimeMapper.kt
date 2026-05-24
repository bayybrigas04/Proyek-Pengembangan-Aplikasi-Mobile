package com.example.sholatyuk.domain.model

import kotlinx.datetime.LocalDate

data class PrayerTime(
    val id: Long = 0,
    val date: LocalDate,
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val imsak: String,
    val midnight: String,
    val latitude: Double,
    val longitude: Double,
    val cityName: String
)

enum class PrayerName(val displayName: String) {
    FAJR("Subuh"),
    SUNRISE("Terbit"),
    DHUHR("Dzuhur"),
    ASR("Ashar"),
    MAGHRIB("Maghrib"),
    ISHA("Isya"),
    MIDNIGHT("Tengah Malam")
}

data class NextPrayer(
    val name: PrayerName,
    val time: String,
    val remainingMinutes: Long
)