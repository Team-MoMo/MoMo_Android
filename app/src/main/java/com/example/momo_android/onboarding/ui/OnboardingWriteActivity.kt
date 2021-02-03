package com.example.momo_android.onboarding.ui

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityOnboardingWriteBinding
import com.example.momo_android.onboarding.ui.OnboardingFeelingActivity.Companion.ONBOARDING_FEELING

class OnboardingWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingWriteBinding
    private val entire_handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        //뷰바인딩
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingWriteBinding.inflate(layoutInflater) // 2
        val view = binding.root
        setContentView(view)

        //온보딩 Lottie 설정
        initOnboardingAnimation()
        setSentenceData()

        //문장 가운데 정렬-> 왼쪽 정렬 용도
        val handler = Handler(mainLooper)

        handler.postDelayed(Runnable {
            run {
                binding.imgFeather.callOnClick()
            }
        },1000)

        //Typing 감정문장 설정_ 초기: Gone 상태
        setOnboardingFeeling()

        //화면 자동전환
        turnActivityIntent()
    }

    //Onboarding Lottie 관련 함수
    private fun initOnboardingAnimation() {
        binding.lottie.apply {
            setAnimation("OnboardingCircle.json")
            playAnimation()
        }
    }
    override fun onPause() {
        super.onPause()
        binding.lottie.cancelAnimation()
        entire_handler.removeCallbacksAndMessages(null)
    }

    //OnboardingSentence 선택 문장 넘겨받은 것 설정
    private fun setSentenceData() {
        binding.apply {
            tvAuthor.text = intent.getStringExtra("author")
            tvBook.text = intent.getStringExtra("book")
            tvPublisher.text = intent.getStringExtra("publisher")
            tvSentence.text = intent.getStringExtra("sentence")
        }
    }

    //Typing 문장설정
    private fun setOnboardingFeeling() {
        entire_handler.postDelayed({
            binding.tvCursor.text="ㅣ"
            binding.tvCursor.blink()
            binding.tvCursor.visibility = View.VISIBLE
            binding.tvWrite.visibility= View.VISIBLE
            binding.tvWrite.setCharacterDelay(150)
            when (ONBOARDING_FEELING) {
                1 -> binding.tvWrite.animateText("새로운 인연이 기대되는 하루였다.")
                2 -> binding.tvWrite.animateText("삶의 소중함을 느낀 하루였다.")
                3 -> binding.tvWrite.animateText("나를 위한 진한 위로가 필요한 하루였다.")
                4 -> binding.tvWrite.animateText("끓어오르는 속을 진정시켜야 하는 하루였다.")
                5 -> binding.tvWrite.animateText("마음이 찌릿하게 아픈 하루였다.")
                6 -> binding.tvWrite.animateText("눈물이 왈칵 쏟아질 것 같은 하루였다.")
                7 -> binding.tvWrite.animateText("오래된 기억이 되살아나는 하루였다.")
                8 -> binding.tvWrite.animateText("평안한 하루가 감사한 날이었다.")
                else -> Log.d("TAG", "setOnboardingFeeling: unknown feeling")
            }
        }, 2000)
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

    //화면자동전환+인텐트
    private fun turnActivityIntent(){
        val intent = Intent(this, OnboardingDepthActivity::class.java)
        entire_handler.postDelayed({
            startActivity(intent)
            overridePendingTransition(R.anim.horizontal_left_in, R.anim.horizontal_right_out)
        }, 7500L)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
    }

}