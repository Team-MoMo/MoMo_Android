package com.example.momo_android.setting.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.lock.ui.LockOnActivity
import com.example.momo_android.databinding.ActivitySettingBinding
import com.example.momo_android.util.SharedPreferenceController

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initBackButton()

        initSwitchLockClickListener()

        initTeamInfoClickListener()

        initMyInfoClickListener()
    }

    override fun onResume() {
        super.onResume()
        binding.switchLock.isChecked = SharedPreferenceController.getLockStatus(this)
    }

    private fun initBackButton() {
        binding.imagebuttonSettingBack.setOnClickListener {
            finish()
        }
    }

    private fun initSwitchLockClickListener() {
        binding.switchLock.setOnClickListener {
            when(binding.switchLock.isChecked) {
                true -> {
                    binding.imagebuttonResetting.visibility = View.VISIBLE
                    val intent = Intent(this, LockOnActivity::class.java)
                    startActivity(intent)
                }
                false -> {
                    binding.imagebuttonResetting.visibility = View.INVISIBLE
                    SharedPreferenceController.setLockStatus(this, false)
//                    val intent = Intent(this, LockOnActivity::class.java)
//                    startActivityForResult(intent)
//                    if(true) {
//                        SharedPreferenceController.setLockStatus(this, false)
//                    } else {
//                        SharedPreferenceController.setLockStatus(this, true)
//                    }
                }
            }
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