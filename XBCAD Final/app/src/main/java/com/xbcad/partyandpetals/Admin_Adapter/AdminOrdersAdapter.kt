package com.xbcad.partyandpetals.Admin_Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xbcad.partyandpetals.Admin_data.Order
import com.xbcad.partyandpetals.R

class AdminOrdersAdapter(
    private val orders: List<Order>,
) : RecyclerView.Adapter<AdminOrdersAdapter.AdminOrdersViewHolder>() {

    class AdminOrdersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderNumber: TextView = view.findViewById(R.id.orderNumber)
        val pickupLocation: TextView = view.findViewById(R.id.pickupLocation)
        val pickupTime: TextView = view.findViewById(R.id.pickupTime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrdersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_order, parent, false)
        return AdminOrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminOrdersViewHolder, position: Int) {
        val order = orders[position]
        holder.orderNumber.text = "Order Number: ${order.orderNumber}"
        holder.pickupLocation.text = "Pickup Location: ${order.pickupLocation}"
        holder.pickupTime.text = "Pickup Time: ${order.pickupTime}"
       }


    override fun getItemCount(): Int = orders.size
}
