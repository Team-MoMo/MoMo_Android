package com.example.momo_android.login.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityMainLoginBinding
import com.example.momo_android.signup.ui.SignUpActivity
import com.example.momo_android.util.setGone
import com.example.momo_android.util.setVisible
import kotlinx.android.synthetic.main.activity_diary.*

class MainLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        val btn_kakao = binding.btnLoginKakao
        val btn_google = binding.btnLoginGoogle
        val btn_momo = binding.btnLoginMomo

        btn_momo.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val depth = intent.getIntExtra("deep", 5)
        setDepthBackground(depth)

    }

    private fun setDepthBackground(depth: Int) {
        when (depth) {
            0 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep1, null)
            }
            1 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep2, null)
            }
            2 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep3, null)
            }
            3 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep4, null)
            }
            4 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep5, null)
            }
            5 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep6, null)
            }
            6 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep7, null)
            }
        }
    }
}