package com.xbcad.partyandpetals

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xbcad.partyandpetals.databinding.ActivityContactUsBinding

class ContactUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pageTitle.text = "Contact Us"
        binding.bottomNavigation.selectedItemId = R.id.nav_contact

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.nav_home -> {
                    startActivity(Intent(this, ActivityHome::class.java))
                    true
                }
                R.id.nav_orders -> {
                    startActivity(Intent(this, OrdersActivity::class.java))
                    true
                }
                R.id.nav_contact -> true
                R.id.nav_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }

        binding.phoneButton.setOnClickListener { makePhoneCall() }
        binding.whatsappButton.setOnClickListener { openWhatsApp() }
        binding.emailButton.setOnClickListener { sendEmail() }
    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun makePhoneCall() {
        val phoneNumber = "+27768961047"
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        try {
            startActivity(callIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Unable to make a call", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    This code was adapted from StackOverflow
    https://stackoverflow.com/questions/75644844/how-to-lauch-whatsapp-contact-list
    Author - Tippu Fisal Sheriff
    https://stackoverflow.com/users/11690526/tippu-fisal-sheriff
     */
    private fun openWhatsApp() {
        val phoneNumber = "+27768961047"
        val url = "https://wa.me/$phoneNumber"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("cashsend60@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Contact Us Inquiry")
        }
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No email apps installed", Toast.LENGTH_SHORT).show()
        }
    }
}
