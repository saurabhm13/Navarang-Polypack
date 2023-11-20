package com.plasticbag.plasticbaguser.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.plasticbag.plasticbaguser.databinding.OrderListItemBinding
import com.plasticbag.plasticbaguser.model.ProductDetails

class OrderAdapter(): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var orderList = ArrayList<ProductDetails>()
    @SuppressLint("NotifyDataSetChanged")
    fun setOrderList(orderList: List<ProductDetails>) {
        this.orderList.clear()
        this.orderList.addAll(orderList)
        notifyDataSetChanged()
    }

    class OrderViewHolder(val binding: OrderListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(OrderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        holder.binding.title.text = orderList[position].title
        holder.binding.quantity.text = orderList[position].quantity+" kg"
    }


}