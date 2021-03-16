package com.mrkaz.tokoin.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.di.configureTestAppWithDatabaseComponent
import com.mrkaz.tokoin.usecase.auth.AuthUseCase
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin

@RunWith(JUnit4::class)
class AuthUseCaseTest : BaseUTTest() {

    private lateinit var usecase: AuthUseCase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        super.setUp()
        //Start Koin with required dependencies
        startKoin { modules(configureTestAppWithDatabaseComponent()) }
        usecase = AuthUseCase()
    }

    @Test
    fun test_auth_use_case_login_returns_expected_value() = runBlocking {
        //Arrange
        coEvery {
            usecase.authRepository.login(any(), any())
        } returns UserEntity("binh", "123", null)

        //Assign
        val actual = usecase.login("binh", "123")
        //Assert
        assertEquals("binh", actual?.username)
        assertEquals("123", actual?.password)
        assertNull(actual?.reference)
    }

    @Test
    fun test_auth_use_case_login_returns_error() = runBlocking {
        //Arrange
        coEvery {
            usecase.authRepository.login(any(), any())
        } returns null
        //Assign
        val actual = usecase.login("binh", "123")
        //Assert
        assertNull(actual)
    }

    @Test
    fun test_auth_use_case_register_returns_error() = runBlocking {
        //Arrange
        coEvery { usecase.authRepository.register(any(), any()) } returns Pair(
            0,
            UserEntity("binh", "123", null)
        )
        //Assign
        val actual = usecase.register("binh", "123")
        //Assert
        assertNotNull(actual)
        assertEquals(actual.first, 0)
        assertEquals(actual.second.username, "binh")
        assertNull(actual.second.reference)
    }

    @Test
    fun test_auth_use_case_register_returns_expected_value() = runBlocking {
        //Arrange
        coEvery { usecase.authRepository.register(any(), any()) } returns Pair(
            -1,
            UserEntity("binh", "123", null)
        )
        //Assign
        val actual = usecase.register("binh", "123")
        //Assert
        assertNotNull(actual)
        assertEquals(actual.first, -1)
        assertEquals(actual.second.username, "binh")
        assertNull(actual.second.reference)
    }

}