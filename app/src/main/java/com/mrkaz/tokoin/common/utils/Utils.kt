package com.mrkaz.tokoin.common.utils

import android.content.Context
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Common Utilities for App
 */
class Utils(val context: Context) {

    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}