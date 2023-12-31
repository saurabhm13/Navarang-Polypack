package com.plasticbag.plasticbaguser.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plasticbag.plasticbaguser.databinding.OrderListItemBinding
import com.plasticbag.plasticbaguser.model.UserOrderDetails

class OrderAdapter(
    private val onDeleteDispatchOrderClick: ((UserOrderDetails) -> Unit)
): RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var orderList = ArrayList<UserOrderDetails>()
    @SuppressLint("NotifyDataSetChanged")
    fun setOrderList(orderList: List<UserOrderDetails>) {
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

        holder.binding.orderDate.text = orderList[position].orderDateTime

        if (orderList[position].dispatchDateTime != "") {
            holder.binding.dispatchDate.visibility = View.VISIBLE
            holder.binding.dispatchDate.text = orderList[position].dispatchDateTime
            holder.binding.pointViewDD.visibility = View.VISIBLE
            holder.binding.horViewDD.visibility = View.VISIBLE
            holder.binding.deleteDispatchOrder.visibility = View.VISIBLE
        }

        holder.binding.deleteDispatchOrder.setOnClickListener {
            onDeleteDispatchOrderClick.invoke(orderList[position])
        }

    }


}