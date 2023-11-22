package com.plasticbag.plasticbaguser.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.plasticbag.plasticbaguser.databinding.CartListItemBinding
import com.plasticbag.plasticbaguser.databinding.ProductListItemBinding
import com.plasticbag.plasticbaguser.model.ProductDetails

class CartAdapter(
    private val onAddQuantityClick: (ProductDetails) -> Unit,
    private val onMinusQuantityClick: (ProductDetails) -> Unit,
    private val onQuantityClick: (ProductDetails) -> Unit
): RecyclerView.Adapter<CartAdapter.ProductViewHolder>() {

    private var productList = ArrayList<ProductDetails>()
    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(productList: List<ProductDetails>) {
        this.productList.clear()
        this.productList.addAll(productList)
        notifyDataSetChanged()
    }

    class ProductViewHolder(val binding: CartListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(CartListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        holder.binding.title.text = productList[position].title
        holder.binding.quantity.text = (productList[position].quantity) + " kg"

        holder.binding.addQuantity.setOnClickListener {
            onAddQuantityClick.invoke(productList[position])
        }

        holder.binding.minusQuantity.setOnClickListener {
            onMinusQuantityClick.invoke(productList[position])
        }

        holder.binding.quantity.setOnClickListener {
            onQuantityClick.invoke(productList[position])
        }
    }

}