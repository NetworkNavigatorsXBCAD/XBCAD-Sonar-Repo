package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xbcad.partyandpetals.Adapters.OrdersAdapter
import com.xbcad.partyandpetals.Admin_data.Order
import com.xbcad.partyandpetals.databinding.ActivityOrdersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private val pastOrders = mutableListOf<Order>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.previousOrdersRecyclerView.layoutManager = LinearLayoutManager(this)
        val pastAdapter = OrdersAdapter(pastOrders)
        binding.previousOrdersRecyclerView.adapter = pastAdapter

        // Fetch orders
        fetchOrders(pastAdapter)

        // Set up navigation
        setupBottomNavigation()
    }

    // Fetch and display past orders
    private fun fetchOrders(pastAdapter: OrdersAdapter) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("users").document(userId).collection("orders").get()
            .addOnSuccessListener { documents ->
                pastOrders.clear()
                for (document in documents) {
                    val order = document.toObject(Order::class.java)
                    pastOrders.add(order)
                }
                pastAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Handle any errors during fetching
            }
    }

    // Set up bottom navigation
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, ActivityHome::class.java))
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.nav_contact -> {
                    startActivity(Intent(this, ContactUsActivity::class.java))
                    true
                }
                R.id.nav_logout -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.menu.findItem(R.id.nav_orders).isChecked = true
    }
}
