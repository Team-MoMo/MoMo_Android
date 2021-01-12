package com.example.momo_android.onboarding.ui

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.momo_android.databinding.ActivityOnboardingWriteFirstBinding

class OnboardingWriteFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingWriteFirstBinding
    var feeling=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingWriteFirstBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        binding.tvAuthor.text=intent.getStringExtra("author")
        binding.tvBook.text=intent.getStringExtra("book")
        binding.tvPublisher.text=intent.getStringExtra("publisher")
        binding.tvSentence.text=intent.getStringExtra("sentence")
        feeling=intent.getIntExtra("feeling",0)
        //Toast.makeText(this@UploadSentenceActivity,uploadSentenceAdapter.data[0].author,Toast.LENGTH_SHORT).show()

        initOnboardingAnimation()
    }

    private fun initOnboardingAnimation() {
        val animationView = binding.lottie
        animationView.setAnimation("OnboardingCircle.json")
        animationView.playAnimation()

        animationView.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                val intent= Intent(this@OnboardingWriteFirstActivity, OnboardingWriteSecondActivity::class.java)
                intent.putExtra("author",binding.tvAuthor.text)
                intent.putExtra("book",binding.tvBook.text)
                intent.putExtra("publisher",binding.tvPublisher.text)
                intent.putExtra("sentence",binding.tvSentence.text)
                intent.putExtra("feeling",feeling)
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