package com.mrkaz.tokoin.di

import com.mrkaz.tokoin.data.database.ReferenceDatabase
import com.mrkaz.tokoin.data.database.UserDatabase
import com.mrkaz.tokoin.data.database.dao.ReferenceDAO
import com.mrkaz.tokoin.data.database.dao.UserDAO
import io.mockk.mockk
import org.koin.dsl.module

val DatabaseDITest = module {

    single {
        mockk<UserDatabase>(relaxed = true)
    }
    single {
        mockk<ReferenceDatabase>(relaxed = true)
    }
    single {
        mockk<UserDAO>(relaxed = true)
    }
    single {
        mockk<ReferenceDAO>(relaxed = true)
    }

}

