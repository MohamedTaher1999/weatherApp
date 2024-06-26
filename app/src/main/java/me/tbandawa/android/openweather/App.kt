package me.tbandawa.android.openweather

import android.app.Application
import openweather.data.di.initKoin
import org.koin.android.ext.koin.androidContext
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        // start koin
        initKoin {
            androidContext(this@App)
        }

        // enable Timber in debug mode
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}