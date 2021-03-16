package com.mrkaz.tokoin.presentation.authenticator

import android.Manifest
import android.accounts.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import com.mrkaz.tokoin.common.utils.AuthUtils
import com.mrkaz.tokoin.presentation.ui.main.MainActivity
import org.koin.core.KoinComponent
import org.koin.core.inject

class Authenticator(private val mContext: Context) : AbstractAccountAuthenticator(mContext),
    KoinComponent {

    override fun editProperties(
        response: AccountAuthenticatorResponse,
        accountType: String
    ): Bundle {
        throw UnsupportedOperationException()
    }

    private val authUtils: AuthUtils by inject()

    @Throws(NetworkErrorException::class)
    override fun addAccount(
        response: AccountAuthenticatorResponse,
        accountType: String,
        authTokenType: String,
        requiredFeatures: Array<String>,
        options: Bundle
    ): Bundle {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.GET_ACCOUNTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Authenticator", "GET_ACCOUNTS not present.")
        }
        if (authUtils.getAccount() != null) {
            val result = Bundle()
            result.putInt(AccountManager.KEY_ERROR_CODE, 400)
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "1 account allowed")
            return result
        }
        val intent = Intent(mContext, MainActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    @Throws(NetworkErrorException::class)
    override fun confirmCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        options: Bundle
    ): Bundle? {
        return null
    }

    @Throws(NetworkErrorException::class)
    override fun getAuthToken(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle
    ): Bundle {
        val am = AccountManager.get(mContext)
        val authToken = am.peekAuthToken(account, authTokenType)
        if (!TextUtils.isEmpty(authToken)) {
            val result = Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
            return result
        }
        val intent = Intent(mContext, MainActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        val retBundle = Bundle()
        retBundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return retBundle
    }

    override fun getAuthTokenLabel(authTokenType: String): String {
        throw UnsupportedOperationException()
    }

    @Throws(NetworkErrorException::class)
    override fun updateCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle
    ): Bundle {
        throw UnsupportedOperationException()
    }

    @Throws(NetworkErrorException::class)
    override fun hasFeatures(
        response: AccountAuthenticatorResponse,
        account: Account,
        features: Array<String>
    ): Bundle {
        throw UnsupportedOperationException()
    }

    @Throws(NetworkErrorException::class)
    override fun getAccountRemovalAllowed(
        response: AccountAuthenticatorResponse,
        account: Account
    ): Bundle {
        val result = Bundle()
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true)
        return result
    }
}