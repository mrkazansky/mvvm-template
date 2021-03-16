package com.mrkaz.tokoin.presentation.base

import android.content.Context
import android.content.SharedPreferences
import com.mrkaz.tokoin.common.constant.DS_SHARED_PREF_CCA

class SharedPreferenceHelper(var context: Context) {

    private val mSharedPrefKey = DS_SHARED_PREF_CCA
    private val mSharedPref : SharedPreferences = context.getSharedPreferences(mSharedPrefKey, Context.MODE_PRIVATE)

    private val mSharedPrefEditor : SharedPreferences.Editor = mSharedPref.edit()

    fun setBooleanData(key : String, value: Boolean){
        mSharedPrefEditor.putBoolean(key,value)
        mSharedPrefEditor.commit()
    }

    fun getBooleanData(key : String): Boolean{
        return if(mSharedPref.contains(key))
            mSharedPref.getBoolean(key,false)
        else
            false
    }

    fun setStringData(key : String, value: String){
        mSharedPrefEditor.putString(key, value)
        mSharedPrefEditor.commit()
    }

    fun getStringData(key : String): String {
        return if(mSharedPref.contains(key))
            mSharedPref.getString(key,"")!!
        else
            ""
    }
}