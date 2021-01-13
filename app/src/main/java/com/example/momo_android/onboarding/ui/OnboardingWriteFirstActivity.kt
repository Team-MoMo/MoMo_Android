package com.example.momo_android.onboarding.ui

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityOnboardingWriteFirstBinding


class OnboardingWriteFirstActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityOnboardingWriteFirstBinding
    private val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setSentenceData()
        initOnboardingAnimation()
    }

    override fun onPause() {
        super.onPause()
        viewBinding.lottie.cancelAnimation()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setViewBinding() {
        viewBinding = ActivityOnboardingWriteFirstBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun setSentenceData() {
        viewBinding.apply {
            tvAuthor.text = intent.getStringExtra("author")
            tvBook.text = intent.getStringExtra("book")
            tvPublisher.text = intent.getStringExtra("publisher")
            tvSentence.text = intent.getStringExtra("sentence")
        }
    }

    private fun initOnboardingAnimation() {
        viewBinding.lottie.apply {
                setAnimation("OnboardingCircle.json")
                playAnimation()
                addAnimatorListener(animatorListener)
            }
    }

    private val animatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) {
            handler.postDelayed({
                startActivityIntent()
                finish()
            }, 500)
        }
    }

    private fun startActivityIntent() {
        val feeling = intent.getIntExtra("feeling", 0)
        val intent = Intent(this, OnboardingWriteSecondActivity::class.java)
        intent.putExtra("author", viewBinding.tvAuthor.text)
        intent.putExtra("book", viewBinding.tvBook.text)
        intent.putExtra("publisher", viewBinding.tvPublisher.text)
        intent.putExtra("sentence", viewBinding.tvSentence.text)
        intent.putExtra("feeling", feeling)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out_long)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val feeling = intent.getIntExtra("feeling", 0)
        val intent = Intent(this, OnboardingSentenceActivity::class.java)
        intent.putExtra("feeling", feeling)
        startActivity(intent)
        overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
    }
}