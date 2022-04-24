package com.esewa.tdi.ui.activity.userdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esewa.tdi.data.User
import com.esewa.tdi.ui.activity.assign.AssignActivity
import com.esewa.tdi.util.SharedPrefUtil
import com.esewa.tdi.util.gone
import com.esewa.tdi.util.visible
import com.google.android.material.button.MaterialButton
import esewa.tdi.R
import esewa.tdi.databinding.ActivityUserDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    var currentDate: String? = null
    private val sharedPrefUtil: SharedPrefUtil by lazy { SharedPrefUtil(this) }
    private lateinit var user: User

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        handleAdminTask()

        val bundle: Bundle? = intent.extras
        val name = bundle!!.getString("name")
        val device = bundle.getString("device")
        val charger = bundle.getString("charger")
        val cable = bundle.getString("cable")

        /* user = User(null, name, device, charger, cable)
         setValues()
         setListeners()*/

        intent.getParcelableExtra<User>("user")?.let { user ->
            this.user = user
            setValues()
            setListeners()
        } ?: run {
            Toast.makeText(this, "Invalid Data", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setValues() {
        val nameView: TextView = findViewById(R.id.recName1)
        val deviceView: TextView = findViewById(R.id.recDevice1)
        val chargerView: TextView = findViewById(R.id.recCharger1)
        val cableView: TextView = findViewById(R.id.recCable1)
//        val tvDateTime: TextView = findViewById(R.id.tvDateTime)

        nameView.text = user.Name
        deviceView.text = user.Device
        chargerView.text = user.Charger
        cableView.text = user.Cable
//        date.text = user.date //TODO format date object into string
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())
//        currentDate = sdf.format(Date(user.date))
        currentDate = sdf.format(Date())
    //       tvDateTime.text = currentDate

    }

    private fun setListeners() {
        val button: MaterialButton = findViewById(R.id.updatebtn)
        button.setOnClickListener {
            val intent = Intent(this, AssignActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleAdminTask() {
        sharedPrefUtil.getBooleanData(SharedPrefUtil.IS_ADMIN)?.let { isAdmin ->
            if (isAdmin) {
                binding.updatebtn.visible()
            } else {
                binding.updatebtn.gone()
            }
        } ?: run {
            binding.updatebtn.gone()
        }
    }

}