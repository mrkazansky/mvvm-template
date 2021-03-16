package com.mrkaz.tokoin.di

import okhttp3.mockwebserver.MockWebServer
import org.koin.dsl.module

val MockWebServerDIPTest = module {

    factory {
        MockWebServer()
    }

}