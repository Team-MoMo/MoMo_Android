package com.example.momo_android.home.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.databinding.ActivityHomeBinding
import com.example.momo_android.home.adapter.HomeViewPager2Adapter
import com.example.momo_android.lock.ui.LockOffActivity
import com.example.momo_android.splash.SplashActivity.Companion.FROM_SPLASH
import com.example.momo_android.util.SharedPreferenceController
import com.example.momo_android.util.showToast


class HomeActivity : AppCompatActivity() {

    private var _viewBinding: ActivityHomeBinding? = null
    private val viewBinding get() = _viewBinding!!

    private var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setViewPager2()
        showLockOffActivity()
    }

    private fun setViewBinding() {
        _viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun setViewPager2() {
        viewBinding.viewPager2.apply {
            adapter = HomeViewPager2Adapter(this@HomeActivity)
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
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
        viewBinding.viewPager2.setCurrentItem(0, true)
    }
}
