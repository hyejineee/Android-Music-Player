package com.hyejineee.musicApp

import android.app.Application
import com.hyejineee.musicApp.util.floModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MusicAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MusicAppApplication)
            modules(floModules)
        }
    }
}
