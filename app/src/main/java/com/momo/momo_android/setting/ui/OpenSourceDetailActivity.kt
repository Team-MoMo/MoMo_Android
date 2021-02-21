package com.momo.momo_android.setting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.momo.momo_android.databinding.ActivityOpenSourceDetailBinding

class OpenSourceDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOpenSourceDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenSourceDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initBackButton()

        binding.apply {
            tvName.text = intent.getStringExtra("name").toString()
            tvGithub.text = intent.getStringExtra("github").toString()
            tvCopyright.text = intent.getStringExtra("copyright").toString()
            tvLicense.text = intent.getStringExtra("license").toString()
            tvDetail.text = intent.getStringExtra("detail").toString()
        }
    }
    //뒤로가기 버튼
    private fun initBackButton() {
        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}