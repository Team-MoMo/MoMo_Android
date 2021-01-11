package com.example.momo_android.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.momo_android.R
import com.example.momo_android.databinding.FragmentOnboardingStartBinding
import com.example.momo_android.ui.OnboardingActivity
import com.example.momo_android.ui.UploadFeelingActivity
import kotlinx.android.synthetic.main.activity_onboarding.*


class OnboardingStartFragment : Fragment() {
    private var _Binding: FragmentOnboardingStartBinding? = null
    private val Binding get() = _Binding!!
    var onboardingActivity: OnboardingActivity?=null

    //Activity로 접근
    private val act by lazy {context as OnboardingActivity}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _Binding = FragmentOnboardingStartBinding.inflate(layoutInflater)
        return Binding.root
        //return inflater.inflate(R.layout.bottomsheet_custom, container, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        onboardingActivity=context as OnboardingActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       Binding.btnStart.setOnClickListener {
           //다음 프래그먼트로 넘어가기
           //(activity as OnboardingActivity).goNext(OnboardingFeelingFragment())
           val onboarding = act.findViewById<ViewPager>(R.id.onboarding)
           onboarding.currentItem = 1
       }
        Binding.tvAccountOk.setOnClickListener {
            /********************** Login으로 가게 바꾸기************/
            val intent = Intent(getActivity(), UploadFeelingActivity::class.java)
            startActivity(intent)
        }
    }


    /*
    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

    */


}