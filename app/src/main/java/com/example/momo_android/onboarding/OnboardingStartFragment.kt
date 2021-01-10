package com.example.momo_android.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.momo_android.databinding.FragmentOnboardingStartBinding
import com.example.momo_android.ui.OnboardingActivity


class OnboardingStartFragment : Fragment() {
    private var _Binding: FragmentOnboardingStartBinding? = null
    private val Binding get() = _Binding!!
    var onboardingActivity: OnboardingActivity?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _Binding = FragmentOnboardingStartBinding.inflate(layoutInflater)

        Binding.btnStart.setOnClickListener {
            //다음 프래그먼트로 넘어가기.
            onboardingActivity?.feelingFragment()
        }

        return Binding.root
        //return inflater.inflate(R.layout.bottomsheet_custom, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onboardingActivity=context as OnboardingActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }




}