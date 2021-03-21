package com.momo.momo_android.diary.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.momo.momo_android.databinding.ActivityDiaryEditWriteBinding
import com.momo.momo_android.diary.data.RequestEditDiaryData
import com.momo.momo_android.diary.data.ResponseDiaryData
import com.momo.momo_android.home.ui.ScrollFragment.Companion.EDITED_DEPTH
import com.momo.momo_android.home.ui.ScrollFragment.Companion.IS_EDITED
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.util.*
import com.momo.momo_android.util.ui.BackPressEditText.OnBackPressListener
import okhttp3.ResponseBody
import org.json.JSONObject
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

        initDiaryData()

        setToggleButton()

        binding.btnBack.setOnClickListener {
            checkEditDiary()
        }

        binding.btnEdit.setOnClickListener {
            if (binding.etDiary.text.toString() == beforeDiary) {
                finish()
            } else {
                requestEditDiary()
            }
        }

        binding.etDiary.setOnBackPressListener(onBackPressListener)

    }

    private fun initDiaryData() {
        binding.apply {
            tvDate.text = intent.getStringExtra("diary_date")
            etDiary.setText(intent.getStringExtra("diary_content").toString())
            imgFeeling.setImageResource(getEmotionBlack(DiaryActivity.responseDiaryData[0].emotionId))
            tvFeeling.text =
                getEmotionString(DiaryActivity.responseDiaryData[0].emotionId, applicationContext)
            tvAuthor.text = DiaryActivity.responseDiaryData[0].Sentence.writer
            tvBook.text = "<${DiaryActivity.responseDiaryData[0].Sentence.bookName}>"
            tvPublisher.text = "(${DiaryActivity.responseDiaryData[0].Sentence.publisher})"
            tvSentence.text = DiaryActivity.responseDiaryData[0].Sentence.contents
            tvDepth.text = intent.getStringExtra("diary_depth")

            beforeDiary = etDiary.text.toString()
        }
    }

    private fun setToggleButton() {
        binding.apply {
            etDiary.showKeyboard()
            toggleOff()

            // 토글 버튼 클릭
            togglebtn.setOnClickListener {
                if (tvSentence.visibility == View.GONE) {
                    toggleOn()
                } else {
                    toggleOff()
                }
            }

            // edittext 클릭
            etDiary.setOnClickListener {
                toggleOff()
            }

            // edittext외 부분 터치시 키보드 안뜨게. 그 외 부분에 다 터치 인식
            root.toggle_visible()
            cardview.toggle_visible()
        }

    }

    private val onBackPressListener: OnBackPressListener = object : OnBackPressListener {
        override fun onBackPress() {
            toggleOn()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                checkEditDiary()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun checkEditDiary() {
        // 일기를 1자라도 수정했을 경우 팝업
        // 일기를 수정하지 않은 경우 팝업 없이 finish()
        if (binding.etDiary.text.toString() == beforeDiary) {
            finish()
        } else {
            val backModal = ModalDiaryEditBack(this)
            backModal.start()
            backModal.setOnClickListener {
                if (it == "확인") {
                    finish()
                }
            }
        }
    }


    private fun View.toggle_visible() {
        this.setOnClickListener {
            binding.etDiary.unshowKeyboard()
            toggleOn()
        }
    }

    private fun toggleOn() {
        binding.togglebtn.rotation = 0.0f
        binding.tvSentence.setVisible()
    }

    private fun toggleOff() {
        binding.togglebtn.rotation = 180.0f
        binding.tvSentence.setGone()
    }

    private fun requestEditDiary() {
        RequestToServer.service.editDiary(
            Authorization = SharedPreferenceController.getAccessToken(this),
            params = DiaryActivity.responseDiaryData[0].id,
            body = RequestEditDiaryData(
                depth = DiaryActivity.responseDiaryData[0].depth,
                contents = binding.etDiary.text.toString(),
                userId = DiaryActivity.responseDiaryData[0].userId,
                sentenceId = DiaryActivity.responseDiaryData[0].sentenceId,
                emotionId = DiaryActivity.responseDiaryData[0].emotionId,
                wroteAt = DiaryActivity.responseDiaryData[0].wroteAt
            )
        ).enqueue(object : retrofit2.Callback<ResponseDiaryData> {
            override fun onResponse(
                call: Call<ResponseDiaryData>,
                response: Response<ResponseDiaryData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        IS_EDITED = true
                        EDITED_DEPTH = it.data.depth
                        finish()
                        applicationContext.showToast("일기가 수정되었습니다.")
                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseDiaryData>, t: Throwable) {
                Log.d("editDiary ERROR", "$t")
            }

        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
    }

}