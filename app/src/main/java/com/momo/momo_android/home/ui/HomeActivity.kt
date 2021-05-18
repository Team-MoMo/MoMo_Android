package com.momo.momo_android.home.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.momo.momo_android.databinding.ActivityHomeBinding
import com.momo.momo_android.home.adapter.HomeViewPager2Adapter
import com.momo.momo_android.lock.ui.LockOffActivity
import com.momo.momo_android.splash.SplashActivity.Companion.FROM_SPLASH
import com.momo.momo_android.util.SharedPreferenceController
import com.momo.momo_android.util.setGone
import com.momo.momo_android.util.setVisible
import com.momo.momo_android.util.showToast


class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setViewPager2()
        showLockOffActivity()
        setCoachMark()
    }

    private fun setViewBinding() {
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setViewPager2() {
        binding.viewPager2.apply {
            adapter = HomeViewPager2Adapter(this@HomeActivity)
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
    }

    private fun setCoachMark() {
        if (SharedPreferenceController.getLoginStatus(this)) {
            binding.coachmark.setGone()
        } else {
            binding.apply {
                coachmark.isClickable = true
                coachPage1.setOnClickListener {
                    coachPage1.setGone()
                    coachPage2.setVisible()
                }

                coachPage2.setOnClickListener {
                    coachmark.setGone()
                    coachmark.isClickable = false
                }
            }

            SharedPreferenceController.setLoginStatus(this, true)
        }
    }

    private fun showLockOffActivity() {
        val isLocked = SharedPreferenceController.getLockStatus(this)
        if (isLocked && FROM_SPLASH) {
            val intent = Intent(this, LockOffActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            FROM_SPLASH = false
        }
    }

    fun showFinishToast() {
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }
        showToast("'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.")
        backPressedTime = System.currentTimeMillis()
    }

    fun replaceToHomeFragment() {
        binding.viewPager2.setCurrentItem(0, true)
    }
}
