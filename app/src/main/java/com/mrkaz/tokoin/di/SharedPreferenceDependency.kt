package com.mrkaz.tokoin.di

import com.mrkaz.tokoin.presentation.base.SharedPreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val SharedPreferenceDependency = module {

    single { SharedPreferenceHelper(androidContext()) }
}