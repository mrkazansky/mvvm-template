package com.mrkaz.tokoin.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.data.database.UserDatabase
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.di.configureTestAppWithDatabaseComponent
import io.mockk.*
import io.mockk.coEvery
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(JUnit4::class)
class AuthRepositoryTest : BaseUTTest(), KoinTest {

    private val repository: IAuthRepository by inject()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        super.setUp()
        startKoin { modules(configureTestAppWithDatabaseComponent()) }
    }

    @Test
    fun test_auth_repo_login() = runBlocking<Unit> {
        //Arrange
        val database = mockkClass(UserDatabase::class, relaxed = true)
        val mock = spyk(repository as AuthRepository)
        coEvery { mock getProperty "userDatabase" } propertyType UserDatabase::class returns  {
            database
        }
        coEvery { database.userDAO().login(any(), any()) } returns listOf(
            UserEntity(
                "binh",
                "123",
                null
            )
        )
        //Assign
        val actual = repository.login("binh", "123")
        //Assert
        assertNotNull(actual)
        assertEquals(actual?.username, "binh")
        assertEquals(actual?.password, "123")
    }

    @Test
    fun test_auth_repo_register() = runBlocking<Unit> {
        //Arrange
        coEvery {
            (repository as AuthRepository).userDatabase.userDAO().insert(any())
        } returns Unit
        coEvery {
            (repository as AuthRepository).userDatabase.userDAO().login(any(), any())
        } returns listOf()
        //Assign
        val actual = (repository as AuthRepository).register("binh", "123")
        //Assert
        assertNotNull(actual)
        assertEquals(actual.first, 0)
        assertEquals(actual.second.username, "binh")
        assertNull(actual.second.reference)
    }

    @Test
    fun test_auth_repo_register_error() = runBlocking<Unit> {
        //Arrange
        coEvery {
            (repository as AuthRepository).userDatabase.userDAO().insert(any())
        } returns Unit
        coEvery {
            (repository as AuthRepository).userDatabase.userDAO().login(any(), any())
        } returns listOf(
            UserEntity(
                "binh",
                "123",
                null
            )
        )
        //Assign
        val actual = repository.register("binh", "123")
        //Assert
        assertNotNull(actual)
        assertEquals(actual.first, -1)
        assertEquals(actual.second.username, "binh")
        assertNull(actual.second.reference)
    }

    @Test
    fun test_auth_repo_update() = runBlocking<Unit> {
        //Assign
        repository.update(UserEntity("binh", "123", "bitcoin"))
        //Assert
        coVerify { (repository as AuthRepository).userDatabase.userDAO().update(any()) }
    }

}