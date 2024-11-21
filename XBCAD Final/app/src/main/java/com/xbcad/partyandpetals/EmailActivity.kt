package com.xbcad.partyandpetals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xbcad.partyandpetals.databinding.ActivityEmailBinding

class EmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button click listener
        binding.backButton.setOnClickListener {
            finish() // Navigate back to the previous screen
        }
    }
}
