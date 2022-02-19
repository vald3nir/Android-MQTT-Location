package com.vald3nir.location

import com.google.gson.Gson
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import java.util.*


class MqttController {

    companion object {
        const val BROKEN_ADDRESS = "broker.hivemq.com"
        const val BROKEN_TOPIC = "com/vald3nir/location"
    }

    private val client = Mqtt5Client.builder()
        .identifier(UUID.randomUUID().toString())
        .serverHost(BROKEN_ADDRESS)
        .buildAsync()

    fun connect() {
        client.connect()
    }

    fun publish(payload: DataMessage) {
        client.publishWith()
            .topic(BROKEN_TOPIC)
            .qos(MqttQos.AT_LEAST_ONCE)
            .retain(true)
            .payload(payload.toBytes())
            .send();
    }

    fun subscribe(onResponse: (dataMessage: DataMessage) -> Unit) {
        client.toAsync().subscribeWith()
            .topicFilter(BROKEN_TOPIC)
            .qos(MqttQos.EXACTLY_ONCE)
            .callback(({ publish ->
                run {
                    onResponse.invoke(String(publish.payloadAsBytes).toDataMessage())
                }
            })).send()
    }
}


data class DataMessage(
    val clientID: String? = "Device X",
    val latitude: Double?,
    val longitude: Double?
) {

    private fun toJson(): String {
        return Gson().toJson(this)
    }

    fun toBytes(): ByteArray {
        val charset = Charsets.UTF_8
        return toJson().toByteArray(charset)
    }
}

fun String.toDataMessage(): DataMessage {
    return Gson().fromJson(this, DataMessage::class.java)
}