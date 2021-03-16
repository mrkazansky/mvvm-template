package com.mrkaz.tokoin

import androidx.multidex.MultiDexApplication
import com.mrkaz.tokoin.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Main Application class.
 * Dependency Injection initiated for all sub modules.
 */
open class MainApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initiateKOIN()
    }

    private fun initiateKOIN() {
        startKoin{
            androidContext(this@MainApp)
            modules(provideDependency())
        }
    }

    open fun provideDependency() = appComponent
}