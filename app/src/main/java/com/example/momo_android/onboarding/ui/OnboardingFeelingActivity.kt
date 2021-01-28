package com.example.momo_android.onboarding.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityOnboardingFeelingBinding
import java.util.*

class OnboardingFeelingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingFeelingBinding//뷰바인딩

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰바인딩
        binding = ActivityOnboardingFeelingBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view) //3

        binding.tvDate.text = timeGenerator()

        //Log.d("feeling_click", OnboardingActivity.companion_feeling)
        binding.btnLove.click()
        binding.btnHappy.click()
        binding.btnConsole.click()
        binding.btnAngry.click()
        binding.btnSad.click()
        binding.btnBored.click()
        binding.btnMemory.click()
        binding.btnDaily.click()
    }

    fun ConstraintLayout.click() {
        this.setOnClickListener {
            when (this) {
                binding.btnLove -> {
                    ONBOARDING_FEELING = 1
                }
                binding.btnHappy -> {
                    ONBOARDING_FEELING = 2
                }
                binding.btnConsole -> {
                    ONBOARDING_FEELING = 3
                }
                binding.btnAngry -> {
                    ONBOARDING_FEELING = 4
                }
                binding.btnSad -> {
                    ONBOARDING_FEELING = 5
                }
                binding.btnBored -> {
                    ONBOARDING_FEELING = 6
                }
                binding.btnMemory -> {
                    ONBOARDING_FEELING = 7
                }
                binding.btnDaily -> {
                    ONBOARDING_FEELING = 8
                }
            }

            val intent =
                Intent(this@OnboardingFeelingActivity, OnboardingSentenceActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.horizontal_left_in, R.anim.horizontal_right_out)
        }
    }


    fun timeGenerator(): String {
        // 현재 날짜 가져오기
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR).toString()
        val month = (currentDate.get(Calendar.MONTH) + 1).toString()
        val day = currentDate.get(Calendar.DATE).toString()
        val week = currentDate.get(Calendar.DAY_OF_WEEK)

        var strDay = ""
        var strMonth = ""
        if (month.toInt() < 10) {
            strMonth = "0$month"
        } else {
            strMonth = month
        }

        if (day.toInt() < 10) {
            strDay = "0$day"
        } else {
            strDay = day
        }

        var strWeek = ""

        when (week) {
            1 -> strWeek = "일요일"
            2 -> strWeek = "월요일"
            3 -> strWeek = "화요일"
            4 -> strWeek = "수요일"
            5 -> strWeek = "목요일"
            6 -> strWeek = "금요일"
            7 -> strWeek = "토요일"
        }

        return year + ". " + strMonth + ". " + strDay + ". " + strWeek
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
    }

    companion object {
        var ONBOARDING_FEELING = 0
    }
}