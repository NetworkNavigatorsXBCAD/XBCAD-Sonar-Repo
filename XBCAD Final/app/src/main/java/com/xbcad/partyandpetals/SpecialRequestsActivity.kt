package com.xbcad.partyandpetals

// This code was adapted from StackOverflow
// https://stackoverflow.com/questions/75720964/view-binding-to-switch-between-activities
// Muhammad Asad
// https://stackoverflow.com/users/13903584/muhammad-asad

// This code was adapted from StackOverflow
// https://stackoverflow.com/questions/77481039/android-studio-kotlin-navigation-drawer-bottom-navigation-bar
// UwUs
// https://stackoverflow.com/users/19108801/uwus

// This code was adapted from StackOverflow
// https://stackoverflow.com/questions/73510902/how-to-observe-a-list-from-adapter-in-android-kotlin
// oyeraghib
// https://stackoverflow.com/users/9062752/oyeraghib


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xbcad.partyandpetals.databinding.ActivitySpecialRequestsBinding

class SpecialRequestsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpecialRequestsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecialRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle WhatsApp button click
        binding.whatsappButton.setOnClickListener {
            openWhatsApp()
        }

        // Handle Instagram button click
        binding.instagramButton.setOnClickListener {
            openInstagram()
        }

        // Handle Facebook button click
        binding.facebookButton.setOnClickListener {
            openFacebook()
        }

        // Handle Email button click
        binding.emailButton.setOnClickListener {
            sendEmail()
        }
    }

    // Function to open WhatsApp
    private fun openWhatsApp() {
        val phoneNumber = "+27768961047" // Replace with business WhatsApp number
        val url = "https://wa.me/$phoneNumber"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to open Instagram
    private fun openInstagram() {
        val uri = Uri.parse("http://instagram.com/_u/semesh_18") // Replace with your Instagram username
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.instagram.android")
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Open in browser if Instagram app is not installed
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/semesh_18")))
        }
    }

    // Function to open Facebook
    private fun openFacebook() {
        val facebookUrl =
            "https://www.facebook.com/profile.php?id=61550194920759&mibextid=ZbWKwL" // Replace with your Facebook page ID
        val webUrl =
            "https://www.facebook.com/profile.php?id=61550194920759&mibextid=ZbWKwL" // Replace with your Facebook page link

        val intent = Intent(Intent.ACTION_VIEW)
        try {
            // Try opening the Facebook app
            intent.data = Uri.parse(facebookUrl)
            startActivity(intent)
        } catch (e: Exception) {
            // Fall back to opening in a browser
            intent.data = Uri.parse(webUrl)
            startActivity(intent)
        }
    }

    // Function to send an email
    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:") // Only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("thivarshangovender@gmail.com")) // Replace with business email
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Special Request Inquiry")

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No email apps installed", Toast.LENGTH_SHORT).show()
        }
    }
}
