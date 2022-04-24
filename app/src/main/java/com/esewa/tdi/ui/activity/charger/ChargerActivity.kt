package com.esewa.tdi.ui.activity.charger

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esewa.tdi.data.Charger
import com.esewa.tdi.data.Device
import com.esewa.tdi.data.User
import com.google.android.material.R
import com.google.firebase.database.*
import esewa.tdi.databinding.ActivityChargerBinding

class ChargerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChargerBinding
    private lateinit var dbref: DatabaseReference
    private var chargerArrayList = mutableListOf<Charger>()
    private var userArrayList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChargerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getDeviceData()
    }

    private fun getDeviceData() {
        dbref = FirebaseDatabase.getInstance().getReference("Charger")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    chargerArrayList = mutableListOf()
                    for (userSnapshot in snapshot.children) {
                        val mCharger = userSnapshot.getValue(Charger::class.java)
                        mCharger?.let { charger ->
                            chargerArrayList.add(charger)
                            Log.wtf("Name", charger.Name)
                        } ?: run {
                            Log.wtf("Name", "charger is null")
                        }
                    }
                    getUserList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Data not Found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUserList() {
        dbref = FirebaseDatabase.getInstance().getReference("Users")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userArrayList = mutableListOf()
                    for (userSnapshot in snapshot.children) {
                        userSnapshot.getValue(User::class.java)?.let { user ->
                            userArrayList.add(user)
                            Log.wtf("User", user.Name)
                        } ?: run {
                            Log.wtf("User", "user is null")
                        }
                    }
                    setList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Data not Found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setList() {

        Log.e("TAG", "-------------------------------------------")
        chargerArrayList.map { Log.e("TAG", "old ${it.id} ${it.Name}") }
        userArrayList.map { Log.e("TAG", "use ${it.id} ${it.Charger}") }

        Log.e("TAG", "-------------------------------------------")

        val newChargerList = mutableListOf<Charger>().apply {
            addAll(chargerArrayList)
        }
        newChargerList.map { Log.e("TAG", "copy ${it.id} ${it.Name}") }

        Log.e("TAG", "-------------------------------------------")

        for (charger in chargerArrayList) {
            for (user in userArrayList) {
                if (charger.Name == user.Charger) {
                    newChargerList.remove(charger)
                }
            }
        }

        newChargerList.map { Log.e("TAG", "final ${it.id} ${it.Name}") }

        Log.e("TAG", "-------------------------------------------")

        binding.listCharger.adapter = ArrayAdapter(this@ChargerActivity, android.R.layout.simple_list_item_1, newChargerList)


    }
}