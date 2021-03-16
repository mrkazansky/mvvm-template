package com.mrkaz.tokoin.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import com.mrkaz.tokoin.di.configureTestAppComponent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin

@RunWith(JUnit4::class)
class ReferenceRepositoryTest : BaseUTTest() {

    private lateinit var repository: ReferenceRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        super.setUp()
        startKoin { modules(configureTestAppComponent(getMockWebServerUrl())) }
        repository = mockk(relaxed = true)
    }

    @Test
    fun test_reference_repo_get_all() = runBlocking<Unit> {
        //Arrange
        coEvery { repository.getAll() } returns listOf(
            "a",
            "b",
            "c",
            "d"
        ).map { ReferenceEntity(it) }
        //Assign
        val actual = repository.getAll()
        val expectedCount = 4
        //Assert
        assertNotNull(actual)
        assertEquals(actual.size, expectedCount)
    }

    @Test
    fun test_reference_repo_insert() = runBlocking<Unit> {
        //Arrange
        coEvery { repository.insert(any()) } coAnswers {
            repository.referenceDatabase.referenceDAO().insert(listOf())
        }
        //Assign
        repository.insert(listOf())
        //Assert
        coVerify { repository.referenceDatabase.referenceDAO().insert(any()) }
    }

}