package com.harry.edwin.softcom.form

import android.app.Application
import com.harry.edwin.softcom.di.appModule
import org.koin.android.ext.android.startKoin

class SoftcomApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}