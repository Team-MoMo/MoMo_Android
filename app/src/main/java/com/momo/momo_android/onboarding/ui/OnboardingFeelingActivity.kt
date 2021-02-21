package com.momo.momo_android.onboarding.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityOnboardingFeelingBinding
import com.momo.momo_android.util.getCurrentDate
import com.momo.momo_android.util.getDate
import com.momo.momo_android.util.getMonth
import java.util.*

class OnboardingFeelingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingFeelingBinding//뷰바인딩


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰바인딩
        binding = ActivityOnboardingFeelingBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view) //3

        setDateData()
        setButtonClick()
    }

    //온보딩_현재날짜 설정(확장함수 사용)
    //year / month / date / day 순으로 배열에 담긴 상태
    private fun setDateData() {
        var date = getCurrentDate() // 확장함수에서 배열 return 받음
        binding.tvDate.text =
            date[0] + ". " + getMonth(date[1].toInt()) + ". " + getDate(date[2].toInt()) + ". " + date[3]
    }

    //감정 버튼 클릭시 setOnClickListener를 담은 click 함수
    private fun setButtonClick() {
        binding.apply {
            btnLove.click()
            btnHappy.click()
            btnConsole.click()
            btnAngry.click()
            btnSad.click()
            btnBored.click()
            btnMemory.click()
            btnDaily.click()
        }
    }

    fun ConstraintLayout.click() {
        this.setOnClickListener {
            binding.apply {
                when (it.id) {
                    btnLove.id -> ONBOARDING_FEELING = 1
                    btnHappy.id -> ONBOARDING_FEELING = 2
                    btnConsole.id -> ONBOARDING_FEELING = 3
                    btnAngry.id -> ONBOARDING_FEELING = 4
                    btnSad.id -> ONBOARDING_FEELING = 5
                    btnBored.id -> ONBOARDING_FEELING = 6
                    btnMemory.id -> ONBOARDING_FEELING = 7
                    btnDaily.id -> ONBOARDING_FEELING = 8
                }

                val intent =
                    Intent(this@OnboardingFeelingActivity, OnboardingSentenceActivity::class.java)
                intent.putExtra("date", tvDate.text)
                startActivity(intent)
                overridePendingTransition(R.anim.horizontal_left_in, R.anim.horizontal_right_out)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
    }

    //Feeling 값 전달을 intent로 하지 않고 companion 사용
    companion object {
        var ONBOARDING_FEELING = 0
    }
}