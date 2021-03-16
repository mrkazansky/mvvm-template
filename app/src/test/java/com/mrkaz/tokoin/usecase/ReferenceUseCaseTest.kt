package com.mrkaz.tokoin.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.di.configureTestAppWithDatabaseComponent
import com.mrkaz.tokoin.usecase.reference.ReferenceUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin

@RunWith(JUnit4::class)
class ReferenceUseCaseTest : BaseUTTest() {

    private lateinit var usecase: ReferenceUseCase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        super.setUp()
        //Start Koin with required dependencies
        startKoin { modules(configureTestAppWithDatabaseComponent()) }
        usecase = ReferenceUseCase()
    }

    @Test
    fun test_ref_use_case_get_all_returns_expected_value() = runBlocking {
        //Arrange
        coEvery {
            usecase.referenceRepository.getAll()
        } returns listOf("a", "b", "c").map { ReferenceEntity((it)) }
        //Assign
        val actual = usecase.getAll()
        val expectedCount = 3
        //Assert
        assertEquals(expectedCount, actual.size)
        assertNotNull(actual)
    }

    @Test
    fun test_ref_use_case_insert_returns_expected_value() = runBlocking {
        //Assign
        usecase.insert(listOf("binh", "123").map { ReferenceEntity(it) })
        //Assert
        coVerify { usecase.referenceRepository.insert(any()) }
    }

    @Test
    fun test_ref_use_case_update_returns_expected_value() = runBlocking {
        //Assign
        usecase.updateUserReference(UserEntity("binh", "123", null), "bitcoin")
        //Assert
        coVerify { usecase.authUtils.updateUserdata(any()) }
        coVerify { usecase.authRepository.update(any()) }
    }

}