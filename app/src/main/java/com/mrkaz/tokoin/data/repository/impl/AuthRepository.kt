package com.mrkaz.tokoin.data.repository.impl

import com.mrkaz.tokoin.common.utils.Utils
import com.mrkaz.tokoin.data.database.UserDatabase
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.mrkaz.tokoin.data.repository.IAuthRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthRepository : IAuthRepository, KoinComponent {

    val userDatabase: UserDatabase by inject()
    private val utils: Utils by inject()

    override suspend fun login(username: String, password: String): UserEntity? {
        val encodePassword = utils.md5(password)
        val result = userDatabase.userDAO().login(username, encodePassword)
        return if (result.isEmpty()) null else result[0]
    }

    override suspend fun register(username: String, password: String): Pair<Int, UserEntity> {
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

    override suspend fun update(user: UserEntity) {
        userDatabase.userDAO().update(user)
    }

}