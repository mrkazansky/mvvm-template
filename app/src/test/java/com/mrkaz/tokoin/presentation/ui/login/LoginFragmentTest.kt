package com.mrkaz.tokoin.presentation.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.exception.NetworkException
import com.mrkaz.tokoin.di.configureTestAppWithDatabaseComponent
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
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
class LoginFragmentTest : BaseUTTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel: LoginVM
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
        viewModel = LoginVM(dispatcher, dispatcher, authUseCase)
    }

    @Test
    fun test_login_view_model_login() {
        //Arrange
        coEvery { authUseCase.login(any(), any()) } returns UserEntity("binh", "123", null)
        //Assign
        viewModel.login("binh","123")
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
    fun test_login_view_model_login_empty() {
        //Assign
        viewModel.login("binh","123")
        //Assert
        assert(viewModel.responseLiveData.value != null)
        assert(
            viewModel.responseLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.ERROR
        )
        assertEquals(viewModel.loadingLiveData.value, false)
    }

    @Test
    fun test_login_view_model_login_error() {
        //Arrange
        coEvery { authUseCase.login(any(), any()) } throws NetworkException("Mock")
        //Assign
        viewModel.login("binh","123")
        //Assert
        assert(viewModel.responseLiveData.value != null)
        assert(
            viewModel.responseLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.ERROR
        )
        assertEquals(viewModel.loadingLiveData.value, false)
    }

}