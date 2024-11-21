package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xbcad.partyandpetals.databinding.ActivityHomeBinding

class ActivityHome : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listeners for image buttons
        binding.bubbleBalloonsButton.setOnClickListener {
            startActivity(Intent(this, BubbleBalloonsActivity::class.java))
        }

        binding.foilBalloonsButton.setOnClickListener {
            startActivity(Intent(this, FoilBalloonsActivity::class.java))
        }

        binding.latexBalloonsButton.setOnClickListener {
            startActivity(Intent(this, LatexBalloonsActivity::class.java))
        }

        binding.specialRequestsButton.setOnClickListener {
            startActivity(Intent(this, SpecialRequestsActivity::class.java))
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true // Current activity
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.nav_orders -> {
                    startActivity(Intent(this, OrdersActivity::class.java))
                    true
                }
                R.id.nav_contact -> {
                    startActivity(Intent(this, ContactUsActivity::class.java))
                    true
                }
                R.id.nav_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
