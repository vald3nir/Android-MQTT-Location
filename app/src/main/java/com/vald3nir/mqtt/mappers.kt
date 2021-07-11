package com.vald3nir.mqtt

import com.google.gson.Gson

fun String.toDataMessage(): DataMessage {
    return Gson().fromJson(this, DataMessage::class.java)
}