package com.xbcad.partyandpetals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xbcad.partyandpetals.Admin_data.Balloon
import com.xbcad.partyandpetals.databinding.ItemBalloonCartBinding

class BalloonCartAdapter(
    private val balloonList: List<Balloon>, // List of balloons to display in the cart
    private val onAddToCart: (Balloon, Int) -> Unit // Callback function for adding balloons to the cart
) : RecyclerView.Adapter<BalloonCartAdapter.BalloonViewHolder>() {

    // ViewHolder class that binds the views in the layout to variables
    inner class BalloonViewHolder(val binding: ItemBalloonCartBinding) :
        RecyclerView.ViewHolder(binding.root) // Using View Binding for better readability and type safety

    // Method to create and inflate the view for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalloonViewHolder {
        val binding = ItemBalloonCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BalloonViewHolder(binding) // Return the ViewHolder instance
    }

    // Method to bind data to the ViewHolder
    override fun onBindViewHolder(holder: BalloonViewHolder, position: Int) {
        val balloon = balloonList[position] // Get the balloon item at the current position
        var quantity = 0 // Initialize quantity for each balloon

        // Use the View Binding instance to access and bind data to the views
        with(holder.binding) {
            // Set the balloon name to the product description TextView
            productDescription.text = balloon.name

            // Safely convert the price to a Double and format it to 2 decimal places
            val priceValue = balloon.price.toDoubleOrNull() ?: 0.0 // Default to 0.0 if conversion fails
            val formattedPrice = String.format("%.2f", priceValue) // Format price to 2 decimal places
            productPrice.text = "Price: R $formattedPrice"

            // Use Glide to load the image from the URL in the balloon object
            Glide.with(productImage.context)
                .load(balloon.imageUrl) // Load image from URL
                .placeholder(android.R.drawable.progress_indeterminate_horizontal) // Placeholder image while loading
                .error(android.R.drawable.stat_notify_error) // Fallback image if loading fails
                .into(productImage) // Load into the ImageView

            // Set up the increment button to increase the quantity
            incrementButton.setOnClickListener {
                quantity++ // Increment the quantity
                quantityText.text = quantity.toString() // Update the quantity TextView
            }

            // Set up the decrement button to decrease the quantity
            decrementButton.setOnClickListener {
                if (quantity > 0) quantity-- // Decrement the quantity only if it's greater than 0
                quantityText.text = quantity.toString() // Update the quantity TextView
            }

            // Handle the add-to-cart button click
            addToCartButton.setOnClickListener {
                onAddToCart(balloon, quantity) // Call the callback function with the selected balloon and quantity
                addToCartButton.text = "Added" // Update button text to indicate the item was added
            }
        }
    }

    // Method to return the total number of items in the RecyclerView
    override fun getItemCount(): Int = balloonList.size
}
