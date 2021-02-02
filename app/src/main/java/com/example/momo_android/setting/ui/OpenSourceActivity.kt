package com.example.momo_android.setting.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityMyInfoBinding
import com.example.momo_android.databinding.ActivityOpenSourceBinding
import com.example.momo_android.setting.adapter.OpenSourceAdapter
import com.example.momo_android.setting.data.OpenSourceData
import com.example.momo_android.upload.adapter.UploadSentenceAdapter
import com.example.momo_android.upload.ui.UploadSentenceActivity
import com.example.momo_android.upload.ui.UploadSentenceActivity.Companion.activity
import com.example.momo_android.upload.ui.UploadWriteActivity
import com.example.momo_android.util.ItemClickListener

class OpenSourceActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOpenSourceBinding
    private lateinit var openSourceAdapter: OpenSourceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenSourceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //뒤로가기
        initBackButton()

        //RecyclerView
        activity = this
        openSourceAdapter = OpenSourceAdapter(this)

        //클릭리스너
        //RecylerView 이용한 버튼
        //RecyclerView 밖에 있는 것들도 이 방식을 사용해서 불러옴. Interface를 만들어서
        openSourceAdapter.setItemClickListener(object : ItemClickListener {
            override fun onClickItem(view: View, position: Int) {


                val intent = Intent(this@OpenSourceActivity, OpenSourceDetailActivity::class.java)
                intent.putExtra("name", openSourceAdapter.data[position].name)
                intent.putExtra("github", openSourceAdapter.data[position].github)
                intent.putExtra("copyright", openSourceAdapter.data[position].copyright)
                intent.putExtra("license", openSourceAdapter.data[position].license)
                intent.putExtra("detail", openSourceAdapter.data[position].detail)
                startActivity(intent)
                overridePendingTransition(R.anim.horizontal_left_in, R.anim.horizontal_right_out)
            }
        })

        //RecyclerView Adapter 연결
        binding.rvOpenSource.adapter =openSourceAdapter
        binding.rvOpenSource.layoutManager = LinearLayoutManager(this)

        openSourceAdapter.data= mutableListOf(
            //Opensource 정보
            OpenSourceData("Retrofit","https://github.com/square/retrofit","Copyright 2013 Square, Inc.","Apache License, Version 2.0",getString(R.string.retrofit)),
            OpenSourceData("Gson","https://github.com/google/gson","Copyright 2008 Google Inc.","Apache License, Version 2.0",getString(R.string.gson)),
            OpenSourceData("Image Part Blur","https://github.com/mmin18/RealtimeBlurView","Copyright 2016 Tu Yimin","Apache License, Version 2.0 ",getString(R.string.image_part_blur)),
            OpenSourceData("Vertical SeekBar","https://github.com/h6ah4i/android-verticalseekbar","Copyright (C) 2015 Haruki Hasegawa","Apache License, Version 2.0",getString(R.string.vertical_seek_bar)),
            OpenSourceData("Lottie","https://github.com/airbnb/lottie-android","Copyright 2018 Airbnb, Inc.","Apache License,Version 2.0",getString(R.string.lottie)),
            OpenSourceData("Shadow","https://github.com/fornewid/neumorphism","Copyright 2020 SOUP","Apache License, Version 2.0 ",getString(R.string.shadow)),
            OpenSourceData("Kotlin","https://github.com/JetBrains/kotlin","Copyright 2010-2020 JetBrains s.r.o.","Apache License 2.0",getString(R.string.kotlin)),
            OpenSourceData("Kakao Open SDK","https://developers.kakao.com/docs","Copyright Kakao Corp","Apache License 2.0",""),
            OpenSourceData("Google API for Android","https://developers.google.com/android","Copyright 2015 Google, Inc","Apache License 2.0",""),
            OpenSourceData("Glide","https://github.com/bumptech/glide","Copyright 2014 Google, Inc.","BSD, part MIT and Apache 2.0.",getString(R.string.glide)),
            OpenSourceData("Apache License 2.0","http://www.apache.org/licenses/LICENSE-2.0","","Apache License",getString(R.string.apache_license))
        )

        openSourceAdapter.notifyDataSetChanged()
    }

    //뒤로가기 버튼
    private fun initBackButton() {
        binding.imgBack.setOnClickListener {
            finish()
        }
    }
}