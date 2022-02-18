package com.android.location.agent.service

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import com.android.location.agent.system.AppExecutors
import com.android.location.agent.system.gps.LocationCallback
import com.android.location.agent.system.gps.LocationHelper
import com.vald3nir.mqtt.DataMessage
import com.vald3nir.mqtt.MqttController

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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
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
                            "fkmfkvmfkv", DataMessage(
                                clientID = "fdfdfd",
                                latitude = mLocation?.latitude!!,
                                longitude = mLocation?.longitude!!
                            )
                        )
                        println("valdenir 2: ${mLocation?.latitude}, ${mLocation?.longitude}")
                    }
                }
            }
        })
    }
}