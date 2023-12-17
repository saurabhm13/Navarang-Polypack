package com.plasticbag.plasticbaguser.presentation.login_signup

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.plasticbag.plasticbaguser.model.UserDetails
import com.plasticbag.plasticbaguser.util.Constants.Companion.ADMIN
import com.plasticbag.plasticbaguser.util.Constants.Companion.NOT_VERIFIED
import com.plasticbag.plasticbaguser.util.Constants.Companion.USER_LOGIN
import com.plasticbag.plasticbaguser.util.Constants.Companion.USERS
import com.plasticbag.plasticbaguser.util.Constants.Companion.USER_DETAILS
import java.util.concurrent.TimeUnit

class LoginSignupViewModel(): ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private var verificationId: String? = null

    private val _verificationCodeSent = MutableLiveData<Boolean>()
    val verificationCodeSent: LiveData<Boolean> get() = _verificationCodeSent

    private val _verificationError = MutableLiveData<String>()
    val verificationError: LiveData<String> get() = _verificationError

    var authCallback: (() -> Unit)? = null
    var errorCallback: ((String) -> Unit)? = null
    var resetEmailSendCallback: (() -> Unit)? = null

    // Register user by email and password and also save user data in realtime database
    fun registerUser(email: String, password: String, username: String, phoneNo: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        val user = UserDetails(username, email, phoneNo, userId, "", "", false)
                        database.child(USERS).child(userId).child(USER_DETAILS).setValue(user)
                        database.child(ADMIN).child(USER_LOGIN).child(NOT_VERIFIED).child(userId).setValue(user)
                    }
                    authCallback?.invoke()
                }
            }
            .addOnFailureListener {
                errorCallback?.invoke(it.message.toString())
            }
    }

    // User Login using email and password
    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authCallback?.invoke()
                }
            }
            .addOnFailureListener {
                errorCallback?.invoke(it.message.toString())
            }
    }

    // Saving user data to realtime database
    fun saveUserToDatabase(name: String, email: String, phoneNo: String) {
        val adminId = auth.currentUser?.uid
        adminId?.let {
            val user = UserDetails(name, email, phoneNo, adminId, "", "", false)
            database.child("users").child(adminId).setValue(user).addOnCompleteListener {
                authCallback?.invoke()
            }
        }
    }

    // Phone Number registration
    fun sendVerificationCode(phoneNo: String, countryCode: String, activity: Activity) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("$countryCode$phoneNo")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallBack)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        _verificationCodeSent.value = true
    }

    fun verifyCode(otp: String) {
        val credential = verificationId?.let { PhoneAuthProvider.getCredential(it, otp) }

        if (credential != null) {
            signInWithCredential(credential)
        }
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode

                if (code != null) {
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _verificationError.value = e.message
            }
        }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authCallback?.invoke()
                } else {
                    _verificationError.value = task.exception?.message
                }
            }
    }

    fun resetPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resetEmailSendCallback?.invoke()
                }
            }
            .addOnFailureListener {
                errorCallback?.invoke(it.message.toString())
            }
    }

}