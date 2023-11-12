package com.plasticbag.plasticbaguser.presentation.orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.plasticbag.plasticbaguser.databinding.ActivityOrderBinding
import com.plasticbag.plasticbaguser.presentation.adapter.ViewPagerAdapter

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when (position){
                0 -> tab.text = "Pending"
                1 -> tab.text = "Dispatched"
                2 -> tab.text = "Delivered"
            }
        }.attach()

        binding.back.setOnClickListener {
            finish()
        }

    }
}