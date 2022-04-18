package com.esewa.tdi.ui.activity.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esewa.tdi.util.SharedPrefUtil
import esewa.tdi.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val sharedPrefUtil: SharedPrefUtil by lazy { SharedPrefUtil(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUserData()

    }

    private fun setUserData() {
        binding.userName.text = sharedPrefUtil.getStringData(SharedPrefUtil.NAME) ?: "User"
        binding.userEmail.text = sharedPrefUtil.getStringData(SharedPrefUtil.EMAIL) ?: "-"
    }
}