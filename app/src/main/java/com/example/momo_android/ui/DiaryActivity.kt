package com.example.momo_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityDiaryBinding
import com.example.momo_android.diary.EditDateBottomSheetFragment

class DiaryActivity : AppCompatActivity() {

    companion object {
        var diary_year = 0
        var diary_month = 0
        var diary_date = 0
    }

    private lateinit var binding : ActivityDiaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val fragEditDate = EditDateBottomSheetFragment{

        }

        fragEditDate.show(supportFragmentManager, fragEditDate.tag)
    }
}