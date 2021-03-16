//package com.dev.ccodetest.di
//
//import com.dev.ccodetest.presentation.ui.login.LoginVM
//import com.dev.ccodetest.presentation.ui.main.MainVM
//import com.dev.ccodetest.presentation.ui.news.NewsVM
//import com.dev.ccodetest.presentation.ui.newsReference.NewsRefVM
//import com.dev.ccodetest.presentation.ui.register.RegisterVM
//import io.mockk.mockk
//import kotlinx.coroutines.Dispatchers
//import org.koin.android.viewmodel.dsl.viewModel
//import org.koin.dsl.module
//
//val ViewModelMockInstrument = module {
//
//    viewModel {
//        mockk<NewsVM>(relaxed = true)
//    }
//    viewModel {
//        mockk<NewsRefVM>(relaxed = true)
//    }
//    viewModel {
//        mockk<LoginVM>(relaxed = true)
//    }
//    viewModel {
//        mockk<RegisterVM>(relaxed = true)
//    }
//    viewModel {
//        mockk<MainVM>(relaxed = true)
//    }
//
//}