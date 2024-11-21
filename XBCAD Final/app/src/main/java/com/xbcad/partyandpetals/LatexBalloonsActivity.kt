package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xbcad.partyandpetals.Admin_data.Balloon
import com.xbcad.partyandpetals.Admin_data.CartItem
import com.xbcad.partyandpetals.databinding.ActivityLatexBalloonsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class LatexBalloonsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLatexBalloonsBinding
    private lateinit var balloonAdapter: BalloonCartAdapter
    private val balloonList = mutableListOf<Balloon>()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatexBalloonsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pageTitle.text = "Latex Balloons"

        // Set up RecyclerView
        binding.balloonRecyclerView.layoutManager = LinearLayoutManager(this)
        balloonAdapter = BalloonCartAdapter(balloonList) { balloon, quantity ->
            addToCart(balloon, quantity)
        }
        binding.balloonRecyclerView.adapter = balloonAdapter

        // Fetch balloons from Firestore
        fetchBalloons()

        // Set up listeners for buttons
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        binding.viewCartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        binding.checkoutButton.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }
    }

    // Fetch balloons with category "Latex" from Firestore
    private fun fetchBalloons() {
        try {
            db.collection("products")
                .whereEqualTo("category", "Latex")
                .get()
                .addOnSuccessListener { documents ->
                    balloonList.clear()
                    for (document in documents) {
                        try {
                            document.toObject<Balloon>()?.let { balloonList.add(it) }
                        } catch (e: Exception) {
                            Log.e(
                                "LatexBalloonsActivity",
                                "Error converting document to Balloon",
                                e
                            )
                        }
                    }
                    balloonAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Log.e("LatexBalloonsActivity", "Error fetching latex balloons", e)
                }
        } catch (e: Exception) {
            Log.e("LatexBalloonsActivity", "Unexpected error in fetchBalloons()", e)
        }
    }

    // Function to add a balloon to the cart with specified quantity
    private fun addToCart(balloon: Balloon, quantity: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null && quantity > 0) {
            val cartDocRef = db.collection("cart").document(userId)

            // Check if the balloon already exists in the cart
            cartDocRef.collection("items").document(balloon.name)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Update existing item quantity
                        val currentQuantity = document.getLong("quantity") ?: 0
                        cartDocRef.collection("items").document(balloon.name)
                            .update("quantity", currentQuantity + quantity)
                            .addOnSuccessListener {
                                Log.d("Cart", "Updated ${balloon.name} with new quantity.")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Cart", "Failed to update item quantity", e)
                            }
                    } else {
                        // Add new item to cart
                        val cartItem = CartItem(
                            name = balloon.name,
                            price = balloon.price,
                            quantity = quantity
                        )
                        cartDocRef.collection("items").document(balloon.name)
                            .set(cartItem)
                            .addOnSuccessListener {
                                Log.d("Cart", "Added ${balloon.name} to cart with quantity $quantity.")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Cart", "Failed to add item to cart", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Cart", "Error retrieving cart item", e)
                }
        } else {
            Log.w("Cart", "User not logged in or invalid quantity.")
        }
    }
}