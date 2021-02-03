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
import com.momo.momo_android.upload.data.ResponseRecentWriteData
import com.momo.momo_android.util.SharedPreferenceController
import com.momo.momo_android.util.getDate
import com.momo.momo_android.util.getMonth
import kotlinx.android.synthetic.main.activity_upload_feeling.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class UploadFeelingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadFeelingBinding//뷰바인딩
    private var feeling=0

    //넘어오는 화면 저장해뒀다가 UploadSentence에게도 intent
    companion object {
        var upload_year = 0
        var upload_month = 0
        var upload_date = 0
        var upload_wroteAt = ""

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

        //툴바
        initToolbar()

        //HomeActivity에서 오늘일기가 있을때 없을 때 + ListView 오늘일기 없을때(else)
        if(intent.hasExtra("diaryStatus")){
            var value=intent.getBooleanExtra("diaryStatus",true)
            when(value){
                true->{//오늘일기가 있으면 모달이 바로 뜬다
                    Log.d("intent","$value")
                    //일기 안쓴 가장 최근 일자 가져오기. Picker용과 tvDate용 설정
                    loadRecentData()
                    //받아온 최근 날짜를 기준으로 모달이 뜬다.
                }
                false->{ //오늘 일기가 없으면 오늘 날짜로 설정한다.
                    setToday()
                    Log.d("intent","$value")
                }
            }

        }
        else{ //그 외 모든 경우 오늘 일기가 없는 것이므로 오늘 날짜로 설정한다.
            setToday()
            Log.d("intent","else")
        }

        //모달 연결
        binding.imgDate.setOnClickListener {
            openDateModal()
        }

//        //X 버튼
//        binding.imgClose.setOnClickListener {
//            finish()
//        }

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
            intent.putExtra("wroteAt", upload_wroteAt)
            startActivity(intent)
            overridePendingTransition(R.anim.horizontal_left_in, R.anim.horizontal_right_out)
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

        val week: String
        week = when (int_week) {
            1 -> "일"
            2 -> "월"
            3 -> "화"
            4 -> "수"
            5 -> "목"
            6 -> "금"
            7 -> "토"
            else -> ""
        }

        binding.tvDate.text="${upload_year}. ${month}. ${date}. ${week}요일"
        upload_wroteAt = "${upload_year}-${month}-${date}"

    }

    private fun openDateModal(){
        //그리고 모달을 띄운다.
        val fragEditDate = UploadDateBottomSheetFragment {

            // picker 에서 가져온 날짜를 다이어리에 띄워준다
            val pickerDate = "${it[0]}${getMonth(it[1])}${getDate(it[2])}"
            val dayFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA).parse(pickerDate)!!
            val pickerDay = SimpleDateFormat("EEEE", Locale.KOREA).format(dayFormat)

            binding.tvDate.text = "${it[0]}. ${getMonth(it[1])}. ${getDate(it[2])}. $pickerDay"

            upload_year = it[0]
            upload_month = it[1]
            upload_date = it[2]

            upload_wroteAt = "${it[0]}-${getMonth(it[1])}-${getDate(it[2])}"

        }

        fragEditDate.show(supportFragmentManager, fragEditDate.tag)
    }

    //안쓴 날짜중 가장 최근날 가져오는것
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
                        val no_data_day=response.body()!!.data
                        // string to date
                        setDate(no_data_day)
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
//    private fun showError(error: ResponseBody?) {
//        val e = error ?: return
//        val ob = JSONObject(e.string())
//        this.showToast(ob.getString("message"))
//        Log.d("RecentData_showError", ob.getString("message"))
//    }

    //
    private fun setDate(no_data_day: String) {
        //"2021-01-13T00:00:00+00:00"
        //val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREAN).parse(wroteAt)
        val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(no_data_day)!!

        upload_year = SimpleDateFormat("yyyy", Locale.KOREA).format(dateformat).toInt()
        upload_month = SimpleDateFormat("MM", Locale.KOREA).format(dateformat).toInt()
        upload_date = SimpleDateFormat("dd", Locale.KOREA).format(dateformat).toInt()
        val week=SimpleDateFormat("EEEE",Locale.KOREA).format(dateformat)

        val month= getMonth(upload_month)
        val date= getDate(upload_date)

        binding.tvDate.text="${upload_year}. ${month}. ${date}. ${week}"
        upload_wroteAt = "${upload_year}"+"-"+"${month}"+"-"+"${date}"
        Log.d("upload_identify", "$upload_month"+" "+ "$upload_date")
        Log.d("upload_wroteAt","$upload_wroteAt")
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
