package com.example.momo_android.login.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.databinding.ActivityMainLoginBinding
import com.example.momo_android.signup.ui.SignUpActivity

class MainLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val btn_kakao = binding.btnLoginKakao
        val btn_google = binding.btnLoginGoogle
        val btn_momo = binding.btnLoginMomo

        btn_momo.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}