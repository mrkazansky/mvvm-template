package com.mrkaz.tokoin.presentation.ui.newsReference

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.models.news.NewsResponse
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.mrkaz.tokoin.usecase.news.NewsUseCase
import com.mrkaz.tokoin.usecase.reference.ReferenceUseCase
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

class NewsRefVM(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val newsUseCase: NewsUseCase,
    private val referenceUseCase: ReferenceUseCase,
    private val authUtils: AuthUtils
) : ViewModel(), KoinComponent {
    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)
    private val ioScope = CoroutineScope(ioDispatcher + job)
    var newsLiveData = MutableLiveData<LiveDataWrapper<NewsResponse>>()
    var referencesLiveData =
        MutableLiveData<LiveDataWrapper<Pair<UserEntity?, List<ReferenceEntity>>>>()
    val loadingLiveData = MutableLiveData<Boolean>()

    val reference = MutableLiveData<String>()
    private var paging = 1

    fun fetch(page: Int, query: String? = null) {
        uiScope.launch {
            newsLiveData.value = LiveDataWrapper.loading()
            setLoadingVisibility(true)
            try {
                val data = ioScope.async {
                    return@async newsUseCase.fetchTopHeadlines(page, query)
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

    fun getAllReference() {
        uiScope.launch {
            referencesLiveData.value = LiveDataWrapper.loading()
            try {
                val references = ioScope.async {
                    return@async referenceUseCase.getAll()
                }.await()
                val userEntity = authUtils.getUserData()
                referencesLiveData.value = LiveDataWrapper.success(Pair(userEntity, references))
            } catch (e: Exception) {
                referencesLiveData.value = LiveDataWrapper.error(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        this.job.cancel()
    }

    fun refresh() {
        paging = 1
        fetch(paging, reference.value)
    }

    fun fetchMore() {
        paging++
        fetch(paging, reference.value)
    }

    fun updateReference(reference: String) {
        uiScope.launch {
            try {
                ioScope.async {
                    authUtils.getUserData()?.let {
                        return@async referenceUseCase.updateUserReference(it, reference)
                    }
                }.await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}