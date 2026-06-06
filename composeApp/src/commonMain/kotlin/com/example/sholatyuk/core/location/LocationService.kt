package com.example.sholatyuk.core.location

data class LocationData(
    val latitude: Double,
    val longitude: Double
)

interface LocationService {
    suspend fun getCurrentLocation(): LocationData?
    fun hasLocationPermission(): Boolean
}
