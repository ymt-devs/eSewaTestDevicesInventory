package esewa.tdi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import esewa.tdi.databinding.ActivityAddDeviceChargerCableBinding
import java.text.SimpleDateFormat

class AddDeviceChargerCableActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDeviceChargerCableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDeviceChargerCableBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnAddDevice.setOnClickListener {
            val name = binding.device.text.toString()
            val currentTime =  System.currentTimeMillis() / 1000L;
            addDevice( id = currentTime.toString(), name = name )
        }
        binding.btnAddCharger.setOnClickListener {
            val name = binding.charger.text.toString()
            val currentTime =  System.currentTimeMillis() / 1000L;
            addCharger( id = currentTime.toString(), name = name )
        }
        binding.btnAddCable.setOnClickListener {
            val name = binding.cable.text.toString()
            val currentTime =  System.currentTimeMillis() / 1000L;
            addCable( id = currentTime.toString(), name = name )
        }

    }

    private fun addDevice(id: String, name: String) {
        val device = mapOf(
            "id" to id,
            "Name" to name
        )
        Log.wtf("id::::::::", "$device")
        val dbref = FirebaseDatabase.getInstance().reference
        dbref.child("Devices").child(id).updateChildren(device).addOnSuccessListener {
            Toast.makeText(this, "Successfully Added !", Toast.LENGTH_SHORT).show()
            binding.device.setText("")
            binding.device.clearFocus()
        }.addOnFailureListener {
            // TODO: Exception 
        }

    }

    private fun addCharger(id: String, name: String) {
        val charger = mapOf(
            "id" to id,
            "Name" to name
        )
        Log.wtf("id::::::::", "$charger")
        val dbref = FirebaseDatabase.getInstance().reference
        dbref.child("Charger").child(id).updateChildren(charger).addOnSuccessListener {
            Toast.makeText(this, "Successfully Added !", Toast.LENGTH_SHORT).show()
            binding.charger.setText("")
            binding.charger.clearFocus()
        }.addOnFailureListener {
            // TODO: Exception
        }

    }

    private fun addCable(id: String, name: String) {
        val cable = mapOf(
            "id" to id,
            "Name" to name
        )
        Log.wtf("id::::::::", "$cable")
        val dbref = FirebaseDatabase.getInstance().reference
        dbref.child("Cables").child(id).updateChildren(cable).addOnSuccessListener {
            Toast.makeText(this, "Successfully Added !", Toast.LENGTH_SHORT).show()
            binding.cable.setText("")
            binding.cable.clearFocus()
        }.addOnFailureListener {
            // TODO: Exception
        }

    }
}