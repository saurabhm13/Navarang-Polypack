package com.plasticbag.plasticbaguser.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.plasticbag.plasticbaguser.databinding.FragmentProductBinding
import com.plasticbag.plasticbaguser.presentation.adapter.ProductAdapter

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(layoutInflater, container, false)

        viewModel.getProducts()
        prepareProductRecyclerView()
        
        viewModel.addToCartCallBack = {
            Toast.makeText(activity, "Added to Cart", Toast.LENGTH_SHORT).show()
        }
        
        viewModel.errorCallBack = {
            Toast.makeText(activity, "Error Occurred, Try Again!!", Toast.LENGTH_SHORT).show()
        }

        viewModel.orderPlacedCallBack = {
            Toast.makeText(activity, "Order Placed", Toast.LENGTH_LONG).show()
        }

        return binding.root
    }

    private fun prepareProductRecyclerView() {
        val productAdapter = ProductAdapter(
            onAddToCartClick = {product ->
                viewModel.getProductQuantity(product.productId)
                viewModel.productQuantityLiveData.observe(viewLifecycleOwner) {quantity ->
                    if (quantity.toInt() > 0) {

                        viewModel.addProductToCart(product)
                    }
                }
            }
        )

        binding.productRv.apply {
            layoutManager = LinearLayoutManager(activity,  LinearLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }

        viewModel.productLiveData.observe(viewLifecycleOwner) {
            productAdapter.setProductList(it)
        }
    }

}