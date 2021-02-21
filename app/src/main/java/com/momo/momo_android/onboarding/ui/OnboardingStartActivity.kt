package com.momo.momo_android.onboarding.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityOnboardingStartBinding
import com.momo.momo_android.login.ui.MainLoginActivity

class OnboardingStartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingStartBinding//뷰바인딩

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰바인딩
        binding = ActivityOnboardingStartBinding.inflate(layoutInflater) // 2
        val view = binding.root
        setContentView(view)

        //시작하기버튼, 계정이 있을경우 버튼에 대한 클릭리스너
        setListeners()
    }
    //시작하기버튼, 계정이 있을경우 버튼에 대한 클릭리스너
    private fun setListeners() {
        binding.apply {
            btnStart.setOnClickListener {
                val intent =
                    Intent(this@OnboardingStartActivity, OnboardingFeelingActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            tvAccountOk.setOnClickListener {
                val intent = Intent(this@OnboardingStartActivity, MainLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}