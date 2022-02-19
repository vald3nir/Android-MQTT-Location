package com.vald3nir.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.vald3nir.location.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if (!hasPermission()) {
            openSettings()
        }
        showLog()
    }

    private fun showLog() {
        val mqtt = MqttController()
        mqtt.connect()
        mqtt.subscribe() {
            binding?.txvLog?.append("${it.latitude} - ${it.longitude}\n")
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasPermission()) {
            startLocationService()
        }
    }

    private fun startLocationService() {
        ContextCompat.startForegroundService(
            this, Intent(this, LocationService::class.java)
        )
    }

    private fun openSettings() {
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun hasPermission(): Boolean {
        val resultFineLocation = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val resultCoarseLocation = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return resultFineLocation == PackageManager.PERMISSION_GRANTED
                && resultCoarseLocation == PackageManager.PERMISSION_GRANTED
    }
}