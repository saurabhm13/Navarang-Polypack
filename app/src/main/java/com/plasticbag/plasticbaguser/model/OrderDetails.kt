package com.plasticbag.plasticbaguser.model

data class OrderDetails(
    val orderId: String? = null,
    val orderDateTime: String? = null,
    val dispatchDateTime: String? = null,
    val userDetails: UserDetails? = null,
    val productDetails: ProductDetails? = null

) {
//    constructor(): this("", "", "", "", "", "", "", "")
}
