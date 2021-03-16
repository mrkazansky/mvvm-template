package com.mrkaz.tokoin.di

import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.common.utils.Utils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val UtilsDependency = module {
    single { Utils(androidContext()) }
    single { AuthUtils(androidContext()) }
}