package com.esewa.tdi.util

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPrefUtil(
    val context: Context
) {

    companion object {
        const val IS_ADMIN = "IS_ADMIN"
        const val EMAIL = "EMAIL"
        const val NAME = "NAME"
    }


    fun writeStringData(key: String, data: String?) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, data).apply()
    }

    fun getStringData(key: String): String? {
        return try {
            PreferenceManager.getDefaultSharedPreferences(context).getString(key, null)
        } catch (e: ClassCastException) {
            e.printStackTrace()
            null
        }
    }

    fun writeBooleanData(key: String, data: Boolean?) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, data ?: false).apply()
    }

    fun getBooleanData(key: String): Boolean? {
        return try {
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false)
        } catch (e: ClassCastException) {
            e.printStackTrace()
            null
        }
    }
}