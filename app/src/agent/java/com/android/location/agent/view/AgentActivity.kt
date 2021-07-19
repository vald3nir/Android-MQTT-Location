package com.android.location.agent.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.location.agent.service.LocationService
import com.android.location.agent.system.gps.LocationCallback
import com.android.location.agent.system.gps.LocationHelper
import com.android.location.agent.system.identifer.DeviceIdentifier
import com.vald3nir.R
import kotlinx.android.synthetic.agent.activity_agent.*


class AgentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent)
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission()) {
            getDeviceIMEI()
            startLocationService()
            getLocation()
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        val resultFineLocation = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val resultCoarseLocation = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val resultPhoneState = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_PHONE_STATE
        )
        return resultFineLocation == PackageManager.PERMISSION_GRANTED &&
                resultCoarseLocation == PackageManager.PERMISSION_GRANTED &&
                resultPhoneState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            ), 1
        )
    }

    private fun startLocationService() {
        ContextCompat.startForegroundService(
            this, Intent(this, LocationService::class.java)
        )
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceIMEI() {
        txvUUID.text = DeviceIdentifier().getDeviceId(this)
    }

    @SuppressLint("SetTextI18n")
    private fun getLocation() {
//        LocationHelper().startListeningUserLocation(this, object : LocationCallback {
//            override fun onLocationChanged(location: Location?) {
//                LocationService.mLocation?.let {
//                    txvGeolocation.text =
//                        "lat: ${LocationService.mLocation?.latitude}\n" +
//                                "long: ${LocationService.mLocation?.longitude}"
//                }
//            }
//        })
    }
}