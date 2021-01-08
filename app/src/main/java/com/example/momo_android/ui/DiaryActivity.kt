package com.example.momo_android.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.databinding.ActivityDiaryBinding
import com.example.momo_android.diary.EditDateBottomSheetFragment
import com.example.momo_android.util.getDate
import com.example.momo_android.util.getMonth
import com.example.momo_android.util.setGone
import com.example.momo_android.util.setVisible
import java.text.SimpleDateFormat
import java.util.*

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

        val btn_back = binding.btnBack
        val btn_menu = binding.btnMenu
        val btn_edit_date = binding.btnEditDate
        val btn_edit_depth = binding.btnEditDepth
        val btn_edit_diary = binding.btnEditDiary
        val btn_edit_delete = binding.btnEditDelete
        val tv_diary_date = binding.tvDiaryDate
        val menu_edit = binding.menuEdit

        // back 버튼
        btn_back.setOnClickListener {
            finish()
        }

        // menu 버튼
        btn_menu.setOnClickListener {
            if(btn_menu.isChecked) {
                menu_edit.setVisible()
            } else {
                menu_edit.setGone()
            }
        }


        // string to date
        val strDate = "2020-08-16T00:00:00.000Z"
        val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREAN).parse(strDate)

        Log.d("테스트", dateformat.toString())

        // 서버에서 받아온 날짜 to 화면에 표시할 형식
        val diary_day = SimpleDateFormat("yyyy. MM. dd. EEEE", Locale.KOREA).format(dateformat)

        Log.d("테스트", diary_day.toString())

        tv_diary_date.text = diary_day

        diary_year = SimpleDateFormat("yyyy", Locale.KOREA).format(dateformat).toInt()
        diary_month = SimpleDateFormat("MM", Locale.KOREA).format(dateformat).toInt()
        diary_date = SimpleDateFormat("dd", Locale.KOREA).format(dateformat).toInt()

        btn_edit_date.setOnClickListener {
            menu_edit.setGone()
            val fragEditDate = EditDateBottomSheetFragment{

                // picker 에서 가져온 날짜를 다이어리에 띄워준다
                val pickerDate = "${it[0]}${getMonth(it[1])}${getDate(it[2])}"
                val dayFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA).parse(pickerDate)
                val pickerDay = SimpleDateFormat("EEEE", Locale.KOREA).format(dayFormat)

                tv_diary_date.text = "${it[0]}. ${getMonth(it[1])}. ${getDate(it[2])}. $pickerDay"

                diary_year = it[0]
                diary_month = it[1]
                diary_date = it[2]

            }


            fragEditDate.show(supportFragmentManager, fragEditDate.tag)
        }





    }

}