package com.esewa.tdi.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.esewa.tdi.adapter.CardAdapter
import com.esewa.tdi.data.Device
import com.esewa.tdi.data.User
import com.esewa.tdi.interfaces.CardClickListener
import com.esewa.tdi.ui.activity.assign.AssignActivity
import com.esewa.tdi.ui.activity.cable.CableActivity
import com.esewa.tdi.ui.activity.charger.ChargerActivity
import com.esewa.tdi.ui.activity.device.DeviceActivity
import com.esewa.tdi.ui.activity.login.LoginActivity
import com.esewa.tdi.ui.activity.profile.ProfileActivity
import com.esewa.tdi.ui.activity.userdetail.UserDetailActivity
import com.esewa.tdi.util.*
import com.google.firebase.database.*
import esewa.tdi.R
import esewa.tdi.databinding.ActivityMainBinding
import java.util.Collections.addAll

class MainActivity : AppCompatActivity(), CardClickListener {

    companion object {
        private var TAG = MainActivity::class.java.simpleName.toString()
    }

    private lateinit var userList: List<User>
    private var backPressedTime = 0L
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbref: DatabaseReference

    private val sharedPrefUtil: SharedPrefUtil by lazy { SharedPrefUtil(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUserData()
        getAssignedUserData()
        handleAdminTask()
        refreshApp()


        binding.fab.setOnClickListener {
            startActivity(Intent(this, AssignActivity::class.java))
            binding.userRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        }
        showDialog()

        binding.btnDevices.setOnClickListener {
            startActivity(Intent(this, DeviceActivity::class.java))
        }
        binding.btnCable.setOnClickListener {
            startActivity(Intent(this, CableActivity::class.java))
        }
        binding.btnCharger.setOnClickListener {
            startActivity(Intent(this, ChargerActivity::class.java))
        }
    }

    private fun setUserData() {
        binding.userName.text = sharedPrefUtil.getStringData(SharedPrefUtil.NAME) ?: "User :"
        binding.userEmail.text = sharedPrefUtil.getStringData(SharedPrefUtil.EMAIL) ?: "-"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.nav_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }

            R.id.nav_logout -> {
                SessionUtil(this).signout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog() {
        binding.progressBar.visible()
        binding.recyclerCard.isEnabled = false
    }

    private fun hideDialog() {
        binding.progressBar.invisible()
        binding.recyclerCard.isEnabled = true
    }

    private fun handleAdminTask() {
        sharedPrefUtil.getBooleanData(SharedPrefUtil.IS_ADMIN)?.let { isAdmin ->
            if (isAdmin) {
                binding.fab.visible()
            } else {
                binding.fab.gone()
            }
        } ?: run {
            binding.fab.gone()
        }
    }

    private fun getAssignedUserData() {

        dbref = FirebaseDatabase.getInstance().getReference("Users")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val userArrayList = mutableListOf<User>()
                    for (userSnapshot in snapshot.children) {
                        //Log.e("TAG", "userSnapshot.value ${userSnapshot.value}")
                        val user = try {
                            userSnapshot.getValue(User::class.java)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                        user?.let { userArrayList.add(it) }
                    }
                    val allUsers = CardAdapter(userArrayList, this@MainActivity)
                    val assignUser = mutableListOf<User>().apply {
                        addAll(userArrayList)
                    }
                            for (user in userArrayList) {
                                if (user.Device == "none" || user.Device == null) {
                                    assignUser.map { Log.e("TAG", "assigned : ${it.Device}") }
                                    assignUser.remove(user)
                                }

                            binding.userRecyclerView.adapter = CardAdapter(assignUser, this@MainActivity)
                            }

                    hideDialog()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun refreshApp() {
        binding.swipeToRefresh.setOnRefreshListener {

            dbref = FirebaseDatabase.getInstance().getReference("Users")
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        val userArrayList = mutableListOf<User>()
                        for (userSnapshot in snapshot.children) {
                            //Log.e("TAG", "userSnapshot.value ${userSnapshot.value}")
                            val user = try {
                                userSnapshot.getValue(User::class.java)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                null
                            }
                            user?.let { userArrayList.add(it) }
                        }
                        val allUsers = CardAdapter(userArrayList, this@MainActivity)
                        val assignUser = mutableListOf<User>().apply {
                            addAll(userArrayList)
                        }
                        for (user in userArrayList) {
                            if (user.Device == "none") {
                                assignUser.map { Log.e("TAG", "assigned : ${it.Device}") }
                                assignUser.remove(user)
                            }
                            binding.userRecyclerView.adapter = CardAdapter(assignUser, this@MainActivity)
                        }

                        hideDialog()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            Toast.makeText(this, "Page refreshed!", Toast.LENGTH_SHORT).show()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    override fun onItemClick(user: User) {
        val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(applicationContext, "Press back again to exit app", Toast.LENGTH_SHORT)
                .show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}