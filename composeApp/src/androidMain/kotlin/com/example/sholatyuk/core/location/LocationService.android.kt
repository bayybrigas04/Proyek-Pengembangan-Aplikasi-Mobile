package com.example.sholatyuk.core.location

import android.content.Context

actual class LocationService(private val context: Context) {
    actual suspend fun getCurrentLocation(): LocationData? = null
    actual fun hasLocationPermission(): Boolean = false
}