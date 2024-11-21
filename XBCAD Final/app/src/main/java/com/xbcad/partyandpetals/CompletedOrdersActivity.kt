package com.xbcad.partyandpetals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xbcad.partyandpetals.databinding.ActivityCompletedOrdersBinding

class CompletedOrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompletedOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the correct layout
        binding = ActivityCompletedOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
