package com.example.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.fragment.CartFragment
import com.example.myapplication.fragment.OrderFragment
import com.example.myapplication.fragment.HomeFragment
import com.example.myapplication.fragment.UserFragment
//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> OrderFragment()
            2 -> CartFragment()
            3 -> UserFragment()
            else -> HomeFragment()
        }
    }


}