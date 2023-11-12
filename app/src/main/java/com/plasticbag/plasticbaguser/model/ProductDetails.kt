package com.plasticbag.plasticbaguser.model

data class ProductDetails(
    val productId: String,
    val image: String,
    val title: String,
    val quantity: String,
) {
    constructor(): this("", "", "", "")
}
