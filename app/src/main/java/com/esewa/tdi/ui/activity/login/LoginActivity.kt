package com.esewa.tdi.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.esewa.tdi.ui.activity.main.MainActivity
import com.esewa.tdi.ui.activity.register.RegisterActivity
import com.esewa.tdi.util.SessionUtil
import com.esewa.tdi.util.SharedPrefUtil
import com.esewa.tdi.util.invisible
import com.esewa.tdi.util.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import esewa.tdi.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        private var TAG = LoginActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityLoginBinding
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val sharedPrefUtil: SharedPrefUtil by lazy { SharedPrefUtil(this) }

//    private var dialog: MaterialAlertDialogBuilder


    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnStart.setOnClickListener {
            showDialog()

            var email = binding.etEmailName.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    Log.i("TAG", "Email $email")

                    if (it.isSuccessful) {
                        checkIfAdmin()
                    } else {
                        Log.e("TAG", "login error ${it.exception?.message}")
                        hideDialog()
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                hideDialog()
                Toast.makeText(this, "Empty Fields are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDialog() {
        binding.progressBar.visible()
        binding.btnStart.isEnabled = false
    }

    private fun hideDialog() {
        binding.progressBar.invisible()
        binding.btnStart.isEnabled = true
    }

    private fun checkIfAdmin() {
        val db = FirebaseDatabase.getInstance().reference
        db.child("admins").get().addOnSuccessListener {
            Log.i(TAG, "admins list $it")
            val admins = try {
                it.value as HashMap<String, Boolean>
            } catch (e: Exception) {
                e.printStackTrace()
                hashMapOf()
            }
            val admin = admins.keys.singleOrNull { it == firebaseAuth.currentUser?.uid }
            val name = firebaseAuth.currentUser?.displayName
            val email = firebaseAuth.currentUser?.email
            sharedPrefUtil.writeBooleanData(SharedPrefUtil.IS_ADMIN, admin != null)
            sharedPrefUtil.writeStringData(SharedPrefUtil.NAME, name)
            sharedPrefUtil.writeStringData(SharedPrefUtil.EMAIL, email)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
            hideDialog()
        }.addOnFailureListener {
            Log.e(TAG, "get user list ${it.message}")
            hideDialog()
            SessionUtil(this).signout()
        }
    }
}