package com.astra.assistant

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AstraApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ASSISTANT_CHANNEL_ID,
                "Astra Assistant",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Фоновая служба голосового ассистента"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val ASSISTANT_CHANNEL_ID = "astra_assistant_channel"
    }
}
