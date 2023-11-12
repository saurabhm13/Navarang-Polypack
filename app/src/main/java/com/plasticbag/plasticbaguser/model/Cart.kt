package com.plasticbag.plasticbaguser.model

data class Cart(
    val image: String,
    val title: String,
    val quantity: String
) {
    constructor(): this("", "", "")
}