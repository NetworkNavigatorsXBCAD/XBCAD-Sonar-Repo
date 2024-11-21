package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xbcad.partyandpetals.Adapters.CartAdapter
import com.xbcad.partyandpetals.Admin_data.CartItem
import com.xbcad.partyandpetals.databinding.ActivityCheckoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val cartItems = mutableListOf<CartItem>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.productDetails.layoutManager = LinearLayoutManager(this)
        val cartAdapter = CartAdapter(cartItems, { position -> removeItemFromCart(position) }, { _, _ -> }, true)
        binding.productDetails.adapter = cartAdapter

        loadCartItems(cartAdapter)

        binding.payAtStoreButton.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty. Add items to proceed.", Toast.LENGTH_SHORT).show()
            } else {
                val totalAmount = calculateTotal()

                // Pass totalAmount to PickupLocationActivity
                val intent = Intent(this, PickupLocationActivity::class.java)
                intent.putExtra("totalAmount", totalAmount)
                startActivity(intent)
            }
        }
    }

    private fun loadCartItems(cartAdapter: CartAdapter) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("cart").document(userId).collection("items").get()
            .addOnSuccessListener { documents ->
                cartItems.clear()
                for (document in documents) {
                    val cartItem = document.toObject(CartItem::class.java)
                    cartItems.add(cartItem)
                }
                cartAdapter.notifyDataSetChanged()
                updateTotalAndSubtotal()
            }
    }

    private fun updateTotalAndSubtotal() {
        val subtotal = calculateSubtotal()
        val total = subtotal + 10.0 // Example tax/fee
        binding.subtotalAmount.text = "Subtotal: R%.2f".format(subtotal)
        binding.totalText.text = "Total: R%.2f".format(total)
    }

    private fun calculateSubtotal() = cartItems.sumOf { it.getPriceAsDouble() * it.quantity }

    private fun calculateTotal() = calculateSubtotal() + 10.0

    private fun removeItemFromCart(position: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val cartItem = cartItems[position]

        if (userId != null) {
            db.collection("cart")
                .document(userId)
                .collection("items")
                .document(cartItem.name)
                .delete()
                .addOnSuccessListener {
                    cartItems.removeAt(position)
                    binding.productDetails.adapter?.notifyItemRemoved(position)
                    updateTotalAndSubtotal()
                }
        }
    }
}
