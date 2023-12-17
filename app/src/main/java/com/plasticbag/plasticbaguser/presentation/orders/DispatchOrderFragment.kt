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
import com.plasticbag.plasticbaguser.databinding.FragmentDeliveredOrderBinding
import com.plasticbag.plasticbaguser.databinding.FragmentDispatchOrderBinding
import com.plasticbag.plasticbaguser.databinding.FragmentPendingOrderBinding
import com.plasticbag.plasticbaguser.presentation.adapter.OrderAdapter

class DispatchOrderFragment : Fragment() {

    private lateinit var binding: FragmentDispatchOrderBinding
    private val viewModel: OrderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDispatchOrderBinding.inflate(layoutInflater, container, false)

        viewModel.getDispatchOrder()
        prepareRecyclerView()

        viewModel.errorCallBack = {
            Toast.makeText(activity, "Error: $it", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun prepareRecyclerView() {
        val orderAdapter = OrderAdapter(
            onDeleteDispatchOrderClick = {
                viewModel.deleteDispatchOrder(it)
            }
        )

        binding.dispatchOrder.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = orderAdapter
        }

        viewModel.dispatchOrderLiveData.observe(viewLifecycleOwner) {
            orderAdapter.setOrderList(it)
        }
    }
}