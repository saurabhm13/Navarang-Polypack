package com.plasticbag.plasticbaguser.presentation.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.plasticbag.plasticbaguser.model.ProductDetails
import com.plasticbag.plasticbaguser.util.Constants.Companion.DELIVERED
import com.plasticbag.plasticbaguser.util.Constants.Companion.DISPATCH
import com.plasticbag.plasticbaguser.util.Constants.Companion.ORDERS
import com.plasticbag.plasticbaguser.util.Constants.Companion.PENDING
import com.plasticbag.plasticbaguser.util.Constants.Companion.USERS

class OrderViewModel(): ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private val currentUserId = auth.currentUser?.uid

    private var _pendingOrderLiveData = MutableLiveData<List<ProductDetails>>()
    val pendingOrderLiveData: LiveData<List<ProductDetails>> get() = _pendingOrderLiveData

    private var _dispatchOrderLiveData = MutableLiveData<List<ProductDetails>>()
    val dispatchOrderLiveData: LiveData<List<ProductDetails>> get() = _dispatchOrderLiveData

    private var _deliverOrderLiveData = MutableLiveData<List<ProductDetails>>()
    val deliverOrderLiveData: LiveData<List<ProductDetails>> get() = _deliverOrderLiveData


    fun getPendingOrder() {
        if (currentUserId != null) {
            database.child(USERS).child(currentUserId).child(ORDERS).child(PENDING).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val orderList = mutableListOf<ProductDetails>()

                    for (dataSnapshot in snapshot.children) {
                        val order = dataSnapshot.getValue(ProductDetails::class.java)
                        order?.let {
                            orderList.add(order)
                        }
                    }

                    _pendingOrderLiveData.value = orderList
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    fun getDispatchOrder() {
        if (currentUserId != null) {
            database.child(USERS).child(currentUserId).child(ORDERS).child(DISPATCH).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val orderList = mutableListOf<ProductDetails>()

                    for (dataSnapshot in snapshot.children) {
                        val order = dataSnapshot.getValue(ProductDetails::class.java)
                        order?.let {
                            orderList.add(order)
                        }
                    }

                    _dispatchOrderLiveData.value = orderList
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    fun getDeliveredOrder() {
        if (currentUserId != null) {
            database.child(USERS).child(currentUserId).child(ORDERS).child(DELIVERED).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val orderList = mutableListOf<ProductDetails>()

                    for (dataSnapshot in snapshot.children) {
                        val order = dataSnapshot.getValue(ProductDetails::class.java)
                        order?.let {
                            orderList.add(order)
                        }
                    }

                    _deliverOrderLiveData.value = orderList
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

}