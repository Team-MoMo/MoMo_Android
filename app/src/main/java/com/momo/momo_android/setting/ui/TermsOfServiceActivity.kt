package com.momo.momo_android.setting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.momo.momo_android.databinding.ActivityTermsOfServiceBinding

class TermsOfServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsOfServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsOfServiceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //뒤로가기
        initBackButton()
    }

    //뒤로가기 버튼
    private fun initBackButton() {
        binding.apply {
            imgBack.setOnClickListener {
                finish()
            }
        }
    }
}