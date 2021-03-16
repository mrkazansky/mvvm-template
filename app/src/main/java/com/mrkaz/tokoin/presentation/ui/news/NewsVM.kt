package com.mrkaz.tokoin.presentation.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.mrkaz.tokoin.usecase.news.NewsUseCase
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

class NewsVM(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val useCase: NewsUseCase
) : ViewModel(), KoinComponent {

    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)
    private val ioScope = CoroutineScope(ioDispatcher + job)
    var newsLiveData = MutableLiveData<LiveDataWrapper<NewsResponse>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    private var paging = 1

    fun fetch(page: Int) {
        uiScope.launch {
            newsLiveData.value = LiveDataWrapper.loading()
            setLoadingVisibility(true)
            try {
                val data = ioScope.async {
                    return@async useCase.fetchTopHeadlines(page, country = "us")
                }.await()
                data.page = page
                newsLiveData.value = LiveDataWrapper.success(data)
            } catch (e: Exception) {
                newsLiveData.value = LiveDataWrapper.error(e)
            } finally {
                setLoadingVisibility(false)
            }
        }
    }

    private fun setLoadingVisibility(visible: Boolean) {
        loadingLiveData.postValue(visible)
    }

    override fun onCleared() {
        super.onCleared()
        this.job.cancel()
    }

    fun refresh() {
        paging = 1
        fetch(paging)
    }

    fun fetchMore() {
        paging++
        fetch(paging)
    }
}