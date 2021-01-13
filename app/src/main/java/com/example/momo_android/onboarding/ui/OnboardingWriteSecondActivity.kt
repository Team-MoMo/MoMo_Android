package com.example.momo_android.onboarding.ui

import android.R
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.databinding.ActivityOnboardingWriteSecondBinding


class OnboardingWriteSecondActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityOnboardingWriteSecondBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setSentenceInfo()
        setOnboardingFeeling()
        viewBinding.tvCursor.blink()
        startActivity()
    }

    private fun setViewBinding() {
        viewBinding = ActivityOnboardingWriteSecondBinding.inflate(layoutInflater) // 2
        setContentView(viewBinding.root)
    }

    private fun setSentenceInfo() {
        viewBinding.apply {
            tvAuthor.text = intent.getStringExtra("author")
            tvBook.text = intent.getStringExtra("book")
            tvPublisher.text = intent.getStringExtra("publisher")
            tvSentence.text = intent.getStringExtra("sentence")
        }
    }

    private fun setOnboardingFeeling() {
        viewBinding.tvWrite.setCharacterDelay(150)
        when (intent.getIntExtra("feeling", 0)) {
            1 -> viewBinding.tvWrite.animateText("새로운 인연이 기대되는 하루였다.")
            2 -> viewBinding.tvWrite.animateText("삶의 소중함을 느낀 하루였다.")
            3 -> viewBinding.tvWrite.animateText("나를 위한 진한 위로가 필요한 하루였다.")
            4 -> viewBinding.tvWrite.animateText("끓어오르는 속을 진정시켜야 하는 하루였다.")
            5 -> viewBinding.tvWrite.animateText("마음이 찌릿하게 아픈 하루였다.")
            6 -> viewBinding.tvWrite.animateText("눈물이 왈칵 쏟아질 것 같은 하루였다.")
            7 -> viewBinding.tvWrite.animateText("오래된 기억이 되살아나는 하루였다.")
            8 -> viewBinding.tvWrite.animateText("평안한 하루가 감사한 날이었다.")
            else -> Log.d("TAG", "setOnboardingFeeling: unknown felling")
        }
    }

    private fun View.blink(
        times: Int = Animation.INFINITE,
        duration: Long = 50L,
        offset: Long = 700L,
        minAlpha: Float = 0.0f,
        maxAlpha: Float = 1.0f,
        repeatMode: Int = Animation.REVERSE
    ) {
        startAnimation(AlphaAnimation(minAlpha, maxAlpha).also {
            it.duration = duration
            it.startOffset = offset
            it.repeatMode = repeatMode
            it.repeatCount = times
        })
    }

    private fun startActivity() {
        val intent = Intent(this, OnboardingDepthActivity::class.java)
        Handler().postDelayed({ startActivity(intent) }, 6000L)
    }
}