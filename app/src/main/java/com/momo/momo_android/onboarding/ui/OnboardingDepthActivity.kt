package com.momo.momo_android.onboarding.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityOnboardingDepthBinding
import com.momo.momo_android.login.ui.MainLoginActivity
import com.momo.momo_android.util.getDepthString
import com.momo.momo_android.util.ui.getThumb
import com.momo.momo_android.util.ui.smoothScrollToView

class OnboardingDepthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingDepthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingDepthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        initSeekBar()

        initBackground()

        applyButtonStart()

    }

    private fun applyButtonStart() {
        binding.btnStart.setOnClickListener {
            val intent = Intent(this@OnboardingDepthActivity, MainLoginActivity::class.java)
            intent.putExtra("depth", binding.mainSeekBar.progress)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun initSeekBar() {
        setMainSeekBar() // main SeekBar
        setSideSeekBar() // line & text SeekBar
    }

    private fun setMainSeekBar() {
        binding.apply {
            // default = 2m
            mainSeekBar.progress = 0

            // mainSeekbar listener
            mainSeekBar.setOnSeekBarChangeListener(seekBarListener)
        }
    }

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun setSideSeekBar() {
        // line & text SeekBar를 main과 동일한 단계로 설정

        val lineThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_line_thumb, null, false
        )

        val textThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_text_thumb, null, false
        )

        binding.apply {
            lineSeekBar.progress = mainSeekBar.progress
            lineSeekBar.thumb = lineThumb.getThumb()
            lineSeekBar.setOnTouchListener { _, _ -> true }

            textSeekBar.progress = mainSeekBar.progress
            (textThumb.findViewById(R.id.tv_seekbar_depth) as TextView).text =
                getDepthString(textSeekBar.progress, applicationContext)
            textSeekBar.thumb = textThumb.getThumb()
            textSeekBar.setOnTouchListener { _, _ -> true }
        }
    }

    private val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            setSideSeekBar()
            binding.svOnboardingDepth.smoothScrollToView(setScrollviewBackground())
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {}

        override fun onStopTrackingTouch(p0: SeekBar?) {}

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBackground() {
        binding.svOnboardingDepth.setOnTouchListener { _, _ -> true } // 스크롤뷰 스크롤 막기
        setBackgroundHeight()
    }

    private fun setBackgroundHeight() {
        // 한 단계의 height = 디바이스 height
        val displayHeight = applicationContext.resources.displayMetrics.heightPixels
        for (i in 1..7) {
            val params = resources.getIdentifier(
                "@id/bg_depth${i}",
                "id",
                this.packageName
            )
            val img = binding.root.findViewById(params) as ImageView
            val layoutParams = img.layoutParams
            layoutParams.height = displayHeight
        }
    }

    // 깊이에 따른 배경색 매치
    private fun setScrollviewBackground(): ImageView {
        binding.apply {
            return when (mainSeekBar.progress) {
                0 -> bgDepth1
                1 -> bgDepth2
                2 -> bgDepth3
                3 -> bgDepth4
                4 -> bgDepth5
                5 -> bgDepth6
                else -> bgDepth7
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
    }
}