package com.example.momo_android.home.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.databinding.ActivityHomeBinding
import com.example.momo_android.home.adapter.HomeViewPager2Adapter


class HomeActivity : AppCompatActivity() {

    private var _viewBinding: ActivityHomeBinding? = null
    private val viewBinding get() = _viewBinding!!

    private var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setViewPager2()
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

    override fun onBackPressed() {
        super.onBackPressed()
        when(viewBinding.viewPager2.currentItem) {
            0 -> showFinishToast()
        }
    }

    private fun showFinishToast() {
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }

    fun replaceToHomeFragment() {
        viewBinding.viewPager2.setCurrentItem(0, true)
    }
}
