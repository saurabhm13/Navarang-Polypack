package com.plasticbag.plasticbaguser.model

data class OrderDetails(
//    val productId: String,
//    val title: String,
//    val image : String,
//    val quantity: String,
//    val bayerAddress: String,
//    val bayerName: String,
//    val bayerPhoneNo: String,
//    val bayerUserId: String,

    val orderId: String? = null,
    val userDetails: UserDetails? = null,
    val productDetails: ProductDetails? = null

) {
//    constructor(): this("", "", "", "", "", "", "", "")
}
