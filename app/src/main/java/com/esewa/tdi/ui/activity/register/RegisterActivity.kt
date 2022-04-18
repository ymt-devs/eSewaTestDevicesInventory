package com.esewa.tdi.ui.activity.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esewa.tdi.ui.activity.main.MainActivity
import com.esewa.tdi.util.SessionUtil
import com.esewa.tdi.util.SharedPrefUtil
import com.esewa.tdi.util.invisible
import com.esewa.tdi.util.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import esewa.tdi.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    companion object {
        private var TAG = RegisterActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityRegisterBinding
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val sharedPrefUtil: SharedPrefUtil by lazy { SharedPrefUtil(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            showDialog()
            val etName = binding.etName.editText!!
            val name = etName.text.toString()
            val etEmail = binding.etEmail.editText!!
            val email = etEmail.text.toString()
            val etPassword = binding.etPassword.editText!!
            val password = etPassword.toString()

            if (email.isNotEmpty()) {
                if (email.contains("@esewa.com.np")) {
                    if (password.isNotEmpty()) {
                        //TODO show loading dialog

                        Log.i("TAG", "email $email password $password")
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                            Log.e("TAG", "name ${it.user?.displayName}")
                            it.user?.uid?.let { uid ->
                                addUser(id = uid, name = name, device = "none", charger = "none", cable = "none")
                            }
                        }.addOnFailureListener { exception ->
                            Log.e("TAG", "createUserWithEmailAndPassword exception ${exception.message}")
                            val errorCode = (exception as FirebaseAuthException).errorCode
                            when (errorCode) {
                                "ERROR_INVALID_CUSTOM_TOKEN" -> Toast.makeText(this@RegisterActivity, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show()
                                "ERROR_CUSTOM_TOKEN_MISMATCH" -> Toast.makeText(this@RegisterActivity, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show()
                                "ERROR_INVALID_CREDENTIAL" -> Toast.makeText(this@RegisterActivity, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show()
                                "ERROR_INVALID_EMAIL" -> {
                                    Toast.makeText(this@RegisterActivity, "The email address is badly formatted.", Toast.LENGTH_LONG).show()
                                    etEmail.error = "The email address is badly formatted."
                                    etEmail.requestFocus()
                                }
                                "ERROR_WRONG_PASSWORD" -> {
                                    Toast.makeText(this@RegisterActivity, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show()
                                    etPassword.error = "password is incorrect "
                                    etPassword.requestFocus()
                                    etPassword.setText("")
                                }
                                "ERROR_USER_MISMATCH" -> Toast.makeText(this@RegisterActivity, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show()
                                "ERROR_REQUIRES_RECENT_LOGIN" -> Toast.makeText(this@RegisterActivity, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show()
                                "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> Toast.makeText(this@RegisterActivity, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show()
                                "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                    Toast.makeText(this@RegisterActivity, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show()
                                    etEmail.error = "The email address is already in use by another account."
                                    etEmail.requestFocus()
                                }
                                "ERROR_CREDENTIAL_ALREADY_IN_USE" -> Toast.makeText(this@RegisterActivity, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show()
                                "ERROR_USER_DISABLED" -> Toast.makeText(this@RegisterActivity, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show()
                                "ERROR_USER_TOKEN_EXPIRED" -> Toast.makeText(this@RegisterActivity, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show()
                                "ERROR_USER_NOT_FOUND" -> Toast.makeText(this@RegisterActivity, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show()
                                "ERROR_INVALID_USER_TOKEN" -> Toast.makeText(this@RegisterActivity, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show()
                                "ERROR_OPERATION_NOT_ALLOWED" -> Toast.makeText(this@RegisterActivity, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show()
                                "ERROR_WEAK_PASSWORD" -> {
                                    Toast.makeText(this@RegisterActivity, "The given password is invalid.", Toast.LENGTH_LONG).show()
                                    etPassword.error = "The password is invalid it must 6 characters at least"
                                    etPassword.requestFocus()
                                }
                            }
                        }
                    } else {
                        etPassword.error = "Empty!"
                        etPassword.requestFocus()
                    }
                } else {
                    binding.etEmail.error = "Invalid Email!"
                }
            } else {
                hideDialog()
                etEmail.error = "Empty!"
                etEmail.requestFocus()
            }
        }

        binding.tvLogin.setOnClickListener { finish() }

    }

    private fun addUser(id: String, name: String, device: String, charger: String, cable: String) {
        val user = mapOf(
            "id" to id,
            "Name" to name,
            "Device" to device,
            "Charger" to charger,
            "Cable" to cable
        )
        Log.wtf("id::::::::", "$user")
        val dbref = FirebaseDatabase.getInstance().reference
        dbref.child("Users").child(id).updateChildren(user).addOnSuccessListener {
            Log.i("TAG", "Successfully Added !")
            checkIfAdmin()
        }.addOnFailureListener { exception ->
            Log.e("TAG", "updateChildren exception ${exception.message}")
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
        }.addOnFailureListener {
            Log.e(TAG, "get user list ${it.message}")
            SessionUtil(this).signout()
        }
    }
}