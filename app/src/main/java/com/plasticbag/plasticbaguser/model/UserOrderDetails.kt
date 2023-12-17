package com.plasticbag.plasticbaguser.model

data class UserOrderDetails(
    val orderId: String? = null,
    val orderDateTime: String? = null,
    val dispatchDateTime: String? = null,
    val productId: String? = null,
    val title: String? = null,
    val quantity: String? = null,
) {
    constructor(): this("", "", "", "", "", "")
}