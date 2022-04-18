package com.esewa.tdi.ui.activity.charger

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esewa.tdi.data.Charger
import com.google.android.material.R
import com.google.firebase.database.*
import esewa.tdi.databinding.ActivityChargerBinding

class ChargerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChargerBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var chargerArrayList: ArrayList<Charger>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChargerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        chargerArrayList = arrayListOf<Charger>()
        getDeviceData()
    }

    private fun getDeviceData() {
        dbref = FirebaseDatabase.getInstance().getReference("Charger")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val charger = userSnapshot.getValue(Charger::class.java)
                        chargerArrayList.add(charger!!)

                        Log.wtf("Name", charger.Name)
                    }
                    binding.listCharger.setAdapter(ArrayAdapter(this@ChargerActivity, R.layout.support_simple_spinner_dropdown_item, chargerArrayList))

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Data not Found", Toast.LENGTH_SHORT).show()
            }

        })
    }
}