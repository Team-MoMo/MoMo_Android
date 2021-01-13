package com.example.momo_android.onboarding.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityOnboardingStartBinding
import com.example.momo_android.login.ui.MainLoginActivity

class OnboardingStartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingStartBinding//뷰바인딩

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰바인딩
        binding = ActivityOnboardingStartBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view) //3

        binding.btnStart.setOnClickListener {
            val intent=Intent(this@OnboardingStartActivity, OnboardingFeelingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        binding.tvAccountOk.setOnClickListener {
            /********************** Login으로 가게 바꾸기************/
            val intent = Intent(this@OnboardingStartActivity, MainLoginActivity::class.java)
            startActivity(intent)
        }
    }
}