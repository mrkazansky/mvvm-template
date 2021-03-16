package com.mrkaz.tokoin.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.data.exception.NetworkException
import com.mrkaz.tokoin.di.configureTestAppComponent
import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.presentation.ui.news.NewsVM
import com.mrkaz.tokoin.usecase.news.NewsUseCase
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.google.gson.Gson
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin

@RunWith(JUnit4::class)
class NewsVMTest : BaseUTTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel: NewsVM
    private val dispatcher = Dispatchers.Unconfined

    @MockK
    lateinit var newsUseCase: NewsUseCase


    @Before
    fun start() {
        super.setUp()
        //Used for initiation of Mockk
        MockKAnnotations.init(this)
        //Start Koin with required dependencies
        startKoin { modules(configureTestAppComponent(getMockWebServerUrl())) }
    }

    @Test
    fun test_news_view_model_fetch() {
        //Arrange
        viewModel = NewsVM(dispatcher, dispatcher, newsUseCase)
        val sampleResponse = getJson("success_news_list.json")
        val jsonObj = Gson().fromJson(sampleResponse, NewsResponse::class.java)
        coEvery { newsUseCase.fetchTopHeadlines(any(), any(), any()) } returns jsonObj
        //Assign
        val page = 1
        viewModel.fetch(page)
        val expectedCount = 38
        val expectedListSize = 20
        //Assert
        assert(viewModel.newsLiveData.value != null)
        assert(
            viewModel.newsLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.SUCCESS
        )
        assertEquals(viewModel.loadingLiveData.value, false)
        val testResult = viewModel.newsLiveData.value as LiveDataWrapper<NewsResponse>
        assertEquals(testResult.response?.page, page)
        assertEquals(testResult.response?.totalResults, expectedCount)
        assertEquals(testResult.response?.articles?.size, expectedListSize)
    }

    @Test
    fun test_news_view_model_refresh() {
        viewModel = NewsVM(dispatcher, dispatcher, newsUseCase)
        val sampleResponse = getJson("success_news_list.json")
        val jsonObj = Gson().fromJson(sampleResponse, NewsResponse::class.java)
        coEvery { newsUseCase.fetchTopHeadlines(any(), any(), any()) } returns jsonObj
        //Assign
        viewModel.refresh()
        val expectedPage = 1
        val expectedCount = 38
        val expectedListSize = 20
        //Assert
        assert(viewModel.newsLiveData.value != null)
        assert(
            viewModel.newsLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.SUCCESS
        )
        assertEquals(viewModel.loadingLiveData.value, false)
        val testResult = viewModel.newsLiveData.value as LiveDataWrapper<NewsResponse>
        assertEquals(testResult.response?.page, expectedPage)
        assertEquals(testResult.response?.totalResults, expectedCount)
        assertEquals(testResult.response?.articles?.size, expectedListSize)
    }

    @Test
    fun test_news_view_model_load_more() {
        viewModel = NewsVM(dispatcher, dispatcher, newsUseCase)
        val sampleResponse = getJson("success_news_list.json")
        val jsonObj = Gson().fromJson(sampleResponse, NewsResponse::class.java)
        coEvery { newsUseCase.fetchTopHeadlines(any(), any(), any()) } returns jsonObj
        //Assign
        viewModel.fetchMore()
        val expectedCount = 38
        val expectedListSize = 20
        //Assert
        assert(viewModel.newsLiveData.value != null)
        assert(
            viewModel.newsLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.SUCCESS
        )
        assertEquals(viewModel.loadingLiveData.value, false)
        val testResult = viewModel.newsLiveData.value as LiveDataWrapper<NewsResponse>
        assertTrue(testResult.response?.page ?: 1 > 1)
        assertEquals(testResult.response?.totalResults, expectedCount)
        assertEquals(testResult.response?.articles?.size, expectedListSize)
    }

    @Test
    fun test_news_view_model_fetch_error() {
        //Arrange
        viewModel = NewsVM(dispatcher, dispatcher, newsUseCase)
        coEvery {
            newsUseCase.fetchTopHeadlines(
                any(),
                any(),
                any()
            )
        } throws NetworkException("Mock")
        //Assign
        val page = 1
        viewModel.fetch(page)
        //Assert
        assert(viewModel.newsLiveData.value != null)
        assert(
            viewModel.newsLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.ERROR
        )
    }

}