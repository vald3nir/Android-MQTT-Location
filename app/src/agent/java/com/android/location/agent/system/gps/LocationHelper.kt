package com.android.location.agent.system.gps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

class LocationHelper {

    private val LOCATION_REFRESH_TIME = 500
    // 3s, The Minimum Time to get location update

    private val LOCATION_REFRESH_DISTANCE = 0
    // 0m, The Minimum Distance to be changed to get location update

    @SuppressLint("MissingPermission")
    fun startListeningUserLocation(context: Context, listener: LocationCallback) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener: LocationListener = object : LocationListener {
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onLocationChanged(location: Location) {
                listener.onLocationChanged(location)
            }
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REFRESH_TIME.toLong(),
            LOCATION_REFRESH_DISTANCE.toFloat(),
            locationListener
        )
    }
}

