package com.bku.mqttnotification

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, MqttClientService::class.java).apply {
            action = MqttClientService.MQTT_CONNECT
        }
        startService(intent)
    }
}
