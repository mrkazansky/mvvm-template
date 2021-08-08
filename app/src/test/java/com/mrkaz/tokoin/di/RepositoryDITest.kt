package com.mrkaz.tokoin.di

import com.mrkaz.tokoin.data.repository.IAuthRepository
import com.mrkaz.tokoin.data.repository.INewsRepository
import com.mrkaz.tokoin.data.repository.IReferenceRepository
import com.mrkaz.tokoin.data.repository.impl.AuthRepository
import com.mrkaz.tokoin.data.repository.impl.NewsRepository
import com.mrkaz.tokoin.data.repository.impl.ReferenceRepository
import io.mockk.mockk
import org.koin.dsl.module

val RepositoryDITest = module {

    single {
        mockk<INewsRepository>(relaxed = true)
    }
    single {
        mockk<IAuthRepository>(relaxed = true)
    }
    single {
        mockk<IReferenceRepository>(relaxed = true)
    }

}