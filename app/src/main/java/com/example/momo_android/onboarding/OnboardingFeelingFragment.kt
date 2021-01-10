package com.example.momo_android.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.momo_android.databinding.FragmentOnboardingFeelingBinding
import com.example.momo_android.databinding.FragmentOnboardingStartBinding
import com.example.momo_android.ui.UploadSentenceActivity

class OnboardingFeelingFragment : Fragment() {
    private var _Binding: FragmentOnboardingFeelingBinding? = null
    private val Binding get() = _Binding!!
    private var feeling=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _Binding = FragmentOnboardingFeelingBinding.inflate(layoutInflater)
        return Binding.root
        //return inflater.inflate(R.layout.bottomsheet_custom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Binding.btnLove.click()
        Binding.btnHappy.click()
        Binding.btnConsole.click()
        Binding.btnAngry.click()
        Binding.btnSad.click()
        Binding.btnBored.click()
        Binding.btnMemory.click()
        Binding.btnDaily.click()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

    private fun ConstraintLayout.click(){
        this.setOnClickListener {
            when(this){
                Binding.btnLove->{feeling=1}
                Binding.btnHappy->{feeling=2}
                Binding.btnConsole->{feeling=3}
                Binding.btnAngry->{feeling=4}
                Binding.btnSad->{feeling=5}
                Binding.btnBored->{feeling=6}
                Binding.btnMemory->{feeling=7}
                Binding.btnDaily->{feeling=8}

            }
        }
    }
}