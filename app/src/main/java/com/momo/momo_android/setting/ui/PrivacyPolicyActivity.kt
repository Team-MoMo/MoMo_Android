package com.momo.momo_android.setting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.momo.momo_android.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //뒤로가기
        initBackButton()
    }
    //뒤로가기 버튼
    private fun initBackButton() {
        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}