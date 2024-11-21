package com.xbcad.partyandpetals.Adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xbcad.partyandpetals.Admin_data.CartItem
import com.xbcad.partyandpetals.R
import com.xbcad.partyandpetals.databinding.ItemCartBinding
import com.xbcad.partyandpetals.databinding.ItemCheckoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartAdapter(
    private val cartItems: MutableList<CartItem>,
    private val onRemoveFromCart: (Int) -> Unit,
    private val onQuantityChange: (Int, Int) -> Unit, // Callback for quantity change
    private val isCheckout: Boolean // Flag to distinguish between cart and checkout views
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Define view types
    private val VIEW_TYPE_CART = 0
    private val VIEW_TYPE_CHECKOUT = 1

    // ViewHolder for Cart layout
    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder for Checkout layout
    inner class CheckoutViewHolder(val binding: ItemCheckoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (isCheckout) VIEW_TYPE_CHECKOUT else VIEW_TYPE_CART
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CART -> {
                val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CartViewHolder(binding)
            }
            VIEW_TYPE_CHECKOUT -> {
                val binding = ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CheckoutViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cartItem = cartItems[position]

        when (holder) {
            is CartViewHolder -> {
                // Bind data for Cart layout
                with(holder.binding) {
                    productName.text = cartItem.name
                    productPrice.text = "R%.2f".format(cartItem.getPriceAsDouble())
                    quantityText.text = cartItem.quantity.toString()

                    // Load image using Glide
                    Glide.with(root.context)
                        .load(cartItem.imageResId)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(productImage)

                    incrementButton.setOnClickListener {
                        cartItem.quantity++
                        quantityText.text = cartItem.quantity.toString()
                        onQuantityChange(position, cartItem.quantity)
                        updateQuantityInFirestore(cartItem, cartItem.quantity)
                        notifyItemChanged(position)
                    }

                    decrementButton.setOnClickListener {
                        if (cartItem.quantity > 1) {
                            cartItem.quantity--
                            quantityText.text = cartItem.quantity.toString()
                            onQuantityChange(position, cartItem.quantity)
                            updateQuantityInFirestore(cartItem, cartItem.quantity)
                            notifyItemChanged(position)
                        }
                    }

                    removeButton.setOnClickListener {
                        onRemoveFromCart(position)
                        deleteItemFromFirestore(cartItem)
                    }
                }
            }
            is CheckoutViewHolder -> {
                with(holder.binding) {
                    itemName.text = cartItem.name
                    val totalPrice = cartItem.getPriceAsDouble() * cartItem.quantity
                    itemPrice.text = "R%.2f".format(totalPrice)
                }
            }
        }
    }

    // Update quantity in Firestore
    private fun updateQuantityInFirestore(cartItem: CartItem, newQuantity: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val cartRef = FirebaseFirestore.getInstance().collection("cart")
                .document(userId)
                .collection("items")
                .document(cartItem.name)

            cartRef.update("quantity", newQuantity)
                .addOnSuccessListener {
                    Log.d("CartAdapter", "Quantity updated in Firestore for ${cartItem.name}")
                }
                .addOnFailureListener { e ->
                    Log.e("CartAdapter", "Error updating quantity in Firestore", e)
                }
        }
    }

    // Remove item from Firestore
    private fun deleteItemFromFirestore(cartItem: CartItem) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val cartRef = FirebaseFirestore.getInstance().collection("cart")
                .document(userId)
                .collection("items")
                .document(cartItem.name)

            cartRef.delete()
                .addOnSuccessListener {
                    Log.d("CartAdapter", "Item deleted from Firestore: ${cartItem.name}")
                }
                .addOnFailureListener { e ->
                    Log.e("CartAdapter", "Error deleting item from Firestore", e)
                }
        }
    }

    override fun getItemCount(): Int = cartItems.size
}
