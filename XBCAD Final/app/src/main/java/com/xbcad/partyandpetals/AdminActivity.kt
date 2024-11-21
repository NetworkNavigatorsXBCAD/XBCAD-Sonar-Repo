package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xbcad.partyandpetals.Admin_data.Order
import com.xbcad.partyandpetals.Admin_Adapter.AdminOrdersAdapter
import com.xbcad.partyandpetals.databinding.ActivityAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private val db = FirebaseFirestore.getInstance()
    private val currentOrders = mutableListOf<Order>()
    private lateinit var ordersAdapter: AdminOrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Correctly use the generated binding class
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView before data fetching
        binding.currentOrdersRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up BottomNavigationView
        binding.bottomAdminNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_currentOrders -> true // Handle the Current Orders selection
                R.id.nav_stock -> {
                    startActivity(Intent(this, StockActivity::class.java))
                    true
                }
                R.id.nav_LogOut ->{
                    logout()
                    true
                }

                else -> false
            }
        }

        // Fetch current orders from Firestore
        fetchCurrentOrders()
    }

    /*This code was adapted from StackOverflow
    https://stackoverflow.com/questions/72769031/how-to-retrieve-data-from-firestore
    Author - Alexandros Giotas
    https://stackoverflow.com/users/13270908/alexandros-giotas
     */

    // Fetch current orders from Firestore using order number
    private fun fetchCurrentOrders() {
        db.collection("orders").get()
            .addOnSuccessListener { documents ->
                currentOrders.clear()
                for (document in documents) {
                    val order = document.toObject(Order::class.java)
                    order.orderNumber?.let { // Ensure order number exists
                        currentOrders.add(order)
                    }
                }

                // Initialize the adapter after fetching the data
                ordersAdapter = AdminOrdersAdapter(currentOrders)

                // Set the adapter to RecyclerView
                binding.currentOrdersRecyclerView.adapter = ordersAdapter

                // Notify the adapter that the data has changed
                ordersAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Handle any errors during fetching
            }
    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

