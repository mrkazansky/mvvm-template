package com.mrkaz.tokoin.di

import com.mrkaz.tokoin.usecase.auth.AuthUseCase
import com.mrkaz.tokoin.usecase.news.NewsUseCase
import com.mrkaz.tokoin.usecase.reference.ReferenceUseCase
import org.koin.dsl.module

val UseCaseDependency = module {

    single {
        NewsUseCase()
    }
    single {
        AuthUseCase()
    }
    single {
        ReferenceUseCase()
    }
}