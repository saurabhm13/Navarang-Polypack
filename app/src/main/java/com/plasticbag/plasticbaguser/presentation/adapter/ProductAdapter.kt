package com.plasticbag.plasticbaguser.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.plasticbag.plasticbaguser.databinding.ProductListItemBinding
import com.plasticbag.plasticbaguser.model.ProductDetails

class ProductAdapter(
    private val onAddToCartClick: (ProductDetails) -> Unit
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var productList = ArrayList<ProductDetails>()
    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(productList: List<ProductDetails>) {
        this.productList.clear()
        this.productList.addAll(productList)
        notifyDataSetChanged()
    }

    class ProductViewHolder(val binding: ProductListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ProductListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        holder.binding.productTitle.text = productList[position].title
        holder.binding.productQuantity.text = productList[position].quantity+" kg"

        holder.binding.addToCart.setOnClickListener {
            onAddToCartClick.invoke(productList[position])
        }
    }


}