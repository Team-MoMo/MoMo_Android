package com.example.momo_android.diary.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityDiaryBinding
import com.example.momo_android.diary.data.Diary
import com.example.momo_android.diary.data.RequestEditDiaryData
import com.example.momo_android.diary.data.ResponseDiaryData
import com.example.momo_android.home.ui.ScrollFragment
import com.example.momo_android.home.ui.ScrollFragment.Companion.EDITED_DEPTH
import com.example.momo_android.home.ui.ScrollFragment.Companion.IS_EDITED
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.util.*
import kotlinx.android.synthetic.main.activity_diary.*
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
        lateinit var responseData : List<Diary>
        const val REQUEST_EDIT_DEPTH = 1000
    }

    private lateinit var binding: ActivityDiaryBinding

    override fun onResume() {
        super.onResume()
        // 다이어리 조회
        requestGetDiary()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        val btn_back = binding.btnBack
        val btn_menu = binding.btnMenu
        val btn_edit_date = binding.btnEditDate
        val btn_edit_depth = binding.btnEditDepth
        val btn_edit_diary = binding.btnEditDiary
        val btn_edit_delete = binding.btnEditDelete
        val menu_edit = binding.menuEdit

        // data
        val tv_diary_date = binding.tvDiaryDate
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


        // 날짜 수정
        btn_edit_date.setOnClickListener {
            menu_edit.setGone()
            val fragEditDate = EditDateBottomSheetFragment {

                // picker 에서 가져온 날짜를 다이어리에 띄워준다
                val pickerDate = "${it[0]}${getMonth(it[1])}${getDate(it[2])}"
                val dayFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA).parse(pickerDate)!!
                val pickerDay = SimpleDateFormat("EEEE", Locale.KOREA).format(dayFormat)

                tv_diary_date.text = "${it[0]}. ${getMonth(it[1])}. ${getDate(it[2])}. $pickerDay"

                diary_year = it[0]
                diary_month = it[1]
                diary_date = it[2]

            }

            fragEditDate.show(supportFragmentManager, fragEditDate.tag)
        }

        // 일기 수정
        btn_edit_diary.setOnClickListener {
            menu_edit.setGone()
            val intent = Intent(this, DiaryEditWriteActivity::class.java)
            intent.putExtra("diary_day", tv_diary_date.text.toString())
            intent.putExtra("diary_content", tv_diary_content.text.toString())
            intent.putExtra("diary_depth", binding.tvDiaryDeep.text.toString())
            startActivity(intent)
        }

        // 깊이 수정
        btn_edit_depth.setOnClickListener {
            menu_edit.setGone()
            val intent = Intent(this, DiaryEditDeepActivity::class.java)
            intent.putExtra("diary_day", tv_diary_date.text.toString())
            startActivityForResult(intent, REQUEST_EDIT_DEPTH)
        }

        // 일기 삭제
        btn_edit_delete.setOnClickListener {
            menu_edit.setGone()
            val deleteModal = ModalDiaryDelete(this)
            deleteModal.start()
            deleteModal.setOnClickListener {
                if (it == "삭제") {
                    deleteDiary()
                }
            }
        }

        view.setOnClickListener {
            menu_edit.setGone()
        }

        binding.clDiary.setOnClickListener {
            menu_edit.setGone()
        }


    }

    private fun requestGetDiary() {

        val tv_diary_date = binding.tvDiaryDate
        val img_diary_emotion = binding.imgDiaryEmotion
        val tv_diary_emotion = binding.tvDiaryEmotion
        val tv_diary_deep = binding.tvDiaryDeep
        val tv_contents = binding.tvContents
        val tv_bookname = binding.tvBookname
        val tv_writer = binding.tvWriter
        val tv_publisher = binding.tvPublisher
        val tv_diary_content = binding.tvDiaryContent

        // 다이어리 조회
        RequestToServer.service.getDiary(
            Authorization = SharedPreferenceController.getAccessToken(this),
            params = intent.getIntExtra("diaryId", 0)
        ).enqueue(object : Callback<ResponseDiaryData> {
            override fun onResponse(call: Call<ResponseDiaryData>, response: Response<ResponseDiaryData>) {

                when {
                    response.code() == 200 -> {


                        val body = response.body()!!
                        responseData = listOf(response.body()!!.data)

                        setLoadingViewBackground(body.data.depth)

                        tv_diary_date.text = getFormedDate(body.data.wroteAt) // 날짜
                        setPickerDate(body.data.wroteAt) // 피커 날짜

                        img_diary_emotion.setImageResource(getEmotionImg(body.data.emotionId)) // 감정아이콘
                        tv_diary_emotion.text = getEmotionStr(body.data.emotionId) // 감정텍스트
                        tv_diary_deep.text = getDepthString(body.data.depth) // 깊이
                        setDepthBackground(body.data.depth) // 배경 오브제

                        tv_contents.text = body.data.Sentence.contents // 문장
                        tv_bookname.text = "<${body.data.Sentence.bookName}>" // 책 제목
                        tv_writer.text = body.data.Sentence.writer // 저자
                        tv_publisher.text = "(${body.data.Sentence.publisher})" // 출판사
                        tv_diary_content.text = body.data.contents // 일기

                    }
                    response.code() == 400 -> {
                        Log.d("getDiary 400", response.message())
                    }
                    else -> {
                        Log.d("getDiary 500", response.message())
                    }
                }

                fadeOutLoadingView()
            }

            override fun onFailure(call: Call<ResponseDiaryData>, t: Throwable) {
                Log.d("getDiary ERROR", "$t")
            }

        })
    }

    private fun deleteDiary() {
        RequestToServer.service.deleteDiary(
            Authorization = SharedPreferenceController.getAccessToken(this),
            params = intent.getIntExtra("diaryId", 0)
        ).enqueue(object : Callback<ResponseDiaryData> {
            override fun onResponse(
                call: Call<ResponseDiaryData>,
                response: Response<ResponseDiaryData>
            ) {
                when {
                    response.code() == 200 -> {
                        IS_EDITED = true
                        EDITED_DEPTH = 0
                        Log.d("일기 삭제 성공", response.body().toString())
                        finish()
                    }
                    response.code() == 400 -> {
                        Log.d("deleteDiary 400", response.message())
                    }
                    else -> {
                        Log.d("deleteDiary 500", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDiaryData>, t: Throwable) {
                Log.d("deleteDiary ERROR", "$t")
            }

        })
    }

    private fun getFormedDate(wroteAt: String) : String {
        val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREAN).parse(wroteAt)!!
        val diary_day = SimpleDateFormat("yyyy. MM. dd. EEEE", Locale.KOREA).format(dateformat)
        return diary_day
    }

    private fun setPickerDate(wroteAt: String) {
        val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREAN).parse(wroteAt)!!
        diary_year = SimpleDateFormat("yyyy", Locale.KOREA).format(dateformat).toInt()
        diary_month = SimpleDateFormat("MM", Locale.KOREA).format(dateformat).toInt()
        diary_date = SimpleDateFormat("dd", Locale.KOREA).format(dateformat).toInt()
    }

    private fun setDepthBackground(depth: Int) {
        binding.objectDeep1.setGone()
        binding.objectDeep2.setGone()
        binding.objectDeep3.setGone()
        binding.objectDeep4.setGone()
        binding.objectDeep5.setGone()
        binding.objectDeep6.setGone()
        when (depth) {
            0 -> {
                binding.root.background = resources.getDrawable(R.drawable.bg_deep1, null)
                binding.objectDeep1.setVisible()
            }
            1 -> {
                binding.root.background = resources.getDrawable(R.drawable.bg_deep2, null)
                binding.objectDeep2.setVisible()
            }
            2 -> {
                binding.root.background = resources.getDrawable(R.drawable.bg_deep3, null)
                binding.objectDeep3.setVisible()
            }
            3 -> {
                binding.root.background = resources.getDrawable(R.drawable.bg_deep4, null)
                binding.objectDeep4.setVisible()
            }
            4 -> {
                binding.root.background = resources.getDrawable(R.drawable.bg_deep5, null)
                binding.objectDeep5.setVisible()
            }
            5 -> {
                binding.root.background = resources.getDrawable(R.drawable.bg_deep6, null)
                binding.objectDeep6.setVisible()
            }
            6 -> {
                binding.root.background = resources.getDrawable(R.drawable.bg_deep7, null)
            }
        }
    }

    private fun getDepthString(depth : Int) : String {
        return when (depth) {
            0 -> "2m"
            1 -> "30m"
            2 -> "100m"
            3 -> "300m"
            4 -> "700m"
            5 -> "1,005m"
            6 -> "심해"
            else -> "error"
        }
    }

    private fun getEmotionImg(emotionIdx: Int) : Int {
        return when (emotionIdx) {
            1 -> R.drawable.ic_love_14_white
            2 -> R.drawable.ic_happy_14_white
            3 -> R.drawable.ic_console_14_white
            4 -> R.drawable.ic_angry_14_white
            5 -> R.drawable.ic_sad_14_white
            6 -> R.drawable.ic_bored_14_white
            7 -> R.drawable.ic_memory_14_white
            else -> R.drawable.ic_daily_14_white
        }
    }

    private fun getEmotionStr(emotionIdx: Int) : String {
        return when (emotionIdx) {
            1 -> "사랑"
            2 -> "행복"
            3 -> "위로"
            4 -> "화남"
            5 -> "슬픔"
            6 -> "우울"
            7 -> "추억"
            else -> "일상"
        }
    }

    private fun fadeOutLoadingView() {
        binding.viewDiaryLoading.apply {
            visibility = View.VISIBLE
            alpha = 1f
            animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.viewDiaryLoading.setGone()
                    }
                })
        }
    }

    private fun setLoadingViewBackground(depth : Int) {
        binding.viewDiaryLoading.apply {
            background = when(depth) {
                0 -> resources.getDrawable(R.drawable.bg_deep1, null)
                1 -> resources.getDrawable(R.drawable.bg_deep2, null)
                2 -> resources.getDrawable(R.drawable.bg_deep3, null)
                3 -> resources.getDrawable(R.drawable.bg_deep4, null)
                4 -> resources.getDrawable(R.drawable.bg_deep5, null)
                5 -> resources.getDrawable(R.drawable.bg_deep6, null)
                else -> resources.getDrawable(R.drawable.bg_deep7, null)
            }
        }
    }
}