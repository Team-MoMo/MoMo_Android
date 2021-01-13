package com.example.momo_android.home.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityHomeBinding
import com.example.momo_android.home.adapter.HomeViewPager2Adapter
import com.example.momo_android.util.setCustomCurrentItem


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
        viewBinding.viewPager2.adapter = HomeViewPager2Adapter(this)
    }

    override fun onBackPressed() {
        when (viewBinding.viewPager2.currentItem) {
            0 -> setHomeFragmentBackPressed()
            1 -> setScrollFragmentBackPressed()
        }
    }

    private fun setHomeFragmentBackPressed() {
        if (!IS_FROM_SCROLL) {
            if (System.currentTimeMillis() - backPressedTime < 2000) {
                finish()
                return
            }
            Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            backPressedTime = System.currentTimeMillis()
        }
    }

    private fun setScrollFragmentBackPressed() {
        viewBinding.viewPager2.setCurrentItem(0, true)
        IS_FROM_SCROLL = false
    }

    companion object {
        // Scroll Fragment에서 onBackPressed() 호출시 0, 1 루트를 연달아 타는걸 방지하기 위해 사용함
        var IS_FROM_SCROLL: Boolean = false
    }
}
