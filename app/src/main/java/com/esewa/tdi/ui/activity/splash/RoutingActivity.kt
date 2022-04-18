package com.esewa.tdi.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.esewa.tdi.ui.activity.login.LoginActivity
import com.esewa.tdi.ui.activity.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class RoutingActivity : AppCompatActivity() {

    private lateinit var splashScreen: SplashScreen
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        private var TAG = RoutingActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }
        initFirebase()
    }

    private fun initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance()
        Log.i(TAG, " ${firebaseAuth.currentUser}")

        firebaseAuth.currentUser?.let { user ->
            startActivity(Intent(this, MainActivity::class.java))
        } ?: run {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}