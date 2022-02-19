package com.vald3nir.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder

class LocationService : Service() {

    private val mqtt = MqttController()

    companion object {
        var mLocation: Location? = null
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mqtt.connect()
        getLocation()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getLocation()
        return START_STICKY
    }

    private fun getLocation() {
        LocationHelper().startListeningUserLocation(this, object : LocationCallback {
            override fun onLocationChanged(location: Location?) {
                AppExecutors.instance?.networkIO()?.execute {
                    mLocation = location
                    mLocation?.let {
                        mqtt.publish(
                            DataMessage(
                                latitude = mLocation?.latitude, longitude = mLocation?.longitude
                            )
                        )
                    }
                }
            }
        })
    }
}

interface LocationCallback {
    fun onLocationChanged(location: Location?)
}

class LocationHelper {

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
            500L,   // 3s, The Minimum Time to get location update
            0000f, // 1Km, The Minimum Distance to be changed to get location update
            locationListener
        )
    }
}