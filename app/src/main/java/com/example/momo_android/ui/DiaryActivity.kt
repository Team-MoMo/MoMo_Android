package com.example.momo_android.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.databinding.ActivityDiaryBinding
import com.example.momo_android.diary.data.ResponseDiaryData
import com.example.momo_android.diary.ui.EditDateBottomSheetFragment
import com.example.momo_android.diary.ui.ModalDiaryDelete
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DiaryActivity : AppCompatActivity() {

    companion object {
        var diary_year = 0
        var diary_month = 0
        var diary_date = 0
    }

    private lateinit var binding: ActivityDiaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val btn_back = binding.btnBack
        val btn_menu = binding.btnMenu
        val btn_edit_date = binding.btnEditDate
        val btn_edit_depth = binding.btnEditDepth
        val btn_edit_diary = binding.btnEditDiary
        val btn_edit_delete = binding.btnEditDelete
        val menu_edit = binding.menuEdit

        // data
        val tv_diary_date = binding.tvDiaryDate
        val img_diary_emotion = binding.imgDiaryEmotion
        val tv_diary_emotion = binding.tvDiaryEmotion
        val tv_diary_deep = binding.tvDiaryDeep
        val tv_contents = binding.tvContents
        val tv_bookname = binding.tvBookname
        val tv_writer = binding.tvWriter
        val tv_publisher = binding.tvPublisher
        val tv_diary_content = binding.tvDiaryContent

        // back 버튼
        btn_back.setOnClickListener {
            finish()
        }

        // menu 버튼
        btn_menu.setOnClickListener {
            if (btn_menu.isChecked) {
                menu_edit.setVisible()
            } else {
                menu_edit.setGone()
            }
        }


        RequestToServer.service.getDiary(
            Authorization = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTYxMDI4NTcxOCwiZXhwIjoxNjE4MDYxNzE4LCJpc3MiOiJtb21vIn0.BudOmb4xI78sbtgw81wWY8nfBD2A6Wn4vS4bvlzSZYc",
            params = 1
        ).enqueue(object : Callback<ResponseDiaryData> {
            override fun onResponse(call: Call<ResponseDiaryData>, response: Response<ResponseDiaryData>) {
                Log.d("뭐가 되긴 한겅미???", response.toString())

                if(response.code() == 400) {
                    Log.d("아아아ㅏㅇㄱ 400", response.message())
                }
                when {
                    response.code() == 200 -> {
                        tv_contents.text = response.body()!!.data.Sentence.contents
                        Log.d("getDiary 통신성공", "??")
                    }
                    response.code() == 400 -> {
                        Log.d("getDiary 400", response.message())
                    }
                    else -> {
                        Log.d("getDiary 500", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDiaryData>, t: Throwable) {
                Log.d("getDiary ERROR", "$t")
            }

        })


        // string to date
        val strDate = "2020-08-16T00:00:00.000Z" // 서버에서 받아온 wroteAt 데이터
        val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREAN).parse(strDate)

        Log.d("테스트", dateformat.toString())

        // 서버에서 받아온 날짜 to 화면에 표시할 형식
        val diary_day = SimpleDateFormat("yyyy. MM. dd. EEEE", Locale.KOREA).format(dateformat)

        Log.d("테스트", diary_day.toString())

        tv_diary_date.text = diary_day

        diary_year = SimpleDateFormat("yyyy", Locale.KOREA).format(dateformat).toInt()
        diary_month = SimpleDateFormat("MM", Locale.KOREA).format(dateformat).toInt()
        diary_date = SimpleDateFormat("dd", Locale.KOREA).format(dateformat).toInt()

        // 날짜 수정
        btn_edit_date.setOnClickListener {
            menu_edit.setGone()
            val fragEditDate = EditDateBottomSheetFragment {

                // picker 에서 가져온 날짜를 다이어리에 띄워준다
                val pickerDate = "${it[0]}${getMonth(it[1])}${getDate(it[2])}"
                val dayFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA).parse(pickerDate)
                val pickerDay = SimpleDateFormat("EEEE", Locale.KOREA).format(dayFormat)

                tv_diary_date.text = "${it[0]}. ${getMonth(it[1])}. ${getDate(it[2])}. $pickerDay"

                diary_year = it[0]
                diary_month = it[1]
                diary_date = it[2]

                // 서버 : 수정한 날짜 다시 받아와서 tv_diary_date 에 넣어주기 -> 그래야 깊이수정으로 바뀐 날짜 넘어감


            }


            fragEditDate.show(supportFragmentManager, fragEditDate.tag)
        }


        // 일기 삭제
        btn_edit_delete.setOnClickListener {
            menu_edit.setGone()
            val deleteModal = ModalDiaryDelete(this)
            deleteModal.start()
            deleteModal.setOnClickListener {
                if (it == "삭제") {
                    // 일기삭제 통신
                    // finish()
                }
            }
        }

        // 깊이 수정
        btn_edit_depth.setOnClickListener {
            menu_edit.setGone()
            val intent = Intent(this, DiaryEditDeepActivity::class.java)
            intent.putExtra("diary_day", diary_day)
            startActivity(intent)
        }

        view.setOnClickListener {
            menu_edit.setGone()
        }

        binding.clDiary.setOnClickListener {
            menu_edit.setGone()
        }

    }

}