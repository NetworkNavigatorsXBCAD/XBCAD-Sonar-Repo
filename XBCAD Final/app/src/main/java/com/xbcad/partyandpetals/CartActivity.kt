package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xbcad.partyandpetals.Adapters.CartAdapter
import com.xbcad.partyandpetals.Admin_data.CartItem
import com.xbcad.partyandpetals.databinding.ActivityCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val cartItems = mutableListOf<CartItem>()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        val cartAdapter = CartAdapter(cartItems, { position -> removeFromCart(position) }, { _, _ -> updateSubtotal() }, false)
        binding.cartRecyclerView.adapter = cartAdapter

        fetchCartItems(cartAdapter)

        binding.checkoutButton.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty. Add items before checkout.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            }
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, ActivityHome::class.java))
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
        binding.bottomNavigation.menu.findItem(R.id.nav_cart).isChecked = true
    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun fetchCartItems(cartAdapter: CartAdapter) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("cart").document(userId).collection("items").get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    binding.emptyCartMessage.visibility = View.VISIBLE
                    binding.cartRecyclerView.visibility = View.GONE
                } else {
                    binding.emptyCartMessage.visibility = View.GONE
                    binding.cartRecyclerView.visibility = View.VISIBLE
                    cartItems.clear()
                    for (document in documents) {
                        val cartItem = document.toObject(CartItem::class.java)
                        cartItems.add(cartItem)
                    }
                    cartAdapter.notifyDataSetChanged()
                    updateSubtotal()
                }
            }
    }

    private fun updateSubtotal() {
        val subtotal = cartItems.sumOf { it.getPriceAsDouble() * it.quantity }
        binding.subtotalText.text = "Subtotal: R%.2f".format(subtotal)
    }

    private fun removeFromCart(position: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val cartItem = cartItems[position]

        db.collection("cart").document(userId).collection("items").document(cartItem.name).delete()
            .addOnSuccessListener {
                cartItems.removeAt(position)
                binding.cartRecyclerView.adapter?.notifyItemRemoved(position)
                updateSubtotal()
            }
    }
}
