package com.xbcad.partyandpetals.Admin_data

data class CartItem(
    val name: String = "",
    val price: String = "0.0",
    var quantity: Int = 1,
    val imageResId: String = ""
) {
    // Convert the price to a Double
    fun getPriceAsDouble(): Double {
        return price.toDoubleOrNull() ?: 0.0
    }
}

