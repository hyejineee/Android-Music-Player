package com.hyejineee.flochallenge

import android.app.Application
import com.hyejineee.flochallenge.util.floModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FloApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FloApplication)
            modules(floModules)
        }
    }
}
