package com.example.newsapi

import android.app.Application
import com.example.newsapi.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            allowOverride(true)
            androidContext(this@NewsApplication)
            modules(appModules)
        }
    }
}