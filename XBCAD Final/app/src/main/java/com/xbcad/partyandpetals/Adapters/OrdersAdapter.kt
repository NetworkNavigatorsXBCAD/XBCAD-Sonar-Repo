package com.xbcad.partyandpetals.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xbcad.partyandpetals.Admin_data.Order
import com.xbcad.partyandpetals.R

// Adapter class for displaying a list of orders in a RecyclerView
class OrdersAdapter(private val orders: List<Order>) :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    // ViewHolder class to hold references to the views for each item in the list
    class OrdersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // TextView to display the order number
        val orderNumber: TextView = view.findViewById(R.id.orderNumber)
        // TextView to display the pickup location of the order
        val pickupLocation: TextView = view.findViewById(R.id.pickupLocation)
        // TextView to display the pickup time of the order
        val pickupTime: TextView = view.findViewById(R.id.pickupTime)
        // TextView to display the total amount for the order
        val totalAmount: TextView = view.findViewById(R.id.totalAmount)
    }

    // Creates and inflates the view for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        // Inflate the layout for an individual order item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrdersViewHolder(view) // Return the ViewHolder instance
    }

    // Binds the data to the views for each item in the RecyclerView
    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        // Get the current order from the list
        val order = orders[position]

        // Bind the order data to the respective views in the ViewHolder
        holder.orderNumber.text = "Order Number: ${order.orderNumber}"
        holder.pickupLocation.text = "Pickup Location: ${order.pickupLocation}"
        holder.pickupTime.text = "Pickup Time: ${order.pickupTime}"
        // Format the total amount to two decimal places and bind it to the view
        holder.totalAmount.text = "Total: R${String.format("%.2f", order.totalAmount)}"
    }

    // Returns the total number of items in the list
    override fun getItemCount(): Int {
        return orders.size
    }
}
