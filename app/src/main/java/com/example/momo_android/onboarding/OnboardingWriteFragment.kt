package com.example.momo_android.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.momo_android.databinding.FragmentOnboardingStartBinding
import com.example.momo_android.databinding.FragmentOnboardingWriteBinding
import com.example.momo_android.ui.OnboardingActivity
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_author
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_book
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_publisher
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_sentence

class OnboardingWriteFragment : Fragment() {
    private var _Binding: FragmentOnboardingWriteBinding? = null
    private val Binding get() = _Binding!!
    var onboardingActivity: OnboardingActivity?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _Binding = FragmentOnboardingWriteBinding.inflate(layoutInflater)
        return Binding.root
        //return inflater.inflate(R.layout.bottomsheet_custom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Binding.tvAuthor.text=companion_author
        Binding.tvBook.text= companion_book
        Binding.tvPublisher.text= companion_publisher
        Binding.tvSentence.text=companion_sentence
    }

    /*
    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }*/
}