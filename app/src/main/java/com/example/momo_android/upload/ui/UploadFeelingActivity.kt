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
import com.example.momo_android.upload.UploadDateBottomSheetFragment
import com.example.momo_android.util.getDate
import com.example.momo_android.util.getMonth
import java.text.SimpleDateFormat
import java.util.*

class UploadFeelingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadFeelingBinding//뷰바인딩
    private var feeling=0

    //넘어오는 화면 저장해뒀다가 UploadSentence에게도 intent
    companion object {
        var upload_year = 2020
        var upload_month = 1
        var upload_date = 12
        var wroteAt=""

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

        //HomeActivity에서 오늘일기가 있을때 없을 때 + ListView 오늘일기 없을때(else)
        if(intent.hasExtra("diaryStatus")){
            var value=intent.getBooleanExtra("diaryStatus",true)
            when(value){
                true->{
                    //오늘일기가 있으면 모달이 바로 뜬다.
                    //여기도 0붙여주는거 해야함.
                    //서버 여기서 연결한 다음.  가장 최근 일기 안쓴 날짜 받아와서 .text시키고. 배경택스트 바꿔줘야
                    // 그 companion넣은 다음 openDateModal로 수정
                    ////////////서버통신 자리////////////
                    //openDateModal()
                    setToday() // 위에 처리 했으면 지워야함.
                }
                false->{
                    //오늘 일기가 없으면 오늘 날짜로 설정한다.
                    setToday()
                }
            }

        }
        else{ //그 외 모든 경우 오늘 일기가 없는 것이므로 오늘 날짜로 설정한다.
            setToday()
        }

        //모달 연결
        binding.imgDate.setOnClickListener {
            openDateModal()
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
            intent.putExtra("wroteAt", wroteAt)
            startActivity(intent)
        }
    }

    private fun setToday(){
        // 현재 날짜 가져오기
        val currentDate = Calendar.getInstance()
        upload_year=currentDate.get(Calendar.YEAR)
        upload_month=(currentDate.get(Calendar.MONTH)+1)
        upload_date=currentDate.get(Calendar.DATE)
        val int_week=currentDate.get(Calendar.DAY_OF_WEEK)

        val month= getMonth(upload_month)
        val date= getDate(upload_date)

        var week=""
        when (int_week) {
            1 -> week="일"
            2 -> week="월"
            3 -> week="화"
            4 -> week="수"
            5 -> week="목"
            6 -> week="금"
            7 -> week="토"
            else -> ""
        }

        binding.tvDate.text="${upload_year}. ${month}. ${date}. ${week}요일"
        wroteAt="${upload_year}-${month}-${date}"

    }

    private fun openDateModal(){
        //그리고 모달을 띄운다.
        val fragEditDate = UploadDateBottomSheetFragment {

            // picker 에서 가져온 날짜를 다이어리에 띄워준다
            val pickerDate = "${it[0]}${getMonth(it[1])}${getDate(it[2])}"
            val dayFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA).parse(pickerDate)
            val pickerDay = SimpleDateFormat("EEEE", Locale.KOREA).format(dayFormat)

            binding.tvDate.text = "${it[0]}. ${getMonth(it[1])}. ${getDate(it[2])}. $pickerDay"

            upload_year = it[0]
            upload_month = it[1]
            upload_date = it[2]

            wroteAt="${it[0]}-${getMonth(it[1])}-${getDate(it[2])}"

        }

        fragEditDate.show(supportFragmentManager, fragEditDate.tag)
    }
}
