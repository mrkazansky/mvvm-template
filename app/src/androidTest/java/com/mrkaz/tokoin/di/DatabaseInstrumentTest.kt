package com.mrkaz.tokoin.di

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.mrkaz.tokoin.data.database.ReferenceDatabase
import com.mrkaz.tokoin.data.database.UserDatabase
import org.koin.dsl.module

val DatabaseInstrumentTest = module {

    single {
        Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context, UserDatabase::class.java
        ).build()
    }
    single {
        get<UserDatabase>().userDAO()
    }
    single {
        Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context, ReferenceDatabase::class.java
        ).build()
    }
    single {
        get<ReferenceDatabase>().referenceDAO()
    }

}

