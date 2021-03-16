package com.mrkaz.tokoin.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mrkaz.tokoin.BaseUTTest
import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.exception.NetworkException
import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.di.configureTestAppComponent
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.mrkaz.tokoin.presentation.ui.newsReference.NewsRefVM
import com.mrkaz.tokoin.usecase.news.NewsUseCase
import com.mrkaz.tokoin.usecase.reference.ReferenceUseCase
import com.google.gson.Gson
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
class NewsRefVMTest : BaseUTTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel: NewsRefVM
    private val dispatcher = Dispatchers.Unconfined

    @MockK
    lateinit var newsUseCase: NewsUseCase

    @MockK
    lateinit var referenceUseCase: ReferenceUseCase

    @MockK
    lateinit var authUtils: AuthUtils

    @Before
    fun start() {
        super.setUp()
        //Used for initiation of Mockk
        MockKAnnotations.init(this)
        //Start Koin with required dependencies
        startKoin { modules(configureTestAppComponent(getMockWebServerUrl())) }
    }

    @Test
    fun test_news_ref_view_model_fetch() {
        //Arrange
        viewModel = NewsRefVM(dispatcher, dispatcher, newsUseCase, referenceUseCase, authUtils)
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
    fun test_news_ref_ref_view_model_refresh() {
        viewModel = NewsRefVM(dispatcher, dispatcher, newsUseCase, referenceUseCase, authUtils)
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
    fun test_news_ref_view_model_load_more() {
        viewModel = NewsRefVM(dispatcher, dispatcher, newsUseCase, referenceUseCase, authUtils)
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
    fun test_news_ref_view_model_fetch_error() {
        //Arrange
        viewModel = NewsRefVM(dispatcher, dispatcher, newsUseCase, referenceUseCase, authUtils)
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
        val expectedCount = 38
        val expectedListSize = 20
        //Assert
        assert(viewModel.newsLiveData.value != null)
        assert(
            viewModel.newsLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.ERROR
        )
    }

    @Test
    fun test_news_ref_view_model_get_all_references() {
        //Arrange
        viewModel = NewsRefVM(dispatcher, dispatcher, newsUseCase, referenceUseCase, authUtils)
        coEvery {
            referenceUseCase.getAll()
        } returns listOf("a", "b", "c", "d").map { ReferenceEntity(it) }
        coEvery {
            authUtils.getUserData()
        } returns UserEntity("binh", "123", "a")
        //Assign
        viewModel.getAllReference()
        val expectedListSize = 4
        //Assert
        assert(viewModel.referencesLiveData.value != null)
        assert(
            viewModel.referencesLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.SUCCESS
        )
        val testResult = viewModel.referencesLiveData.value
        assertNotNull(testResult?.response?.first)
        assertNotNull(testResult?.response?.second)
        assertEquals(testResult?.response?.second?.size, expectedListSize)
    }

    @Test
    fun test_news_ref_view_model_get_all_references_error() {
        //Arrange
        viewModel = NewsRefVM(dispatcher, dispatcher, newsUseCase, referenceUseCase, authUtils)
        coEvery {
            referenceUseCase.getAll()
        } throws NetworkException("Mock")
        coEvery {
            authUtils.getUserData()
        } throws NetworkException("Mock")
        //Assign
        viewModel.getAllReference()
        //Assert
        assert(viewModel.referencesLiveData.value != null)
        assert(
            viewModel.referencesLiveData.value?.responseStatus
                    == LiveDataWrapper.ResponseStatus.ERROR
        )
    }


    @Test
    fun test_news_ref_view_model_update_references() {
        //Arrange
        viewModel = NewsRefVM(dispatcher, dispatcher, newsUseCase, referenceUseCase, authUtils)
        coEvery { referenceUseCase.updateUserReference(any(), any()) } returns Unit
        coEvery {
            authUtils.getUserData()
        } returns UserEntity("binh", "123", "a")
        //Assign
        viewModel.updateReference("binh")
        //Assert
        coVerify { referenceUseCase.updateUserReference(any(), any()) }

    }

}