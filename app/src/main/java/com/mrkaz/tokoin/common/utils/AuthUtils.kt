package com.mrkaz.tokoin.common.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.mrkaz.tokoin.data.database.entity.UserEntity
import com.google.gson.Gson

class AuthUtils(val context: Context) {

    val loggingStatus = MutableLiveData(peekToken()?.isNotBlank())

    fun logged(loginData: UserEntity) {
        val account = Account(loginData.username, "normal")
        val am = AccountManager.get(context)
        val bundle = Bundle()
        bundle.putString("user", Gson().toJson(loginData))
        am.addAccountExplicitly(account, null, bundle)
        am.setAuthToken(account, "full_access", loginData.username)
        loggingStatus.value = true
    }

    fun getAccount(): Account? {
        val accountManager = AccountManager.get(context)
        val accounts = accountManager.getAccountsByType("normal")
        return try {
            accounts[0]
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    fun getUserData(): UserEntity? {
        val account = getAccount()
        account?.let {
            return Gson().fromJson(
                AccountManager
                    .get(context)
                    .getUserData(it, "user"), UserEntity::class.java
            )

        }
        return null
    }

    fun updateUserdata(userData: UserEntity?) {
        val account = getAccount()
        if (account != null) {
            AccountManager
                .get(context)
                .setUserData(
                    account,
                    "user",
                    Gson().toJson(userData)
                )
        }
    }

    fun peekToken(): String? {
        val account = getAccount()
        val accountManager = AccountManager.get(context)
        return if (account == null) null else accountManager.peekAuthToken(account, "full_access")
    }

    fun logout() {
        val accountManager = AccountManager.get(context)
        val accounts = accountManager.getAccountsByType("normal")
        for (account in accounts) {
            accountManager.removeAccount(account, null, null)
        }
        loggingStatus.value = false
    }
}