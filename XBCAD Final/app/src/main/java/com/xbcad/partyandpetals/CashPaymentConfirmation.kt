package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.xbcad.partyandpetals.databinding.ActivityPaymentConfirmationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CashPaymentConfirmation : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentConfirmationBinding
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedLocation = intent.getStringExtra("selectedLocation") ?: "Unknown Location"
        val totalAmount = intent.getDoubleExtra("totalAmount", 0.0)

        generateUniqueOrderNumber { orderNumber ->
            val pickupTime = calculatePickupTime()
            saveOrderDetails(orderNumber, selectedLocation, pickupTime, totalAmount)

            binding.orderNumber.text = "Order Number: $orderNumber"
            binding.pickupTime.text = "Pickup Time: $pickupTime at $selectedLocation"
            binding.totalAmount.text = "Total: R%.2f".format(totalAmount)

            clearCart()

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, ActivityHome::class.java))
                finish()
            }, 5000)
        }
    }

    private fun calculatePickupTime(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 30)
        return android.text.format.DateFormat.format("HH:mm", calendar).toString()
    }

    private fun generateUniqueOrderNumber(onComplete: (String) -> Unit) {
        db.collectionGroup("orders").get()
            .addOnSuccessListener { documents ->
                val maxOrderNumber = documents.mapNotNull { it.getString("orderNumber")?.toIntOrNull() }.maxOrNull() ?: 0
                val newOrderNumber = (maxOrderNumber + 1).toString().padStart(5, '0')
                onComplete(newOrderNumber)
            }
            .addOnFailureListener {
                onComplete("00001")
            }
    }

    private fun saveOrderDetails(orderNumber: String, location: String, pickupTime: String, totalAmount: Double) {
        val userId = currentUser?.uid ?: return

        // Order data structure
        val orderData = mapOf(
            "orderNumber" to orderNumber,
            "pickupLocation" to location,
            "pickupTime" to pickupTime,
            "totalAmount" to totalAmount,
            "timestamp" to System.currentTimeMillis(),
            "userId" to userId // Add userId for admin reference
        )

        // Save to user's orders subcollection
        db.collection("users").document(userId).collection("orders").add(orderData)

        // Save to main orders collection
        db.collection("orders").add(orderData)
    }

    private fun clearCart() {
        val userId = currentUser?.uid ?: return
        db.collection("cart").document(userId).collection("items").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                }
            }
    }
}