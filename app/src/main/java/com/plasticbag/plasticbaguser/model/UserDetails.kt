package com.plasticbag.plasticbaguser.model

data class UserDetails(
    val name: String,
    val email: String,
    val phoneNo: String,
    val userId: String,
    val address: String,
    val image: String,
    val userVerified: Boolean = false
) {
    constructor(): this ("", "", "", "", "", "")
}
