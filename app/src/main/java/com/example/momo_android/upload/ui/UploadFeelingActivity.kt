package com.example.momo_android.upload.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.momo_android.databinding.ActivityUploadFeelingBinding
import com.example.momo_android.diary.data.Diary
import com.example.momo_android.diary.ui.DiaryActivity
import com.example.momo_android.diary.ui.EditDateBottomSheetFragment
import com.example.momo_android.home.ui.HomeActivity
import com.example.momo_android.list.ui.ListActivity
import com.example.momo_android.util.getDate
import com.example.momo_android.util.getMonth
import java.text.SimpleDateFormat
import java.util.*

class UploadFeelingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadFeelingBinding//뷰바인딩
    private var feeling=0

    //넘어오는 화면 저장해뒀다가 UploadSentence에게도 intent
    companion object {
        var diary_year = 0
        var diary_month = 0
        var diary_date = 0

        var activity : Activity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰바인딩
        binding = ActivityUploadFeelingBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view) //3

        //Sentence Activity에서 Feeling 이전의 창으로 넘어가기 위함
        activity = this

        if(intent.hasExtra("diaryStatus")){
            var value=intent.getBooleanExtra("diaryStatus",true)
            when(value){
                true->{
                    //오늘일기가 있으면 모달이 바로 뜬다
                }
                false->{
                    //오늘 일기가 없으면 오늘 날짜로 설정한다.
                }
            }

        }
        else{ //그 외 모든 경우 오늘 일기가 없는 것이므로 오늘 날짜로 설정한다.


        }

        //모달 연결
        binding.imgDate.setOnClickListener {
            val fragEditDate = EditDateBottomSheetFragment {

                // picker 에서 가져온 날짜를 다이어리에 띄워준다
                val pickerDate = "${it[0]}${getMonth(it[1])}${getDate(it[2])}"
                val dayFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA).parse(pickerDate)
                val pickerDay = SimpleDateFormat("EEEE", Locale.KOREA).format(dayFormat)

                binding.tvDate.text = "${it[0]}. ${getMonth(it[1])}. ${getDate(it[2])}. $pickerDay"

                UploadFeelingActivity.diary_year = it[0]
                UploadFeelingActivity.diary_month = it[1]
                UploadFeelingActivity.diary_date = it[2]

            }

            fragEditDate.show(supportFragmentManager, fragEditDate.tag)
        }


        //X 버튼
        binding.imgClose.setOnClickListener {
            finish()
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
            startActivity(intent)
        }
    }

}