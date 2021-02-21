package com.momo.momo_android.upload.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityUploadFeelingBinding
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.onboarding.ui.OnboardingFeelingActivity
import com.momo.momo_android.onboarding.ui.OnboardingSentenceActivity
import com.momo.momo_android.upload.data.ResponseRecentWriteData
import com.momo.momo_android.util.SharedPreferenceController
import com.momo.momo_android.util.getCurrentDate
import com.momo.momo_android.util.getDate
import com.momo.momo_android.util.getMonth
import kotlinx.android.synthetic.main.activity_upload_feeling.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class UploadFeelingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadFeelingBinding//뷰바인딩

    //넘어오는 화면 저장해뒀다가 UploadSentence에게도 intent
    companion object {
        var upload_year = 0
        var upload_month = 0
        var upload_date = 0
        var upload_wroteAt = "" // 작성 날짜 _-_-_ 형태

        var activity : Activity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰바인딩
        binding = ActivityUploadFeelingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Sentence Activity에서 Feeling 이전의 창으로 넘어가기 위함
        activity = this

        //툴바
        initToolbar()

        //HomeActivity에서 오늘일기가 있을때 없을 때 + ListView 오늘일기 없을때(else) : Modal Open Logic
        if(intent.hasExtra("diaryStatus")){
            val value=intent.getBooleanExtra("diaryStatus",true)
            when(value){
                true->{//오늘일기가 있으면 모달이 바로 뜨며 일기 안쓴 가장 최근 일자 가져오기. Picker/tvDate 설정
                    //받아온 최근 날짜를 기준으로 모달이 뜬다.
                    loadRecentData()
                }
                false->{ //오늘 일기가 없으면 오늘 날짜로 설정한다.
                    setToday()
                }
            }
        }
        else{ //그 외 모든 경우 오늘 일기가 없는 것이므로 오늘 날짜로 설정한다.
            setToday()
        }

        //캘린더 img 클릭시 모달 연결
        binding.apply {
            imgDate.setOnClickListener { openDateModal() }
        }

        //감정버튼 클릭
        setButtonClick()

    }

    //감정 버튼 클릭시 setOnClickListener를 담은 click 함수
    private fun setButtonClick(){
        binding.apply{
            btnLove.click()
            btnHappy.click()
            btnConsole.click()
            btnAngry.click()
            btnSad.click()
            btnBored.click()
            btnMemory.click()
            btnDaily.click()
        }
    }

    fun ConstraintLayout.click() {
        this.setOnClickListener {
            var upload_feeling=0
            binding.apply {
                when(it.id){
                    btnLove.id->{ upload_feeling =1}
                    btnHappy.id -> { upload_feeling = 2}
                    btnConsole.id -> {upload_feeling = 3}
                    btnAngry.id -> {upload_feeling= 4}
                    btnSad.id -> {upload_feeling= 5 }
                    btnBored.id -> {upload_feeling= 6 }
                    btnMemory.id -> {upload_feeling= 7 }
                    btnDaily.id -> {upload_feeling= 8 }
                }

                val intent= Intent(this@UploadFeelingActivity, UploadSentenceActivity::class.java)
                intent.putExtra("emotionId",upload_feeling)
                intent.putExtra("date",tvDate.text.toString())
                intent.putExtra("wroteAt", upload_wroteAt)
                startActivity(intent)
                overridePendingTransition(R.anim.horizontal_left_in, R.anim.horizontal_right_out)
            }
        }
    }

    //DatePicker Modal Open 함수
    private fun openDateModal(){
        //그리고 모달을 띄운다.
        val fragEditDate = UploadDateBottomSheetFragment {

            // picker 에서 가져온 날짜를 다이어리에 띄워준다
            val pickerDate = "${it[0]}${getMonth(it[1])}${getDate(it[2])}"
            val dayFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA).parse(pickerDate)!!
            val pickerDay = SimpleDateFormat("EEEE", Locale.KOREA).format(dayFormat)

            binding.apply {
                tvDate.text = "${it[0]}. ${getMonth(it[1])}. ${getDate(it[2])}. $pickerDay"
            }

            upload_year = it[0]
            upload_month = it[1]
            upload_date = it[2]

            upload_wroteAt = "${it[0]}-${getMonth(it[1])}-${getDate(it[2])}"

        }

        fragEditDate.show(supportFragmentManager, fragEditDate.tag)
    }

    //오늘 일기를 쓴 경우 :안쓴 날짜중 가장 최근날 가져오는것
    private fun loadRecentData(){
        RequestToServer.service.getRecentNoWrite(
            Authorization = SharedPreferenceController.getAccessToken(this),
            userId = SharedPreferenceController.getUserId(this)
        ).enqueue(object:retrofit2.Callback<ResponseRecentWriteData> {
            override fun onResponse(
                call: Call<ResponseRecentWriteData>,
                response: Response<ResponseRecentWriteData>
            ) {
                when {
                    response.code() == 200 -> {
                        Log.d("recentData 200","통신 가능")
                        val no_diary_date=response.body()!!.data
                        // 없는 최근날짜로 xml의 tvDate 재설정(Modal뒤 불투명한 배경에서)
                        setDate(no_diary_date)
                        //tvDate재설정과 동시에 DatePickerModal 띄워줌줌
                       openDateModal()
                    }
                    response.code() == 400 -> {
                        Log.d("recentData 401", response.message())
                    }
                    else -> {
                        Log.d("recentData 500", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseRecentWriteData>, t: Throwable) {
                Log.d("recentData_fail", "fail : ${t.message}")
            }
        })
    }

    //loadRecentData()함수 안에서 쓰이는 '서버에서 받아온 일기안쓴날짜 중 가장최근 값'으로 UI의 날짜text 설정
    private fun setDate(no_data_day: String) {
        //"2021-01-13T00:00:00+00:00"   // 기존 날짜 받아오는 형태와 다른 값.
        val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(no_data_day)!!

        upload_year = SimpleDateFormat("yyyy", Locale.KOREA).format(dateformat).toInt()
        upload_month = SimpleDateFormat("MM", Locale.KOREA).format(dateformat).toInt()
        upload_date = SimpleDateFormat("dd", Locale.KOREA).format(dateformat).toInt()
        val day=SimpleDateFormat("EEEE",Locale.KOREA).format(dateformat)

        val month= getMonth(upload_month)
        val date= getDate(upload_date)

        binding.apply {
            tvDate.text="${upload_year}. ${month}. ${date}. ${day}"
        }
        upload_wroteAt = "${upload_year}"+"-"+"${month}"+"-"+"${date}"
    }

    //오늘일기가 없는경우 : 현재 날짜 가져와서 설정
    private fun setToday(){
        binding.apply {
            var dateArr= getCurrentDate() // 확장함수에서 배열 return 받음
            upload_year=dateArr[0].toInt()
            upload_month=dateArr[1].toInt()
            upload_date=dateArr[2].toInt()

            val year= upload_year.toString()
            val month= getMonth(upload_month)
            val date= getDate(upload_date)
            val week=dateArr[3]

            tvDate.text= "${year}. ${month}. ${date}. ${week}"
            upload_wroteAt = "${year}-${month}-${date}"
        }

    }

    //툴바 설정
    private fun initToolbar() {
        //툴바 사용 설정
        setSupportActionBar(binding.toolbar)

        //toolbar 왼쪽 버튼&제목 안 사용
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_feeling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}
