package com.xbcad.partyandpetals.Admin_data

data class Order(
    val userId: String = "",
    val orderNumber: String = "",
    val pickupLocation: String = "",
    val pickupTime: String = "",
    val timestamp: Long = 0L,
    val totalAmount: Double = 0.0
)