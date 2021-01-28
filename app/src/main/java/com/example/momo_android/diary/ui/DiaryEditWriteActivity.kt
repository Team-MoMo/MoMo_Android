package com.example.momo_android.diary.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityDiaryEditWriteBinding
import com.example.momo_android.diary.data.RequestEditDiaryData
import com.example.momo_android.diary.data.ResponseDiaryData
import com.example.momo_android.home.ui.ScrollFragment.Companion.EDITED_DEPTH
import com.example.momo_android.home.ui.ScrollFragment.Companion.IS_EDITED
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.util.*
import com.example.momo_android.util.ui.BackPressEditText.OnBackPressListener
import retrofit2.Call
import retrofit2.Response


class DiaryEditWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryEditWriteBinding
    private var beforeDiary = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryEditWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.tvDate.text = intent.getStringExtra("diary_day")
        binding.etDiary.setText(intent.getStringExtra("diary_content").toString())
        binding.imgFeeling.setImageResource(getEmotionImg(DiaryActivity.responseData[0].emotionId))
        binding.tvFeeling.text = getEmotionStr(DiaryActivity.responseData[0].emotionId)
        binding.tvAuthor.text = DiaryActivity.responseData[0].Sentence.writer
        binding.tvBook.text = "<${DiaryActivity.responseData[0].Sentence.bookName}>"
        binding.tvPublisher.text = "(${DiaryActivity.responseData[0].Sentence.publisher})"
        binding.tvSentence.text = DiaryActivity.responseData[0].Sentence.contents
        binding.tvDepth.text = intent.getStringExtra("diary_depth")

        beforeDiary = binding.etDiary.text.toString()


        binding.etDiary.showKeyboard()
        binding.togglebtn.rotation=180.0f
        binding.tvSentence.setGone()

        //토글 기능
        binding.togglebtn.setOnClickListener {
            if(binding.tvSentence.visibility== View.GONE){
                binding.togglebtn.rotation=0.0f
                binding.tvSentence.setVisible()
            }
            else{
                binding.togglebtn.rotation=180.0f
                binding.tvSentence.setGone()
            }
        }

        // edittext 클릭
        binding.etDiary.setOnClickListener {
            binding.togglebtn.rotation=180.0f
            binding.tvSentence.setGone()
        }


        //EditText외 부분 터치시 키보드 안뜨게. 그 외 부분에 다 터치 인식
        binding.constraintlayout.toggle_visible()
        binding.cardview.toggle_visible()

        binding.btnBack.setOnClickListener {
            checkEditDiary()
        }

        binding.btnEdit.setOnClickListener {
            // 일기수정 통신
            requestEditDiary()
        }

        binding.etDiary.setOnBackPressListener(onBackPressListener)

    }

    private val onBackPressListener: OnBackPressListener = object : OnBackPressListener {
        override fun onBackPress() {
            binding.togglebtn.rotation=0.0f
            binding.tvSentence.setVisible()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.action == KeyEvent.ACTION_DOWN) {
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                checkEditDiary()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun checkEditDiary() {
        // 일기를 1자라도 수정했을 경우 팝업
        // 일기를 수정하지 않은 경우 팝업 없이 finish()
        if(binding.etDiary.text.toString() == beforeDiary) {
            finish()
        } else {
            val backModal = ModalDiaryEditBack(this)
            backModal.start()
            backModal.setOnClickListener {
                if(it == "확인") {
                    finish()
                }
            }
        }
    }


    private fun View.toggle_visible(){
        this.setOnClickListener {
            binding.etDiary.unshowKeyboard()
            binding.togglebtn.rotation=0.0f
            binding.tvSentence.setVisible()
        }
    }

    private fun requestEditDiary() {

        RequestToServer.service.editDiary(
            Authorization = SharedPreferenceController.getAccessToken(this),
            params = DiaryActivity.responseData[0].id,
            body = RequestEditDiaryData(
                depth = DiaryActivity.responseData[0].depth,
                contents = binding.etDiary.text.toString(),
                userId = DiaryActivity.responseData[0].userId,
                sentenceId = DiaryActivity.responseData[0].sentenceId,
                emotionId = DiaryActivity.responseData[0].emotionId,
                wroteAt = DiaryActivity.responseData[0].wroteAt
            )
        ).enqueue(object : retrofit2.Callback<ResponseDiaryData> {
            override fun onResponse(
                call: Call<ResponseDiaryData>,
                response: Response<ResponseDiaryData>
            ) {
                when {
                    response.code() == 200 -> {
                        IS_EDITED = true
                        EDITED_DEPTH = response.body()!!.data.depth
                        Log.d("일기내용 수정 성공", response.body().toString())
                        finish()
                        applicationContext.showToast("일기가 수정되었습니다.")
                    }
                    response.code() == 400 -> {
                        Log.d("editDiary 400", response.message())
                    }
                    else -> {
                        Log.d("editDiary 500", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDiaryData>, t: Throwable) {
                Log.d("editDiary ERROR", "$t")
            }

        })
    }

    private fun getEmotionImg(emotionIdx: Int) : Int {
        return when (emotionIdx) {
            1 -> R.drawable.ic_love_14_black
            2 -> R.drawable.ic_happy_14_black
            3 -> R.drawable.ic_console_14_black
            4 -> R.drawable.ic_angry_14_black
            5 -> R.drawable.ic_sad_14_black
            6 -> R.drawable.ic_bored_14_black
            7 -> R.drawable.ic_memory_14_black
            else -> R.drawable.ic_daily_14_black
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

}