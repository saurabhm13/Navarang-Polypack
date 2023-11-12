package com.plasticbag.plasticbaguser.presentation.edit_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.plasticbag.plasticbaguser.model.UserDetails
import com.plasticbag.plasticbaguser.util.Constants.Companion.EMAIL
import com.plasticbag.plasticbaguser.util.Constants.Companion.IMAGE
import com.plasticbag.plasticbaguser.util.Constants.Companion.NAME
import com.plasticbag.plasticbaguser.util.Constants.Companion.PHONE_NO
import com.plasticbag.plasticbaguser.util.Constants.Companion.USERS
import com.plasticbag.plasticbaguser.util.Constants.Companion.USER_DETAILS
import java.util.UUID

class EditProfileViewModel(): ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference
    private val storageReference = FirebaseStorage.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private val currentUserID = auth.currentUser?.uid

    var successCallback: (() -> Unit)? = null
    var errorCallback: (() -> Unit)? = null

    fun updateUserData(name: String, image: Uri, email: String, phoneNo: String) {
        val imageUri: Uri = image
        val imageRef = storageReference.child("users/images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully
            // Now, get the download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // The 'uri' variable now contains the download URL of the uploaded image
                val imageUrl = uri.toString()

                if (currentUserID != null) {
                    database.child(USERS).child(currentUserID).child(USER_DETAILS).child(NAME).setValue(name)
                    database.child(USERS).child(currentUserID).child(USER_DETAILS).child(EMAIL).setValue(email)
                    database.child(USERS).child(currentUserID).child(USER_DETAILS).child(PHONE_NO).setValue(phoneNo)
                    database.child(USERS).child(currentUserID).child(USER_DETAILS).child(IMAGE).setValue(imageUrl)
                }
                successCallback?.invoke()

            }.addOnFailureListener { exception ->
                // Handle the failure to get the download URL
                errorCallback?.invoke()
            }
        }.addOnFailureListener { exception ->
            // Handle the failure to upload the image
            errorCallback?.invoke()
        }
    }

    fun updateTextUserData(name: String, email: String, phoneNo: String) {
        currentUserID?.let {
            database.child(USERS).child(currentUserID).child(USER_DETAILS).child("name").setValue(name)
            database.child(USERS).child(currentUserID).child(USER_DETAILS).child("email").setValue(email)
            database.child(USERS).child(currentUserID).child(USER_DETAILS).child("phoneNo").setValue(phoneNo)
                .addOnSuccessListener {
                    successCallback?.invoke()
                }
                .addOnFailureListener {
                    errorCallback?.invoke()
                }
        }

    }

}