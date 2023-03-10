package com.jetbrains.handson.androidApp

import android.app.Application
import com.jetbrains.handson.kmm.shared.di.initKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
    }
}