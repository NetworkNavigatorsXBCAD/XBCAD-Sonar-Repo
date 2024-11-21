package com.xbcad.partyandpetals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup)

        val customerButton: Button = findViewById(R.id.customerButton)
        val adminButton: Button = findViewById(R.id.adminButton)

        customerButton.setOnClickListener {
            startActivity(Intent(this, CustomerRegistrationActivity::class.java))
        }

        adminButton.setOnClickListener {
            startActivity(Intent(this, AdminRegistrationActivity::class.java))
        }
    }
}