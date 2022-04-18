package com.esewa.tdi.ui.activity.device

import android.R
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esewa.tdi.data.Device
import com.esewa.tdi.data.User
import com.google.firebase.database.*
import esewa.tdi.databinding.ActivityDeviceBinding

class DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding
    private lateinit var dbref: DatabaseReference
    private var deviceArrayList = mutableListOf<Device>()
    private var userArrayList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getDeviceData()
    }

    private fun getDeviceData() {
        dbref = FirebaseDatabase.getInstance().getReference("Devices")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    deviceArrayList = mutableListOf()
                    for (userSnapshot in snapshot.children) {
                        val mDevice = userSnapshot.getValue(Device::class.java)
                        mDevice?.let { device ->
                            deviceArrayList.add(device)
                            Log.wtf("Name", device.Name)
                        } ?: run {
                            Log.wtf("Name", "device is null")
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
//        val newList = mutableListOf<Device>()

        Log.e("TAG", "-------------------------------------------")
        deviceArrayList.map { Log.e("TAG", "old ${it.id} ${it.Name}") }
        userArrayList.map { Log.e("TAG", "use ${it.id} ${it.Device}") }
        val newList = deviceArrayList.filter { it.id !in userArrayList.map { item -> item.Device } }.toMutableList()
        Log.e("TAG", "-------------------------------------------")
        newList.map { Log.e("TAG", "new ${it.id} ${it.Name}") }

        /* for (device in deviceArrayList) {
             for (user in userArrayList) {
                 if (device.id != user.Device) {
                     newList.add(device)
                     break
                 }
             }
         }*/
        var pos = -1
        for (i in deviceArrayList.indices) {
            if (deviceArrayList[i].id == "D0") {
                pos = i
            }
        }
        newList.add(0, deviceArrayList[pos])
        Log.e("TAG", "-------------------------------------------")
        newList.map { Log.e("TAG", "add ${it.id} ${it.Name}") }
        binding.listDevice.adapter = ArrayAdapter(this@DeviceActivity, R.layout.simple_list_item_1, newList)
    }

}