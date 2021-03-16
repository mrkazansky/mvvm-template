package com.mrkaz.tokoin.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrkaz.tokoin.data.database.entity.ReferenceEntity
import com.mrkaz.tokoin.usecase.reference.ReferenceUseCase
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

class MainVM(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val useCase: ReferenceUseCase
) : ViewModel(), KoinComponent {

    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)
    private val ioScope = CoroutineScope(ioDispatcher + job)
    val loadingLiveData = MutableLiveData<Boolean>()

    fun setupData() {
        uiScope.launch {
            setLoadingVisibility(true)
            try {
                val data = ioScope.async {
                    return@async useCase.insert(mockReference().map { ReferenceEntity(it) })
                }.await()
                setLoadingVisibility(false)
            } catch (e: Exception) {
                setLoadingVisibility(false)
            }
        }
    }

    private fun mockReference() = listOf("bitcoin", "apple", "earthquake", "animal")

    private fun setLoadingVisibility(visible: Boolean) {
        loadingLiveData.postValue(visible)
    }

}