package com.example.momo_android.splash

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.momo_android.databinding.ActivitySplashBinding
import com.example.momo_android.home.ui.HomeActivity
import com.example.momo_android.login.ui.MainLoginActivity
import com.example.momo_android.onboarding.ui.OnboardingStartActivity
import com.example.momo_android.util.SharedPreferenceController

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initSplashAnimation()
    }

    private fun initSplashAnimation() {
        val animationView = binding.lottieanimationSplash
        animationView.setAnimation("momo_splash.json")
        animationView.playAnimation()

        animationView.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {

                if(SharedPreferenceController.getOnBoarding(this@SplashActivity) == "true") {
                    // 한 번 실행했으면 true
                    if(SharedPreferenceController.getAccessToken(this@SplashActivity).isNullOrBlank()) {
                        // 토큰이 저장되어 있지 않으면 => 로그인으로 이동
                        val intent = Intent(this@SplashActivity, MainLoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // 토큰이 저장되어 있으면 => 홈으로 이동
                        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // 온보딩 실행 & true로 변경
                    SharedPreferenceController.setOnBoarding(this@SplashActivity, "true")
                    val intent = Intent(this@SplashActivity, OnboardingStartActivity::class.java)
                    startActivity(intent)
                    finish()
                }


           }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
    }
}