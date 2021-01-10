package com.example.momo_android.splash

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivitySplashBinding
import com.example.momo_android.list.ui.ListActivity
import com.example.momo_android.ui.HomeActivity

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
                // splash 애니메이션이 종료되면 onboarding Activity로 넘어감
                // Todo: Onboarding Activity로 연결하기
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
           }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
    }
}