package com.android.location.manager.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vald3nir.R
import com.vald3nir.databinding.ActivityMapsBinding
import com.vald3nir.mqtt.DataMessage
import com.vald3nir.mqtt.MqttController


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MqttController.MQTTCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mqttController = MqttController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mqttController.connect()
        mqttController.subscribe("vald3nir", this)
    }

    override fun onResponseMQTT(dataMessage: DataMessage) {
        runOnUiThread {
            val location = LatLng(dataMessage.latitude, dataMessage.longitude)
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(location).title(dataMessage.clientID))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
        }
    }
}
