package com.mrkaz.tokoin.di

import com.mrkaz.tokoin.data.repository.AuthRepository
import com.mrkaz.tokoin.data.repository.NewsRepository
import com.mrkaz.tokoin.data.repository.ReferenceRepository
import io.mockk.mockk
import org.koin.dsl.module

val RepositoryDITest = module {

    single {
        mockk<NewsRepository>(relaxed = true)
    }
    single {
        mockk<AuthRepository>(relaxed = true)
    }
    single {
        mockk<ReferenceRepository>(relaxed = true)
    }

}