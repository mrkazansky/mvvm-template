package com.mrkaz.tokoin.di

import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.common.utils.Utils
import io.mockk.mockk
import org.koin.dsl.module

val UtilsDITest = module {
    single { mockk<Utils>(relaxed = true) }
    single { mockk<AuthUtils>(relaxed = true) }
}