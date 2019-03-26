package com.bku.mqttnotification

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttConnectOptions

class MqttClientService : Service() {

    private lateinit var mqttClient: MqttAndroidClient
    lateinit var mqttClientId: String

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val MQTT_CONNECT = "connect"
        const val MQTT_DISCONNECT = "disconnect"
        const val MQTT_SERVER_URL = "tcp://broker.hivemq.com:1883"
        const val TOPIC = "notification"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            stopSelf()
            return Service.START_NOT_STICKY
        }
        this.mqttClientId = "clientId-I2nFXSOQuS"
        if (intent.action == MQTT_CONNECT) {
            connectToServer()
        } else if (intent.action == MQTT_DISCONNECT) {
            disconnectFromServer()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun disconnectFromServer() {
        this.mqttClient.disconnect(this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "Disconnect successfully!")
                mqttClient.unsubscribe(TOPIC)
                mqttClient.unregisterResources()
                mqttClient.close()
                stopSelf()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("MQTT", "Disconnect failure!")
            }
        })

    }

    private fun connectToServer() {
        mqttClient = MqttAndroidClient(this, MQTT_SERVER_URL, this.mqttClientId)
        val options = MqttConnectOptions().apply {
            connectionTimeout = 30
            isAutomaticReconnect = true
            isCleanSession = true
            keepAliveInterval = 120
        }
        mqttClient.setCallback(NotificationMqttCallBack(this))
        mqttClient.connect(options, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "Connect successfully!")
                subscribeToNotificationTopic()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("MQTT", "Connect failure! $exception")
//                disconnectFromServer()
            }
        })
    }

    private fun subscribeToNotificationTopic() {
        mqttClient.subscribe(TOPIC, 0, this, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "Subscribe to topic notification successfully!")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d("MQTT", "Subscribe to topic notification failure!")
            }
        })
    }
}