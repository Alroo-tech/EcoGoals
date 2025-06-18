package com.example.tusecogoals

import com.google.android.gms.maps.model.LatLng

data class Places(
    val type: String,
    val image: String,
    val latitude: Double?,
    val longitude: Double?
) {
    fun position() : LatLng {
        return LatLng(latitude ?: 0.0, longitude ?: 0.0)
    }
}