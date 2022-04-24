package com.esewa.tdi.ui.activity.cable

import android.R
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.esewa.tdi.data.Cable
import com.esewa.tdi.data.Device
import com.esewa.tdi.data.User
import com.google.firebase.database.*
import esewa.tdi.databinding.ActivityCableBinding

class CableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCableBinding
    private lateinit var dbref: DatabaseReference
    private var cableArrayList = mutableListOf<Cable>()
    private var userArrayList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //       cableArrayList = arrayListOf<Cable>()
        getCableData()
    }

    /*private fun getCableData() {
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
    }*/

    private fun getCableData() {
        dbref = FirebaseDatabase.getInstance().getReference("Cables")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    cableArrayList = mutableListOf()
                    for (userSnapshot in snapshot.children) {
                        val mCable = userSnapshot.getValue(Cable::class.java)
                        mCable?.let { cable ->
                            cableArrayList.add(cable)
                            Log.wtf("Name", cable.Name)
                        } ?: run {
                            Log.wtf("Name", "cable is null")
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
        cableArrayList.map { Log.e("TAG", "old ${it.id} ${it.Name}") }
        userArrayList.map { Log.e("TAG", "use ${it.id} ${it.Cable}") }

        Log.e("TAG", "-------------------------------------------")

        val newCableList = mutableListOf<Cable>().apply {
            addAll(cableArrayList)
        }
        newCableList.map { Log.e("TAG", "copy ${it.id} ${it.Name}") }

        Log.e("TAG", "-------------------------------------------")

        for (cable in cableArrayList) {
            for (user in userArrayList) {
                if (cable.Name == user.Cable) {
                    newCableList.remove(cable)
                }
            }
        }

        newCableList.map { Log.e("TAG", "final ${it.id} ${it.Name}") }

        Log.e("TAG", "-------------------------------------------")

        binding.listCable.adapter = ArrayAdapter(this@CableActivity, R.layout.simple_list_item_1, newCableList)




    }

}