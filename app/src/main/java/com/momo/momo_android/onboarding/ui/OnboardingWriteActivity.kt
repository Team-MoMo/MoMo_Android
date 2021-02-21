package com.momo.momo_android.onboarding.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityOnboardingWriteBinding
import com.momo.momo_android.onboarding.ui.OnboardingFeelingActivity.Companion.ONBOARDING_FEELING

class OnboardingWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingWriteBinding
    private val entire_handler = Handler(Looper.myLooper()!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        //뷰바인딩
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingWriteBinding.inflate(layoutInflater) // 2
        val view = binding.root
        setContentView(view)

        //온보딩 Lottie 설정
        initOnboardingAnimation()
        //선택 문장 정보 설정
        setSentenceData()

        //문장 가운데 정렬-> 왼쪽 정렬 자동이동
        entire_handler.postDelayed(Runnable {
            run {
                binding.imgFeather.callOnClick()
            }
        }, 1000)

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
            binding.apply {
                tvCursor.text = "ㅣ"
                tvCursor.blink()
                tvCursor.visibility = View.VISIBLE
                tvWrite.visibility = View.VISIBLE
                tvWrite.setCharacterDelay(150)

                tvWrite.animateText(getTypingString(ONBOARDING_FEELING))
            }
        }, 2000)
    }

    //Typing Animation 감정 별 문장
    fun getTypingString(emotionIdx: Int): String {
        return when (emotionIdx) {
            1 -> getString(R.string.typing_love)
            2 -> getString(R.string.typing_happy)
            3 -> getString(R.string.typing_console)
            4 -> getString(R.string.typing_angry)
            5 -> getString(R.string.typing_sad)
            6 -> getString(R.string.typing_bored)
            7 -> getString(R.string.typing_memory)
            8 -> getString(R.string.typing_daily)
            else -> getString(R.string.typing_memory)
        }
    }

    //Typing문장 뒤의 커서
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
    private fun turnActivityIntent() {
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