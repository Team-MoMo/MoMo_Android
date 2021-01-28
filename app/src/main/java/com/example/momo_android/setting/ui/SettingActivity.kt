package com.example.momo_android.setting.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initBackButton()

        isSwitchOn()

        initTeamInfoClickListener()

        initMyInfoClickListener()
    }

    private fun isSwitchOn() {
        binding.switchLock.setOnCheckedChangeListener { _, onSwitch ->
            // 스위치가 켜지면
            if (onSwitch) {
                binding.imagebuttonResetting.visibility = View.VISIBLE
            }
            // 스위치가 꺼지면
            else {
                binding.imagebuttonResetting.visibility = View.INVISIBLE
            }
        }
    }

    private fun initBackButton() {
        binding.imagebuttonSettingBack.setOnClickListener {
            finish()
        }
    }

    private fun initTeamInfoClickListener() {
        binding.constraintlayoutTouchboxMomoInfo.setOnClickListener {
            val intent = Intent(this, TeamInfoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initMyInfoClickListener(){
        binding.constraintlayoutTouchboxInfo.setOnClickListener {
            val intent=Intent(this, MyInfoActivity::class.java)
            startActivity(intent)
        }
        binding.constraintlayoutTouchboxInsta.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/momo.__.diary?igshid=1slzufxe233m"))
            startActivity(intent)
        }
    }
}