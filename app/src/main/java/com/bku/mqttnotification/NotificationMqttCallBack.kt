package com.bku.mqttnotification

import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage

class NotificationMqttCallBack(val context: Context) : MqttCallbackExtended {
    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        message?.let {
            val strMessage = String(it.payload)
            val notificationBuilder = NotificationCompat.Builder(context, "1")
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Message")
                .setContentText(strMessage)
                .setVibrate(arrayListOf(1000L, 1000L, 1000L, 1000L).toLongArray())
            val notification = notificationBuilder.build()

            with(NotificationManagerCompat.from(context)) {
                this.notify(12, notification)
            }
        }
    }

    override fun connectionLost(cause: Throwable?) {
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
    }

}