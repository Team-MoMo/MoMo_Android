package com.example.momo_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.momo_android.adapter.HomeViewPager2Adapter
import com.example.momo_android.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    private var _viewBinding: ActivityHomeBinding? = null
    private val viewBinding get() = _viewBinding!!


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
            0 -> super.onBackPressed()
        }
    }
}