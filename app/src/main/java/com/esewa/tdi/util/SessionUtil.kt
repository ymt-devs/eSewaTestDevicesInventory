package com.esewa.tdi.util

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class SessionUtil(
    val context: Context
) {

    fun signout() {
        FirebaseAuth.getInstance().signOut()
        val sharedPrefUtil = SharedPrefUtil(context)
        sharedPrefUtil.writeBooleanData(SharedPrefUtil.IS_ADMIN, null)
        sharedPrefUtil.writeStringData(SharedPrefUtil.EMAIL, null)
        sharedPrefUtil.writeStringData(SharedPrefUtil.NAME, null)
    }
}