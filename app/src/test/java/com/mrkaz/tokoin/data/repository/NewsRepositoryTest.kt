package com.mrkaz.tokoin.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.data.repository.impl.NewsRepository
import com.mrkaz.tokoin.di.configureTestAppComponent
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class NewsRepositoryTest : BaseUTTest() {

    private lateinit var mRepo: NewsRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        super.setUp()
        startKoin { modules(configureTestAppComponent(getMockWebServerUrl())) }
        mRepo = NewsRepository()
    }

    @Test
    fun test_news_repo_retrieves_expected_data() = runBlocking<Unit> {
        //Arrange
        val page = 1
        val country = "us"
        mockNetworkResponseWithFileContent(
            "success_news_list.json",
            HttpURLConnection.HTTP_OK
        )
        //Assign
        val actual = mRepo.fetchTopHeadlines(page, null, country)
        val expectedCount = 38
        val expectedListSize = 20
        //Assert
        assertNotNull(actual)
        assertEquals(actual.totalResults, expectedCount)
        assertEquals(actual.articles.size, expectedListSize)
    }

    @Test
    fun test_news_query_repo_retrieves_expected_data() = runBlocking<Unit> {
        //Arrange
        val page = 1
        val query = "bitcoin"
        mockNetworkResponseWithFileContent(
            "success_news_list.json",
            HttpURLConnection.HTTP_OK
        )
        //Assign
        val actual = mRepo.fetchTopHeadlines(page, query, null)
        val expectedCount = 38
        val expectedListSize = 20
        //Assert
        assertNotNull(actual)
        assertEquals(actual.totalResults, expectedCount)
        assertEquals(actual.articles.size, expectedListSize)
    }
}