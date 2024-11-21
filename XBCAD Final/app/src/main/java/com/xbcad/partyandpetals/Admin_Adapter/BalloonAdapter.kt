package com.xbcad.partyandpetals.Admin_Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xbcad.partyandpetals.Admin_data.Balloon
import com.xbcad.partyandpetals.R
import com.google.firebase.firestore.FirebaseFirestore


class BalloonAdapter(
    private val balloonList: MutableList<Balloon>,
    private val onEditClick: (Balloon) -> Unit,
    private val onDeleteClick: (Balloon) -> Unit
) : RecyclerView.Adapter<BalloonAdapter.BalloonViewHolder>() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    class BalloonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.balloonName)
        val priceTextView: TextView = itemView.findViewById(R.id.balloonPrice)
        val imageView: ImageView = itemView.findViewById(R.id.balloonImage)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteBalloon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalloonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_balloon, parent, false)
        return BalloonViewHolder(view)
    }

    override fun onBindViewHolder(holder: BalloonViewHolder, position: Int) {
        val balloon = balloonList[position]
        holder.nameTextView.text = balloon.name

        // Convert price from String to Double and format it to 2 decimal places
        val priceValue = balloon.price.toDoubleOrNull() ?: 0.0 // If conversion fails, default to 0.0
        val formattedPrice = String.format("%.2f", priceValue) // Format to 2 decimal places
        holder.priceTextView.text = "Price: R $formattedPrice"

        // Using  for Glide loading
        val imageUrl = balloon.imageResId  // This should be the image URL saved in Firestore

        Log.d("BalloonAdapter", "Attempting to load image from URL: $imageUrl")

        // Fetch and load the image URL from Firebase Storage or the passed URL
        Glide.with(holder.itemView.context)
            .load(imageUrl)  // Use image URL directly
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            Log.d("BalloonAdapter", "Edit clicked for balloon: ${balloon.name}")
            onEditClick(balloon)
        }

        holder.deleteButton.setOnClickListener {
            Log.d("BalloonAdapter", "Delete clicked for balloon: ${balloon.name}")
            deleteBalloon(balloon)
        }
    }




    override fun getItemCount() = balloonList.size

    private fun deleteBalloon(balloon: Balloon) {
        Log.d("BalloonAdapter", "Attempting to delete balloon: ${balloon.name}")
        // Query to find the balloon document by name
        db.collection("products")
            .whereEqualTo("name", balloon.name)
            .get()
            .addOnSuccessListener { documents ->
                Log.d(
                    "BalloonAdapter",
                    "Successfully fetched documents for balloon: ${balloon.name}"
                )
                for (document in documents) {
                    Log.d("BalloonAdapter", "Deleting document ID: ${document.id}")
                    // Delete the document
                    document.reference.delete()
                        .addOnSuccessListener {
                            Log.d("BalloonAdapter", "Balloon successfully deleted: ${balloon.name}")
                            balloonList.remove(balloon) // Remove from local list
                            notifyDataSetChanged() // Notify the adapter about data change
                        }
                        .addOnFailureListener { e ->
                            Log.e("BalloonAdapter", "Error deleting balloon: ${balloon.name}", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("BalloonAdapter", "Error getting documents for balloon: ${balloon.name}", e)
            }
    }
}

