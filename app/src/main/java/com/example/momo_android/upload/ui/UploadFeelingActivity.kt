package com.example.momo_android.upload.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.momo_android.databinding.ActivityUploadFeelingBinding
import com.example.momo_android.diary.ui.EditDateBottomSheetFragment
import com.example.momo_android.home.ui.HomeActivity
import com.example.momo_android.list.ui.ListActivity

class UploadFeelingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadFeelingBinding//뷰바인딩
    private var feeling=0
    //넘어오는 화면 저장해뒀다가 UploadSentence에게도 intent
    var page=intent.getStringExtra("intentFrom")

    companion object {
        var diary_year = 0
        var diary_month = 0
        var diary_date = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰바인딩
        binding = ActivityUploadFeelingBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view) //3


        /****
        서버에서 현재날짜와 최근일기작성 받아와서 바로 모달 띄울지 결정하는 코드
        0. binding.tvDate.text=서버최근 날짜로 첨부터 설정
        1. if(서버최근날짜==오늘날짜){}
           else{모달바로 뜨게 show모달}
         ***/

        //모달 연결
        binding.imgDate.setOnClickListener {
            val fragEditDate = EditDateBottomSheetFragment{

            }

            fragEditDate.show(supportFragmentManager, fragEditDate.tag)
        }

        //< 뒤로가기버튼
        binding.imgBack.setOnClickListener {
            //홈화면
            val intent= Intent(this@UploadFeelingActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        //X 버튼
        binding.imgClose.setOnClickListener {
            //Upload 들어오기 전화면 보여주기
            when(page){
                "List -> Upload"->{
                    val intent=Intent(this, ListActivity::class.java)
                }
                //그 외 다른 뷰에서 넘어온 경우.
            }

        }

        //감정버튼 클릭
        binding.btnLove.click()
        binding.btnHappy.click()
        binding.btnConsole.click()
        binding.btnAngry.click()
        binding.btnSad.click()
        binding.btnBored.click()
        binding.btnMemory.click()
        binding.btnDaily.click()

    }

    private fun ConstraintLayout.click(){
        this.setOnClickListener {
            when(this){
                binding.btnLove->{feeling=1}
                binding.btnHappy->{feeling=2}
                binding.btnConsole->{feeling=3}
                binding.btnAngry->{feeling=4}
                binding.btnSad->{feeling=5}
                binding.btnBored->{feeling=6}
                binding.btnMemory->{feeling=7}
                binding.btnDaily->{feeling=8}

            }
            val intent= Intent(this@UploadFeelingActivity, UploadSentenceActivity::class.java)
            intent.putExtra("feeling",feeling)
            intent.putExtra("date",binding.tvDate.text.toString())
            intent.putExtra("intentFrom",page)
            startActivity(intent)
        }
    }
}