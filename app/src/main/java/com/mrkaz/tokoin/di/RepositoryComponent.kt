package com.mrkaz.tokoin.di

import com.mrkaz.tokoin.data.repository.AuthRepository
import com.mrkaz.tokoin.data.repository.IAuthRepository
import com.mrkaz.tokoin.data.repository.NewsRepository
import com.mrkaz.tokoin.data.repository.ReferenceRepository
import org.koin.dsl.module

val RepositoryDependency = module {

    single {
        NewsRepository()
    }
    factory<IAuthRepository> { AuthRepository() }

    single {
        ReferenceRepository()
    }

}