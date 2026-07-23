package com.astra.assistant

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AstraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
