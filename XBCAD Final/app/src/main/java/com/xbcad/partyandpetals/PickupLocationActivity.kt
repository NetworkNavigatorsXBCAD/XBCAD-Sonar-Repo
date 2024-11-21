package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PickupLocationActivity : AppCompatActivity() {

    private lateinit var locationRadioGroup: RadioGroup
    private lateinit var payButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_input)

        locationRadioGroup = findViewById(R.id.locationRadioGroup)
        payButton = findViewById(R.id.payButton)

        val totalAmount = intent.getDoubleExtra("totalAmount", 0.0)

        payButton.setOnClickListener {
            val selectedLocationId = locationRadioGroup.checkedRadioButtonId
            if (selectedLocationId == -1) {
                Toast.makeText(this, "Please select a pickup location", Toast.LENGTH_SHORT).show()
            } else {
                val selectedLocation = findViewById<RadioButton>(selectedLocationId).text.toString()

                // Pass totalAmount and selectedLocation to ConfirmationActivity
                val intent = Intent(this, CashPaymentConfirmation::class.java)
                intent.putExtra("totalAmount", totalAmount)
                intent.putExtra("selectedLocation", selectedLocation)
                startActivity(intent)
            }
        }
    }
}
