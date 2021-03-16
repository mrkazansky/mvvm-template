package com.mrkaz.tokoin.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.exception.NetworkException
import com.mrkaz.tokoin.di.configureTestAppWithDatabaseComponent
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.mrkaz.tokoin.presentation.ui.register.RegisterVM
import com.mrkaz.tokoin.usecase.auth.AuthUseCase
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
class RegisterVMTest : BaseUTTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel: RegisterVM
    private val dispatcher = Dispatchers.Unconfined

    @MockK
    lateinit var authUseCase: AuthUseCase


    @Before
    fun start() {
        super.setUp()
        //Used for initiation of Mockk
        MockKAnnotations.init(this)
        //Start Koin with required dependencies
        startKoin { modules(configureTestAppWithDatabaseComponent()) }
        viewModel = RegisterVM(dispatcher, dispatcher, authUseCase)
    }

    @Test
    fun test_register_view_model_register() {
        //Arrange
        coEvery { authUseCase.register(any(), any()) } returns Pair(0, UserEntity("binh", "123", null))
        //Assign
        viewModel.register("binh","123")
        //Assert
        assert(viewModel.responseLiveData.value != null)
        assert(
            viewModel.responseLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.SUCCESS
        )
        assertEquals(viewModel.loadingLiveData.value, false)
        val testResult = viewModel.responseLiveData.value
        assertNotNull(testResult?.response)
        assertEquals(testResult?.response?.username, "binh")
        assertNull(testResult?.response?.reference)
    }

    @Test
    fun test_register_view_model_register_duplicated() {
        //Arrange
        coEvery { authUseCase.register(any(), any()) } returns Pair(-1, UserEntity("binh", "123", null))
        //Assign
        viewModel.register("binh","123")
        //Assert
        assert(viewModel.responseLiveData.value != null)
        assert(
            viewModel.responseLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.ERROR
        )
        assertEquals(viewModel.loadingLiveData.value, false)
    }

    @Test
    fun test_register_view_model_register_empty() {
        //Assign
        viewModel.register("binh","123")
        //Assert
        assert(viewModel.responseLiveData.value != null)
        assert(
            viewModel.responseLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.ERROR
        )
        assertEquals(viewModel.loadingLiveData.value, false)
    }

    @Test
    fun test_register_view_model_register_error() {
        //Arrange
        coEvery { authUseCase.login(any(), any()) } throws NetworkException("Mock")
        //Assign
        viewModel.register("binh","123")
        //Assert
        assert(viewModel.responseLiveData.value != null)
        assert(
            viewModel.responseLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.ERROR
        )
        assertEquals(viewModel.loadingLiveData.value, false)
    }

}