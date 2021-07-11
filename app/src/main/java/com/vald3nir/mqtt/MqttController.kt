package com.vald3nir.mqtt

import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import java.util.*


class MqttController {

    private val BROKEN_ADDRESS = "broker.hivemq.com"

    private val client = Mqtt5Client.builder()
        .identifier(UUID.randomUUID().toString())
        .serverHost(BROKEN_ADDRESS)
        .buildAsync()

    fun connect() {
        client.connect()
    }

    fun publish(topic: String, payload: DataMessage) {
        client.publishWith()
            .topic(topic)
            .qos(MqttQos.AT_LEAST_ONCE)
            .payload(payload.toBytes())
            .send();
    }

    fun subscribe(topic: String, callback: MQTTCallback) {
        client.toAsync().subscribeWith()
            .topicFilter(topic)
            .qos(MqttQos.EXACTLY_ONCE)
            .callback(({ publish ->
                run {
                    callback.onResponseMQTT(String(publish.payloadAsBytes).toDataMessage())
                }
            }))
            .send();
    }

    interface MQTTCallback {
        fun onResponseMQTT(dataMessage: DataMessage)
    }

}