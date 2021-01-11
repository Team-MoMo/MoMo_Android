package com.example.momo_android.login.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.momo_android.databinding.ActivityFindPasswordBinding

class FindPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}