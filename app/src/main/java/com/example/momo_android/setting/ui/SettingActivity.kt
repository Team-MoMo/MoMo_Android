package com.example.momo_android.setting.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.lock.ui.LockOnActivity
import com.example.momo_android.databinding.ActivitySettingBinding
import com.example.momo_android.lock.ui.LockOffActivity
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

        initOpensourceClickListener()

        initVersionName()

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
                    startActivityForResult(intent, LOCK_ON)
                }
                false -> {
                    binding.imagebuttonResetting.visibility = View.INVISIBLE
                    val intent = Intent(this, LockOffActivity::class.java)
                    startActivityForResult(intent, LOCK_OFF)
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

    private fun initOpensourceClickListener(){
        binding.constraintlayoutTouchboxLicense.setOnClickListener {
            val intent = Intent(this, OpenSourceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initVersionName() {
        val pi = packageManager.getPackageInfo(packageName, 0)
        binding.tvSettingVersionInfo.text = "Ver. " + pi.versionName
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val isLocked = data!!.getBooleanExtra("isLocked", false)
            when(requestCode) {
                LOCK_ON -> {
                    finish()
                    startActivity(intent)
                    SharedPreferenceController.setLockStatus(this, isLocked)

                }
                LOCK_OFF -> {
                    binding.switchLock.isChecked = isLocked
                    SharedPreferenceController.clearPassCode(this)
                    SharedPreferenceController.setLockStatus(this, isLocked)
                }
            }
            SharedPreferenceController.setLockStatus(this, isLocked)
        }
    }

    companion object {
        const val LOCK_ON = 1111
        const val LOCK_OFF = 2222
    }
}