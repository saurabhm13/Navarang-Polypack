package com.plasticbag.plasticbaguser.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.plasticbag.plasticbaguser.databinding.FragmentCartBinding
import com.plasticbag.plasticbaguser.model.OrderDetails
import com.plasticbag.plasticbaguser.model.ProductDetails
import com.plasticbag.plasticbaguser.model.UserDetails
import com.plasticbag.plasticbaguser.presentation.adapter.CartAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: MainViewModel by viewModels()

    private var productList = mutableListOf<ProductDetails>()
    private lateinit var userDetails: UserDetails

    private lateinit var customDialog: CustomDialog
    private lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)

        viewModel.getUserDetails()
        viewModel.userDetailsLiveData.observe(viewLifecycleOwner) {
            userDetails = it
        }

        date = viewModel.getCurrentDate()

        viewModel.getCartProducts()
        prepareRecyclerView()

        binding.btnPlaceOrder.setOnClickListener {
            if (userDetails.userVerified) {

                if (productList.size > 0) {
                    for(product in productList) {
                        val order = OrderDetails("", date, "", userDetails, product)
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.getProductQuantity1(product.productId) { q ->
                                viewModel.addPendingOrder(order, q)
                            }
                        }
                    }
                }else {
                    Toast.makeText(activity, "Add Products", Toast.LENGTH_SHORT).show()
                }

            }else {
                Toast.makeText(activity, "Not Verified!!", Toast.LENGTH_SHORT).show()
            }

        }

        viewModel.orderPlacedCallBack = {
            Toast.makeText(activity, "Order Placed", Toast.LENGTH_SHORT).show()
        }

        viewModel.errorCallBack = {
            Toast.makeText(activity, "Error: $it", Toast.LENGTH_SHORT).show()
        }

        viewModel.outOfQuantityCallBack = {
            Toast.makeText(activity, "Out of Order", Toast.LENGTH_SHORT).show()
        }

        viewModel.minimumQuantityCallBack = {
            Toast.makeText(activity, "Minimum quantity is 30Kg", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun prepareRecyclerView() {
        val cartAdapter = CartAdapter(
            onAddQuantityClick = {product ->
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getProductQuantity1(product.productId) { q ->
                        viewModel.addQuantity(product, q)
                    }
                }
            },
            onMinusQuantityClick = {
                viewModel.minusQuantity(it)
            },
            onQuantityClick = {
//                customDialog.show()
//                cd.show()
                showCustomDialog(it)
            }
        )

        binding.cartRv.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
        }

        viewModel.cartLiveData.observe(viewLifecycleOwner) {
            cartAdapter.setProductList(it)
            productList.clear()
            productList.addAll(it)
        }

    }

    private fun showCustomDialog(productDetails: ProductDetails) {
        val customDialog = CustomDialog(requireContext(), productDetails)
        customDialog.show()
    }

}