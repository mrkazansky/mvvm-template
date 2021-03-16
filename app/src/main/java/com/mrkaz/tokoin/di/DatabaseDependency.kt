package com.mrkaz.tokoin.di

import androidx.room.Room
import com.mrkaz.tokoin.data.database.ReferenceDatabase
import com.mrkaz.tokoin.data.database.UserDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val DatabaseDependency = module {

    single {
        Room.databaseBuilder(androidApplication(), UserDatabase::class.java, "users")
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        get<UserDatabase>().userDAO()
    }
    single {
        Room.databaseBuilder(androidApplication(), ReferenceDatabase::class.java, "references")
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        get<ReferenceDatabase>().referenceDAO()
    }

}

