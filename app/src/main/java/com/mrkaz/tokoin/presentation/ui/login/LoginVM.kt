package com.mrkaz.tokoin.presentation.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.exception.InvalidInputException
import com.mrkaz.tokoin.presentation.base.LiveDataWrapper
import com.mrkaz.tokoin.usecase.auth.AuthUseCase
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

class LoginVM(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val useCase: AuthUseCase
) : ViewModel(), KoinComponent {

    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(mainDispatcher + job)
    private val ioScope = CoroutineScope(ioDispatcher + job)
    var responseLiveData = MutableLiveData<LiveDataWrapper<UserEntity>>()
    val loadingLiveData = MutableLiveData<Boolean>()

    fun login(username: String, password: String) {
        uiScope.launch {
            if (username.isBlank() || password.isBlank()) {
                responseLiveData.value = LiveDataWrapper.error(InvalidInputException("Empty Input"))
            } else {
                responseLiveData.value = LiveDataWrapper.loading()
                setLoadingVisibility(true)
                try {
                    val data = ioScope.async {
                        return@async useCase.login(username, password)
                    }.await() //
                    responseLiveData.value = if (data != null)
                        LiveDataWrapper.success(data)
                    else LiveDataWrapper.error(Exception("Login failed"))
                } catch (e: Exception) {
                    responseLiveData.value = LiveDataWrapper.error(e)
                } finally {
                    setLoadingVisibility(false)
                }
            }
        }
    }

    private fun setLoadingVisibility(visible: Boolean) {
        loadingLiveData.postValue(visible)
    }

}