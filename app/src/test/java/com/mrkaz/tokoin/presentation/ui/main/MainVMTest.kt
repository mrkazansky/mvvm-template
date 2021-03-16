package com.mrkaz.tokoin.presentation.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.di.configureTestAppWithDatabaseComponent
import com.mrkaz.tokoin.usecase.reference.ReferenceUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin

@RunWith(JUnit4::class)
class MainVMTest : BaseUTTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel: MainVM
    private val dispatcher = Dispatchers.Unconfined

    @MockK
    lateinit var referenceUseCase: ReferenceUseCase


    @Before
    fun start() {
        super.setUp()
        //Used for initiation of Mockk
        MockKAnnotations.init(this)
        //Start Koin with required dependencies
        startKoin { modules(configureTestAppWithDatabaseComponent()) }
        viewModel = MainVM(dispatcher, dispatcher, referenceUseCase)
    }

    @Test
    fun test_main_view_model_setup() {
        //Assign
        viewModel.setupData()
        //Assert
        coVerify { referenceUseCase.insert(any()) }
        assertEquals(viewModel.loadingLiveData.value, false)
    }

}