package com.example.momo_android.onboarding.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
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

        binding.tvDate.text= timeGenerator()

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

    fun ConstraintLayout.click(){
        var feeling=0
        this.setOnClickListener {
            when(this){
                binding.btnLove->{feeling=1}
                binding.btnHappy->{feeling=2}
                binding.btnConsole->{feeling=3}
                binding.btnAngry->{feeling=4}
                binding.btnSad->{feeling=5}
                binding.btnBored->{feeling=6}
                binding.btnMemory->{feeling=7}
                binding.btnDaily->{feeling=8}
            }

            val intent= Intent(this@OnboardingFeelingActivity, OnboardingSentenceActivity::class.java)
            intent.putExtra("feeling",feeling)
            intent.putExtra("date",binding.tvDate.text.toString())
            startActivity(intent)
        }
    }


    fun timeGenerator() :String{
        // 현재 날짜 가져오기
        val currentDate = Calendar.getInstance()
        val year=currentDate.get(Calendar.YEAR).toString()
        val month=(currentDate.get(Calendar.MONTH)+1).toString()
        val day=currentDate.get(Calendar.DATE).toString()
        val week=currentDate.get(Calendar.DAY_OF_WEEK)

        var strDay=""
        var strMonth=""
        if (month.toInt() < 10) {
            strMonth="0$month"
        }else{strMonth=month}

        if (day.toInt() < 10) {
            strDay="0$day"
        }else{strDay=day}

        var strWeek=""

        when(week){
            1->strWeek="일요일"
            2->strWeek="월요일"
            3->strWeek="화요일"
            4->strWeek="수요일"
            5->strWeek="목요일"
            6->strWeek="금요일"
            7->strWeek="토요일"
        }

        return year+"."+strMonth+"."+strDay+"."+strWeek
    }
}