package com.example.momo_android.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class OnboardingAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    var fragments= listOf<Fragment>()

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }
    override fun getCount():Int=fragments.size

}