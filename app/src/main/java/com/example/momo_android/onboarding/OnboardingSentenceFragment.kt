package com.example.momo_android.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.momo_android.databinding.FragmentOnboardingSentenceBinding
import com.example.momo_android.databinding.FragmentOnboardingStartBinding


class OnboardingSentenceFragment : Fragment() {
    private var _Binding: FragmentOnboardingSentenceBinding? = null
    private val Binding get() = _Binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _Binding = FragmentOnboardingSentenceBinding.inflate(layoutInflater)
        return Binding.root
        //return inflater.inflate(R.layout.bottomsheet_custom, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }
}