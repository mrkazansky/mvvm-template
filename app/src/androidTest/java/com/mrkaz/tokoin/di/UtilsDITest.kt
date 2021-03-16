package com.mrkaz.tokoin.di

import androidx.test.platform.app.InstrumentationRegistry
import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.common.utils.Utils
import org.koin.dsl.module

val UtilsInstrumentTest = module {
    single { Utils(InstrumentationRegistry.getInstrumentation().context) }
    single { AuthUtils(InstrumentationRegistry.getInstrumentation().context) }
}