package com.example.momo_android.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.momo_android.databinding.ActivityOnboardingBinding
import com.example.momo_android.databinding.FragmentOnboardingDepthBinding
import com.example.momo_android.databinding.FragmentOnboardingStartBinding


class OnboardingDepthFragment : Fragment() {
    private var _Binding: FragmentOnboardingDepthBinding? = null
    private val Binding get() = _Binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _Binding = FragmentOnboardingDepthBinding.inflate(layoutInflater)
        return Binding.root
        //return inflater.inflate(R.layout.bottomsheet_custom, container, false)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }
}