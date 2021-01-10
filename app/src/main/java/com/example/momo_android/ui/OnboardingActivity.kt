package com.example.momo_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.momo_android.*
import com.example.momo_android.databinding.ActivityOnboardingBinding
import com.example.momo_android.onboarding.*

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        startFragment()



    }

    fun startFragment(){
        val startFragment: OnboardingStartFragment = OnboardingStartFragment()
        val feelingFragment: OnboardingFeelingFragment = OnboardingFeelingFragment()
        val sentenceFragment: OnboardingSentenceFragment = OnboardingSentenceFragment()
        val writeFragment: OnboardingWriteFragment = OnboardingWriteFragment()
        val depthFragment: OnboardingDepthFragment = OnboardingDepthFragment()

        supportFragmentManager.beginTransaction().add(R.id.onboarding, OnboardingStartFragment()).commit()
    }
    fun feelingFragment(){
        val feelingFragment: OnboardingFeelingFragment = OnboardingFeelingFragment()
        val transaction=supportFragmentManager .beginTransaction()
        transaction.add(R.id.onboarding,feelingFragment)
        //transaction.addToBackStack()//뒤로가기 버튼을 누르면 액티비티종료 말고 프래그먼트 내 뒤로가기 가능
        transaction.commit()
    }
    fun goBack(){
        onBackPressed()
    }
}