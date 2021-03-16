package com.mrkaz.tokoin.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.di.configureTestAppComponent
import com.mrkaz.tokoin.usecase.news.NewsUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class NewsUseCaseTest : BaseUTTest() {

    private lateinit var usecase: NewsUseCase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start() {
        super.setUp()
        //Start Koin with required dependencies
        startKoin { modules(configureTestAppComponent(getMockWebServerUrl())) }
    }

    @Test
    fun test_news_use_case_returns_expected_value() = runBlocking {
        //Arrange
        val page = 1
        mockNetworkResponseWithFileContent("success_news_list.json", HttpURLConnection.HTTP_OK)
        usecase = NewsUseCase()
        //Assign
        val actual = usecase.fetchTopHeadlines(page, null, "us")
        val expectedCount = 38
        val expectedListSize = 20
        //Assert
        assertNotNull(actual)
        assertEquals(actual.totalResults, expectedCount)
        assertEquals(actual.articles.size, expectedListSize)
    }

}