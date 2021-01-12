package com.example.momo_android.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.momo_android.databinding.ActivityOnboardingWriteFirstBinding

class OnboardingWriteFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingWriteFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingWriteFirstBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        binding.tvAuthor.text=intent.getStringExtra("author")
        binding.tvBook.text=intent.getStringExtra("book")
        binding.tvPublisher.text=intent.getStringExtra("publisher")
        binding.tvSentence.text=intent.getStringExtra("sentence")
        val feeling=intent.getIntExtra("feeling",0)




        val intent= Intent(this@OnboardingWriteFirstActivity, OnboardingWriteSecondActivity::class.java)
        intent.putExtra("author",binding.tvAuthor.text)
        intent.putExtra("book",binding.tvBook.text)
        intent.putExtra("publisher",binding.tvPublisher.text)
        intent.putExtra("sentence",binding.tvSentence.text)
        intent.putExtra("feeling",feeling)
        //Toast.makeText(this@UploadSentenceActivity,uploadSentenceAdapter.data[0].author,Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ startActivity(intent) }, 1000L)

    }


}