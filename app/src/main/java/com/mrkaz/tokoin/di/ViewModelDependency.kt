package com.mrkaz.tokoin.di

import com.mrkaz.tokoin.presentation.ui.login.LoginVM
import com.mrkaz.tokoin.presentation.ui.main.MainVM
import com.mrkaz.tokoin.presentation.ui.news.NewsVM
import com.mrkaz.tokoin.presentation.ui.newsReference.NewsRefVM
import com.mrkaz.tokoin.presentation.ui.register.RegisterVM
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelDependency = module {

    viewModel {
        NewsVM(Dispatchers.Main, Dispatchers.IO, get())
    }
    viewModel {
        NewsRefVM(Dispatchers.Main, Dispatchers.IO, get(), get(), get())
    }
    viewModel {
        LoginVM(Dispatchers.Main, Dispatchers.IO, get())
    }
    viewModel {
        RegisterVM(Dispatchers.Main, Dispatchers.IO, get())
    }
    viewModel {
        MainVM(Dispatchers.Main, Dispatchers.IO, get())
    }

}