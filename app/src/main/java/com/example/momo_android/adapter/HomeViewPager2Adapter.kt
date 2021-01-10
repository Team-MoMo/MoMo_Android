package com.example.momo_android.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.momo_android.ui.HomeFragment
import com.example.momo_android.ui.ScrollFragment


class HomeViewPager2Adapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> ScrollFragment()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}
