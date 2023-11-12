package com.plasticbag.plasticbaguser.presentation.add_edit_address

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.plasticbag.plasticbaguser.util.Constants.Companion.USERS
import com.plasticbag.plasticbaguser.util.Constants.Companion.USER_DETAILS

class AddEditAddressViewModel(): ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser?.uid

    var successCallback: (() -> Unit)? = null
    var errorCallback: (() -> Unit)? = null

    fun saveAddress(address: String) {
        if (currentUser != null) {
            database.child(USERS).child(currentUser).child(USER_DETAILS).child("address").setValue(address)
                .addOnSuccessListener {
                    successCallback?.invoke()
                }
                .addOnFailureListener {
                    errorCallback?.invoke()
                }
        }
    }

}