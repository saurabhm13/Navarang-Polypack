package com.plasticbag.plasticbaguser.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.plasticbag.plasticbaguser.model.OrderDetails
import com.plasticbag.plasticbaguser.model.ProductDetails
import com.plasticbag.plasticbaguser.model.UserDetails
import com.plasticbag.plasticbaguser.model.UserOrderDetails
import com.plasticbag.plasticbaguser.util.Constants.Companion.ADMIN
import com.plasticbag.plasticbaguser.util.Constants.Companion.CART
import com.plasticbag.plasticbaguser.util.Constants.Companion.ORDERS
import com.plasticbag.plasticbaguser.util.Constants.Companion.PENDING
import com.plasticbag.plasticbaguser.util.Constants.Companion.PRODUCTS
import com.plasticbag.plasticbaguser.util.Constants.Companion.QUANTITY
import com.plasticbag.plasticbaguser.util.Constants.Companion.USERS
import com.plasticbag.plasticbaguser.util.Constants.Companion.USER_DETAILS
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel() : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private val currentUserId = auth.currentUser?.uid

    private var _productLiveData = MutableLiveData<List<ProductDetails>>()
    val productLiveData: LiveData<List<ProductDetails>> get() = _productLiveData

    private var _cartLiveData = MutableLiveData<List<ProductDetails>>()
    val cartLiveData: LiveData<List<ProductDetails>> get() = _cartLiveData

    private var _userDetailsLiveData = MutableLiveData<UserDetails>()
    val userDetailsLiveData: LiveData<UserDetails> get() = _userDetailsLiveData

    private var _productQuantityLiveData = MutableLiveData<String>()
    val productQuantityLiveData: LiveData<String> get() = _productQuantityLiveData

    var addToCartCallBack: (() -> Unit)? = null
    var errorCallBack: ((String) -> Unit)? = null
    var orderPlacedCallBack: (() -> Unit)? = null
    var outOfQuantityCallBack: (() -> Unit)? = null
    var minimumQuantityCallBack: (() -> Unit)? = null


    fun getProducts() {
        database.child(PRODUCTS).orderByChild("title").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductDetails>()

                for (dataSnapshot in snapshot.children) {

                    val product = dataSnapshot.getValue(ProductDetails::class.java)
                    product?.let {
                        productList.add(product)
                    }
                }

                _productLiveData.value = productList
            }

            override fun onCancelled(error: DatabaseError) {
                errorCallBack?.invoke(error.message)
            }

        })
    }

    fun getCartProducts() {
        if (currentUserId != null) {
            database.child(USERS).child(currentUserId).child(CART)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val cartList = mutableListOf<ProductDetails>()

                        for (dataSnapshot in snapshot.children) {
                            val cartItem = dataSnapshot.getValue(ProductDetails::class.java)
                            cartItem?.let {
                                cartList.add(cartItem)
                            }
                        }

                        _cartLiveData.value = cartList
                    }

                    override fun onCancelled(error: DatabaseError) {
                        errorCallBack?.invoke(error.message)
                    }

                })
        }
    }

    fun getUserDetails() {
        if (currentUserId != null) {
            database.child(USERS).child(currentUserId).child(USER_DETAILS)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userData = snapshot.getValue(UserDetails::class.java)!!
                        _userDetailsLiveData.value = userData
                    }

                    override fun onCancelled(error: DatabaseError) {
                        errorCallBack?.invoke(error.message)
                    }

                })
        }
    }

    fun addProductToCart(product: ProductDetails) {

        val cartProducts = ProductDetails(product.productId, product.title, "30")

        if (currentUserId != null) {
            database.child(USERS).child(currentUserId).child(CART).child(product.productId)
                .setValue(cartProducts)
                .addOnSuccessListener {
                    addToCartCallBack?.invoke()
                }
                .addOnFailureListener {
                    it.message?.let { it1 -> errorCallBack?.invoke(it1) }
                }
        }
    }

    fun addQuantity(product: ProductDetails, productQuantity: String) {
        if (productQuantity != "null") {
            val newQuantity = product.quantity.toInt() + 1
            if (currentUserId != null && productQuantity.toInt() >= newQuantity) {
                database.child(USERS).child(currentUserId).child(CART).child(product.productId)
                    .child(QUANTITY).setValue(newQuantity.toString())
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener {
                        it.message?.let { it1 -> errorCallBack?.invoke(it1) }
                    }
            }
        }else {
            errorCallBack?.invoke("Out of Order")
        }
    }

    fun minusQuantity(product: ProductDetails) {
        val newQuantity = product.quantity.toInt() - 1
        if (newQuantity > 0) {
            if (currentUserId != null) {
                database.child(USERS).child(currentUserId).child(CART).child(product.productId)
                    .child(QUANTITY).setValue(newQuantity.toString())
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener {
                        it.message?.let { it1 -> errorCallBack?.invoke(it1) }
                    }
            }
        } else {
            if (currentUserId != null) {
                database.child(USERS).child(currentUserId).child(CART).child(product.productId)
                    .removeValue()
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener {
                        it.message?.let { it1 -> errorCallBack?.invoke(it1) }
                    }
            }
        }
    }

    fun editQuantity(product: ProductDetails, newQuantity: String) {

        if (currentUserId != null) {
            database.child(USERS).child(currentUserId).child(CART).child(product.productId)
                .child(QUANTITY).setValue(newQuantity)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                    it.message?.let { it1 -> errorCallBack?.invoke(it1) }
                }
        }
    }

    fun addPendingOrder(order: OrderDetails, totalQuantity: String) {
        var remainingQuantity = -1
        if (totalQuantity != "null") {
            remainingQuantity = totalQuantity.toInt() - order.productDetails?.quantity?.toInt()!!
        }

        if (remainingQuantity >= 0) {
            if (order.productDetails?.quantity?.toInt()!! >= 30) {
                if (currentUserId != null) {
                    order.productDetails.let {

                        val orderChildRef = database.child(USERS).child(currentUserId).child(ORDERS).child(PENDING).push()
                        val orderKey = orderChildRef.key

                        val newOrder = OrderDetails(orderKey, order.orderDateTime, order.dispatchDateTime, order.userDetails, order.productDetails)
                        val userOrderDetails = UserOrderDetails(orderKey, order.orderDateTime, order.dispatchDateTime,
                        order.productDetails.productId, order.productDetails.title, order.productDetails.quantity)

                        orderChildRef.child(it.productId).setValue(userOrderDetails)
                            .addOnSuccessListener {
                                database.child(PRODUCTS).child(order.productDetails.productId).child(
                                    QUANTITY).setValue(remainingQuantity.toString())
                                orderPlacedCallBack?.invoke()
                            }
                            .addOnFailureListener {
                                it.message?.let { it1 -> errorCallBack?.invoke(it1) }
                            }

                        database.child(USERS).child(currentUserId).child(CART).child(it.productId).removeValue()

                        if (orderKey != null) {
                            database.child(ADMIN).child(ORDERS).child(PENDING).child(orderKey).child(it.productId).setValue(newOrder)
                                .addOnSuccessListener {
                                }
                                .addOnFailureListener {
                                }
                        }
                    }
                }
            }else {
                minimumQuantityCallBack?.invoke()
            }

        }else {
            outOfQuantityCallBack?.invoke()
        }


    }

    fun getProductQuantity(productId: String): String {
        var quantity: String = ""
        database.child(PRODUCTS).child(productId).child(QUANTITY).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                quantity = snapshot.value.toString()
                _productQuantityLiveData.value = quantity
            }

            override fun onCancelled(error: DatabaseError) {
                errorCallBack?.invoke(error.message)
            }

        })

        return quantity
    }

    suspend fun getProductQuantity1(productId: String, callback: (String) -> Unit) = coroutineScope {
        var quantity: String = ""
        val job = async {
            database.child(PRODUCTS).child(productId).child(QUANTITY).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    quantity = snapshot.value.toString()
                    _productQuantityLiveData.value = quantity
                    callback(quantity)
                }

                override fun onCancelled(error: DatabaseError) {
                    quantity = "0"
                    callback(quantity)
                }

            })

        }
        job.await()
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

}