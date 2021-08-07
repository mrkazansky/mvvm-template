package com.mrkaz.tokoin.data.repository

import com.mrkaz.tokoin.data.database.entity.UserEntity

interface IAuthRepository {

    suspend fun login(username: String, password: String): UserEntity?

    suspend fun register(username: String, password: String): Pair<Int, UserEntity>

    suspend fun update(user: UserEntity)

}