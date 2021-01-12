package com.example.momo_android.diary.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.momo_android.databinding.ActivityDiaryEditWriteBinding
import com.example.momo_android.diary.data.RequestEditDiaryData
import com.example.momo_android.diary.data.ResponseDiaryData
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.util.*
import retrofit2.Call
import retrofit2.Response

class DiaryEditWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryEditWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryEditWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.etDiary.setText(intent.getStringExtra("tv_diary_content").toString())

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
            val backModal = ModalDiaryEditBack(this)
            backModal.start()
            backModal.setOnClickListener {
                if(it == "확인") {
                    finish()
                }
            }
        }

        binding.btnEdit.setOnClickListener {
            // 일기수정 통신
            requestEditDiary()
            finish()
            this.showToast("일기가 수정되었습니다.")
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
            Authorization = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTYxMDI4NTcxOCwiZXhwIjoxNjE4MDYxNzE4LCJpc3MiOiJtb21vIn0.BudOmb4xI78sbtgw81wWY8nfBD2A6Wn4vS4bvlzSZYc",
            params = DiaryActivity.responseData[0].id,
            RequestEditDiaryData(
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
                        Log.d("일기내용 수정 성공", response.body().toString())
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


}