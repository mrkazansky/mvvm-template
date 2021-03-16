package com.mrkaz.tokoin.data.repository

import com.mrkaz.tokoin.common.utils.Utils
import com.mrkaz.tokoin.data.database.UserDatabase
import com.mrkaz.tokoin.data.database.entity.UserEntity
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthRepository : KoinComponent {

    val userDatabase: UserDatabase by inject()
    private val utils: Utils by inject()

    suspend fun login(username: String, password: String): UserEntity? {
        val encodePassword = utils.md5(password)
        val result = userDatabase.userDAO().login(username, encodePassword)
        return if (result.isEmpty()) null else result[0]
    }

    suspend fun register(username: String, password: String): Pair<Int, UserEntity> {
        val encodePassword = utils.md5(password)
        val isValid = login(username, encodePassword)
        return if (isValid == null) {
            val user = UserEntity(username, encodePassword, null)
            userDatabase.userDAO().insert(user)
            Pair(0, user)
        } else {
            Pair(-1, isValid)
        }
    }

    suspend fun update(user: UserEntity) {
        userDatabase.userDAO().update(user)
    }
}