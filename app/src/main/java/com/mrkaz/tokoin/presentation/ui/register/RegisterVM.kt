package com.mrkaz.tokoin.presentation.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.exception.ConflictException
import com.mrkaz.tokoin.data.exception.InvalidInputException
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.mrkaz.tokoin.usecase.auth.AuthUseCase
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

class RegisterVM(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val useCase: AuthUseCase
) : ViewModel(), KoinComponent {

    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)
    private val ioScope = CoroutineScope(ioDispatcher + job)
    var responseLiveData = MutableLiveData<LiveDataWrapper<UserEntity>>()
    val loadingLiveData = MutableLiveData<Boolean>()

    fun register(username: String, password: String) {
        uiScope.launch {
            if (username.isBlank() || password.isBlank()) {
                responseLiveData.value = LiveDataWrapper.error(InvalidInputException("Empty Input"))
            } else {
                responseLiveData.value = LiveDataWrapper.loading()
                setLoadingVisibility(true)
                try {
                    val data = ioScope.async {
                        return@async useCase.register(username, password)
                    }.await()
                    setLoadingVisibility(false)
                    responseLiveData.value = if (data.first == 0)
                        LiveDataWrapper.success(data.second)
                    else LiveDataWrapper.error(
                        ConflictException(data.second.username)
                    )
                } catch (e: Exception) {
                    setLoadingVisibility(false)
                    responseLiveData.value = LiveDataWrapper.error(e)
                }
            }
        }
    }

    private fun setLoadingVisibility(visible: Boolean) {
        loadingLiveData.postValue(visible)
    }

}