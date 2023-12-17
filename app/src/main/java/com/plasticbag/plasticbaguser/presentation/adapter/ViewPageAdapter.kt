package com.plasticbag.plasticbaguser.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.plasticbag.plasticbaguser.presentation.orders.DeliveredOrderFragment
import com.plasticbag.plasticbaguser.presentation.orders.DispatchOrderFragment
import com.plasticbag.plasticbaguser.presentation.orders.OrderActivity
import com.plasticbag.plasticbaguser.presentation.orders.PendingOrderFragment

class ViewPagerAdapter(activity: OrderActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PendingOrderFragment()
            1 -> DispatchOrderFragment()
            else -> PendingOrderFragment()
        }
    }

}