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

        binding.tvName.text=intent.getStringExtra("name").toString()
        binding.tvGithub.text=intent.getStringExtra("github").toString()
        binding.tvCopyright.text=intent.getStringExtra("copyright").toString()
        binding.tvLicense.text=intent.getStringExtra("license").toString()
        binding.tvDetail.text=intent.getStringExtra("detail").toString()
    }

    //뒤로가기 버튼
    private fun initBackButton() {
        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}