package com.example.momo_android.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.momo_android.databinding.ActivityOnboardingWriteSecondBinding

class OnboardingWriteSecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingWriteSecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingWriteSecondBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        binding.tvAuthor.text=intent.getStringExtra("author")
        binding.tvBook.text=intent.getStringExtra("book")
        binding.tvPublisher.text=intent.getStringExtra("publisher")
        binding.tvSentence.text=intent.getStringExtra("sentence")
        val feeling=intent.getIntExtra("feeling",0)
        when(feeling){
            1->binding.tvWrite.text="새로운 인연이 기대되는 하루였다."
            2->binding.tvWrite.text="삶의 소중함을 느낀 하루였다."
            3->binding.tvWrite.text="나를 위한 진한 위로가 필요한 하루였다."
            4->binding.tvWrite.text="끓어오르는 속을 진정시켜야 하는 하루였다."
            5->binding.tvWrite.text="마음이 찌릿하게 아픈 하루였다."
            6->binding.tvWrite.text="눈물이 왈칵 쏟아질 것 같은 하루였다."
            7->binding.tvWrite.text="오래된 기억이 되살아나는 하루였다."
            8->binding.tvWrite.text="평안한 하루가 감사한 날이었다."
        }
        val intent= Intent(this@OnboardingWriteSecondActivity, OnboardingDepthActivity::class.java)

        Handler().postDelayed({ startActivity(intent) }, 4000L)

    }
}