package com.vald3nir.mqtt

import com.google.gson.Gson

class DataMessage(
    val clientID: String,
    val latitude: Double,
    val longitude: Double
) {

    private fun toJson(): String {
        return Gson().toJson(this)
    }

    fun toBytes(): ByteArray {
        val charset = Charsets.UTF_8
        return toJson().toByteArray(charset)
    }
}