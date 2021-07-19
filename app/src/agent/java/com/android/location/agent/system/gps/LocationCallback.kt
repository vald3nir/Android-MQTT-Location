package com.android.location.agent.system.gps

import android.location.Location

interface LocationCallback {
    fun onLocationChanged(location: Location?)
}