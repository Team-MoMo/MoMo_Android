package com.example.momo_android.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityMyInfoBinding
import com.example.momo_android.databinding.ActivityUploadDeepBinding

class MyInfoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMyInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //뒤로가기
        initBackButton()

        //박스
        changePasswordClickListener()
        privacyPolicyClickListener()
        termsOfServiceClickListener()
        logoutClickListener()

        //회원탈퇴
        withdrawalClickListener()
    }

    //뒤로가기 버튼
    private fun initBackButton() {
        binding.imgBack.setOnClickListener {
            finish()
        }
    }
    //박스 1_ 비밀번호 변경
    private fun changePasswordClickListener(){
        binding.constraintlayoutBox1.setOnClickListener {}
    }
    //박스 2_ 개인정보처리방침
    private fun privacyPolicyClickListener(){
        binding.constraintlayoutBox2.setOnClickListener {}
    }
    //박스 3_서비스이용약관
    private fun termsOfServiceClickListener(){
        binding.constraintlayoutBox3.setOnClickListener {}
    }
    //박스 4_로그아웃
    private fun logoutClickListener(){
        binding.constraintlayoutBox4.setOnClickListener {}
    }

    //회원탈퇴
    private fun withdrawalClickListener(){
        binding.tvWithdrawal.setOnClickListener {
            //모달 보여주고 탈퇴처리
        }
    }
}