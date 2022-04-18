package com.esewa.tdi.ui.activity.cable

import android.R
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esewa.tdi.data.Cable
import com.google.firebase.database.*
import esewa.tdi.databinding.ActivityCableBinding

class CableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCableBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var cableArrayList: ArrayList<Cable>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        cableArrayList = arrayListOf<Cable>()
        getCableData()
    }

    private fun getCableData() {
        dbref = FirebaseDatabase.getInstance().getReference("Cables")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val cable = userSnapshot.getValue(Cable::class.java)
                        cableArrayList.add(cable!!)

                        Log.wtf("Name", cable.Name)
                    }
                }
                binding.listCable.setAdapter(ArrayAdapter(this@CableActivity, R.layout.simple_list_item_1, cableArrayList))
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Data not Found", Toast.LENGTH_SHORT).show()
            }

        })
    }
}