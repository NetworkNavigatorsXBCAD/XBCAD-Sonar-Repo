package com.xbcad.partyandpetals.StockCategories
//this code was adapted from stackoverflow
//https://stackoverflow.com/questions/75720964/view-binding-to-switch-between-activities
//Muhammad Asad
//https://stackoverflow.com/users/13903584/muhammad-asad

//this code was adapted from stackoverflow
//https://stackoverflow.com/questions/77481039/android-studio-kotlin-navigation-drawer-bottom-navigation-bar
//UwUs
//https://stackoverflow.com/users/19108801/uwus

//this code was adapted from stackoverflow
//https://stackoverflow.com/questions/73510902/how-to-observe-a-list-from-adapter-in-android-kotlin
//oyeraghib
//https://stackoverflow.com/users/9062752/oyeraghib


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xbcad.partyandpetals.R
import com.xbcad.partyandpetals.StockActivity

class AdminCreateRequestActivity : AppCompatActivity() {

    private lateinit var etOrderNumber: EditText
    private lateinit var etDate: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_create_requests)
        // Initialize views
        etOrderNumber = findViewById(R.id.etOrderNumber)
        etDate = findViewById(R.id.etDate)
        etDescription = findViewById(R.id.etDescription)
        btnSave = findViewById(R.id.btnSave)
        backButton = findViewById(R.id.backButton)

        // Handle save button click
        btnSave.setOnClickListener {
            val orderNumber = etOrderNumber.text.toString().trim()
            val date = etDate.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (orderNumber.isEmpty() || date.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Handle the save action (e.g., save to database or send to server)
                Toast.makeText(this, "Request saved!", Toast.LENGTH_SHORT).show()
                clearFields()
            }
        }

        // Handle back button click
        backButton.setOnClickListener {
            val intent = Intent(this, StockActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Function to clear fields after saving
    private fun clearFields() {
        etOrderNumber.text.clear()
        etDate.text.clear()
        etDescription.text.clear()
    }
}
