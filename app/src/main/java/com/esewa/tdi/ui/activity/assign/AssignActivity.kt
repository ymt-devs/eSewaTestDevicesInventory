package com.esewa.tdi.ui.activity.assign

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esewa.tdi.data.Cable
import com.esewa.tdi.data.Charger
import com.esewa.tdi.data.Device
import com.esewa.tdi.data.User
import com.google.firebase.database.*
import esewa.tdi.databinding.ActivityAssignBinding

class AssignActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAssignBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var userArrayList: MutableList<User>
    private lateinit var chargerArrayList: ArrayList<Charger>
    private lateinit var cableArrayList: ArrayList<Cable>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        chargerArrayList = arrayListOf<Charger>()
        cableArrayList = arrayListOf<Cable>()

        getUserData()
        getDeviceData()
        getChargerData()
        getCableData()

        binding.saveToDatabase.setOnClickListener {

            val device = (binding.tilDevices.editText as AutoCompleteTextView).text.toString()
            val charger = binding.autoCompleteTextViewCharger.text.toString()
            val cable = binding.autoCompleteTextViewCable.text.toString()
            val names = (binding.tilUser.editText as AutoCompleteTextView).text
            val selectedUser = userArrayList.singleOrNull { it.Name == names.toString() }
            if (names.isEmpty()){
                binding.tilDevices.error = "Required"
                binding.autoCompleteTextViewCable.error = "Required"
                binding.autoCompleteTextViewCharger.error = "Required"
                binding.tilUser.error = "Required"
            }else{
                updateData(selectedUser?.id ?:"", selectedUser?.Name ?: "", device, charger, cable)
            }
            userArrayList.map { Log.e("TAG", "user list -> ${it.Name}") }
            Log.e("TAG", " editText: $names")
            Log.e("TAG", " editText: $selectedUser")

        }
    }

    private fun updateData(userId: String, name: String, device: String, charger: String, cable: String) {
        dbref = FirebaseDatabase.getInstance().reference
        val user = mapOf(
            "id" to userId,
            "Name" to name,
            "Device" to device,
            "Charger" to charger,
            "Cable" to cable,

            )
        Log.wtf("id::::::::", "$user")

        dbref.child("Users").child(userId).updateChildren(user).addOnSuccessListener {
            Toast.makeText(this, "Successfully Added !", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to Add !", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("Users")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    userArrayList = mutableListOf<User>()
                    for (userSnapshot in snapshot.children) {
                        val mUser = userSnapshot.getValue(User::class.java)
                        mUser?.let { user ->
                            userArrayList.add(user)
                            Log.wtf("Name", user.Name)
                        } ?: run {
                            Log.wtf("Name", "user is null")
                        }
                    }
                    userArrayList.forEach { Log.e("TAG", "Before: $it") }
                    userArrayList.sortBy { it.Name?.lowercase() }
                    userArrayList.forEach { Log.e("TAG", "sorted: $it") }

                    val orderedUserList = mutableListOf<User>()
                    userArrayList.forEach {
                        if (it.Name?.lowercase() == "none") {
                            orderedUserList.add(0, it)
                        } else {
                            orderedUserList.add(it)
                        }
                    }
                    orderedUserList.forEach { Log.e("TAG", "Ordered: $it") }
                    (binding.tilUser.editText as AutoCompleteTextView).setAdapter(ArrayAdapter(this@AssignActivity, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, orderedUserList))

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getDeviceData() {
        dbref = FirebaseDatabase.getInstance().getReference("Devices")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val deviceArrayList = mutableListOf<Device>()
                    for (userSnapshot in snapshot.children) {
                        val mDevice = userSnapshot.getValue(Device::class.java)
                        mDevice?.let { device ->
                            deviceArrayList.add(device)
                            Log.wtf("Name", device.Name)
                        } ?: run {
                            Log.wtf("Name", "device is null")
                        }
                    }
                    deviceArrayList.forEach { Log.e("TAG", "Before: $it") }
                    deviceArrayList.sortBy { it.Name?.lowercase() }
                    deviceArrayList.forEach { Log.e("TAG", "sorted: $it") }

                    val orderedDeviceList = mutableListOf<Device>()
                    deviceArrayList.forEach {
                        if (it.Name?.lowercase() == "none") {
                            orderedDeviceList.add(0, it)
                        } else {
                            orderedDeviceList.add(it)
                        }
                    }
                    orderedDeviceList.forEach { Log.e("TAG", "Ordered: $it") }

                    (binding.tilDevices.editText as AutoCompleteTextView).setAdapter(ArrayAdapter(this@AssignActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, orderedDeviceList))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Data not Found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getChargerData() {
        dbref = FirebaseDatabase.getInstance().getReference("Charger")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val chargerArrayList = mutableListOf<Charger>()
                    for (userSnapshot in snapshot.children) {
                        val mCharger = userSnapshot.getValue(Charger::class.java)
                        mCharger?.let { charger ->
                            chargerArrayList.add(charger)
                            Log.wtf("Name", charger.Name)
                        } ?: run {
                            Log.wtf("Name", "charger is null")
                        }
                    }
                    chargerArrayList.forEach { Log.e("TAG", "Before: $it") }
                    chargerArrayList.sortBy { it.Name?.lowercase() }
                    chargerArrayList.forEach { Log.e("TAG", "sorted: $it") }

                    val orderedChargerList = mutableListOf<Charger>()
                    chargerArrayList.forEach {
                        if (it.Name?.lowercase() == "none") {
                            orderedChargerList.add(0, it)
                        } else {
                            orderedChargerList.add(it)
                        }
                    }
                    orderedChargerList.forEach { Log.e("TAG", "Ordered: $it") }

                    binding.autoCompleteTextViewCharger.setAdapter(ArrayAdapter(this@AssignActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, orderedChargerList))

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Data not Found", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getCableData() {
        dbref = FirebaseDatabase.getInstance().getReference("Cables")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val cableArrayList = mutableListOf<Cable>()
                    for (userSnapshot in snapshot.children) {
                        val mCable = userSnapshot.getValue(Cable::class.java)
                        mCable?.let { cable ->
                            cableArrayList.add(cable)
                            Log.wtf("Name", cable.Name)
                        } ?: run {
                            Log.wtf("Name", "cable is null")
                        }
                    }
                    cableArrayList.forEach { Log.e("TAG", "Before: $it") }
                    cableArrayList.sortBy { it.Name?.lowercase() }
                    cableArrayList.forEach { Log.e("TAG", "sorted: $it") }

                    val orderedCableList = mutableListOf<Cable>()
                    cableArrayList.forEach {
                        if (it.Name?.lowercase() == "none") {
                            orderedCableList.add(0, it)
                        } else {
                            orderedCableList.add(it)
                        }
                    }
                    orderedCableList.forEach { Log.e("TAG", "Ordered: $it") }

                    binding.autoCompleteTextViewCable.setAdapter(ArrayAdapter(this@AssignActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, orderedCableList))


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Data not Found", Toast.LENGTH_SHORT).show()
            }

        })
    }


}