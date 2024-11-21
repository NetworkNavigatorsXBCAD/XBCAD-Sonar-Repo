package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class RecieptEmailActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var nextBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reciept_email)

        // Initialize UI elements
        emailInput = findViewById(R.id.emailInput)

        nextBtn = findViewById(R.id.NextBtn)

        // Set the submit button click listener
        nextBtn.setOnClickListener {
            val intent = Intent(this, PickupLocationActivity::class.java)
            startActivity(intent)
        }
    }
}


