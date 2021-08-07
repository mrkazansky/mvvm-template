package com.mrkaz.tokoin.usecase.auth

import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.repository.IAuthRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthUseCase : KoinComponent {

    val authRepository: IAuthRepository by inject()

    suspend fun login(username: String, password: String): UserEntity? {
        return authRepository.login(username, password)
    }

    suspend fun register(username: String, password: String): Pair<Int, UserEntity> {
        return authRepository.register(username, password)
    }

}