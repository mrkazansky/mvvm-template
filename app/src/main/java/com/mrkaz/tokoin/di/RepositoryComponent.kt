package com.mrkaz.tokoin.di

import com.mrkaz.tokoin.data.repository.IAuthRepository
import com.mrkaz.tokoin.data.repository.INewsRepository
import com.mrkaz.tokoin.data.repository.IReferenceRepository
import com.mrkaz.tokoin.data.repository.impl.AuthRepository
import com.mrkaz.tokoin.data.repository.impl.NewsRepository
import com.mrkaz.tokoin.data.repository.impl.ReferenceRepository
import org.koin.dsl.module

val RepositoryDependency = module {

    factory<IAuthRepository> { AuthRepository() }

    factory<INewsRepository> { NewsRepository() }

    factory<IReferenceRepository> { ReferenceRepository() }

}