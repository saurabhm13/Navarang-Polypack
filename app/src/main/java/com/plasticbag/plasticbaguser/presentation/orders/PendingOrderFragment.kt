package com.plasticbag.plasticbaguser.presentation.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.plasticbag.plasticbaguser.R
import com.plasticbag.plasticbaguser.databinding.FragmentPendingOrderBinding
import com.plasticbag.plasticbaguser.presentation.adapter.OrderAdapter

class PendingOrderFragment : Fragment() {

    private lateinit var binding: FragmentPendingOrderBinding
    private val viewModel: OrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPendingOrderBinding.inflate(layoutInflater, container, false)

        viewModel.getPendingOrder()
        prepareRecyclerView()

        viewModel.errorCallBack = {
            Toast.makeText(activity, "Error: $it", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun prepareRecyclerView() {
        val orderAdapter = OrderAdapter(
            onDeleteDispatchOrderClick = {

            }
        )

        binding.pendingOrders.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = orderAdapter
        }

        viewModel.pendingOrderLiveData.observe(viewLifecycleOwner) {
            orderAdapter.setOrderList(it)
        }
    }

}