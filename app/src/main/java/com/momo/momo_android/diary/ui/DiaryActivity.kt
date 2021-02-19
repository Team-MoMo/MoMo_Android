package com.momo.momo_android.diary.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityDiaryBinding
import com.momo.momo_android.diary.data.Diary
import com.momo.momo_android.diary.data.ResponseDiaryData
import com.momo.momo_android.home.ui.ScrollFragment.Companion.EDITED_DEPTH
import com.momo.momo_android.home.ui.ScrollFragment.Companion.IS_EDITED
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.util.*
import okhttp3.ResponseBody
import org.json.JSONObject
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
        lateinit var responseDiaryData: List<Diary>
        const val REQUEST_EDIT_DEPTH = 1000
    }

    private lateinit var binding: ActivityDiaryBinding

    override fun onResume() {
        super.onResume()
        // 다이어리 조회
        requestGetDiary()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        // back 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }

        // menu 버튼
        showMenu()
        hideMenu()

        // 날짜 수정
        updateDiaryDate()
        // 일기 수정
        updateDiaryContent()
        // 깊이 수정
        updateDiaryDepth()
        // 일기 삭제
        deleteDiary()


    }

    private fun showMenu() {
        binding.apply {
            btnMenu.setOnClickListener {
                if (btnMenu.isChecked) {
                    menuEdit.setVisible()
                } else {
                    menuEdit.setGone()
                }
            }
        }
    }

    private fun hideMenu() {
        binding.apply {
            root.setOnClickListener {
                menuEdit.setGone()
            }

            clDiary.setOnClickListener {
                menuEdit.setGone()
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateDiaryDate() {
        binding.btnEditDate.setOnClickListener {
            binding.menuEdit.setGone()
            val fragEditDate = EditDateBottomSheetFragment {

                // picker 에서 가져온 날짜를 다이어리에 띄워준다
                val pickerDate = "${it[0]}${getMonth(it[1])}${getDate(it[2])}"
                val dayFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA).parse(pickerDate)!!
                val pickerDay = SimpleDateFormat("EEEE", Locale.KOREA).format(dayFormat)

                binding.tvDiaryDate.text =
                    "${it[0]}. ${getMonth(it[1])}. ${getDate(it[2])}. $pickerDay"

                diary_year = it[0]
                diary_month = it[1]
                diary_date = it[2]

            }

            fragEditDate.show(supportFragmentManager, fragEditDate.tag)
        }
    }

    private fun updateDiaryContent() {
        binding.apply {
            btnEditDiary.setOnClickListener {
                menuEdit.setGone()
                val intent = Intent(this@DiaryActivity, DiaryEditWriteActivity::class.java)
                intent.putExtra("diary_date", tvDiaryDate.text.toString())
                intent.putExtra("diary_content", tvDiaryContent.text.toString())
                intent.putExtra("diary_depth", tvDiaryDepth.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun updateDiaryDepth() {
        binding.apply {
            btnEditDepth.setOnClickListener {
                menuEdit.setGone()
                val intent = Intent(this@DiaryActivity, DiaryEditDeepActivity::class.java)
                intent.putExtra("diary_date", tvDiaryDate.text.toString())
                startActivityForResult(intent, REQUEST_EDIT_DEPTH)
            }
        }
    }

    private fun deleteDiary() {
        binding.btnEditDelete.setOnClickListener {
            binding.menuEdit.setGone()
            val deleteModal = ModalDiaryDelete(this)
            deleteModal.start()
            deleteModal.setOnClickListener {
                if (it == "삭제") {
                    requestDeleteDiary()
                }
            }
        }
    }

    private fun requestGetDiary() {
        // 다이어리 조회
        RequestToServer.service.getDiary(
            Authorization = SharedPreferenceController.getAccessToken(this),
            params = intent.getIntExtra("diaryId", 0)
        ).enqueue(object : Callback<ResponseDiaryData> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ResponseDiaryData>,
                response: Response<ResponseDiaryData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        responseDiaryData = listOf(response.body()!!.data)

                        setLoadingViewBackground(it.data.depth)

                        binding.apply {
                            setDepthBackground(it.data.depth) // 배경 오브제
                            setPickerDate(it.data.wroteAt) // 피커 날짜
                            tvDiaryDate.text = getFormedDate(it.data.wroteAt) // 날짜
                            imgDiaryEmotion.setImageResource(getEmotionWhite(it.data.emotionId)) // 감정아이콘
                            tvDiaryEmotion.text = getEmotionString(it.data.emotionId, applicationContext) // 감정텍스트
                            tvDiaryDepth.text = getDepthString(it.data.depth, applicationContext) // 깊이
                            tvContents.text = it.data.Sentence.contents // 문장
                            tvBookname.text = "<${it.data.Sentence.bookName}>" // 책 제목
                            tvWriter.text = it.data.Sentence.writer // 저자
                            tvPublisher.text = "(${it.data.Sentence.publisher})" // 출판사
                            tvDiaryContent.text = it.data.contents // 일기
                        }

                    } ?: showError(response.errorBody())

                fadeOutLoadingView()
            }

            override fun onFailure(call: Call<ResponseDiaryData>, t: Throwable) {
                Log.d("getDiary ERROR", "$t")
            }

        })
    }

    private fun requestDeleteDiary() {
        // 다이어리 삭제
        RequestToServer.service.deleteDiary(
            Authorization = SharedPreferenceController.getAccessToken(this),
            params = intent.getIntExtra("diaryId", 0)
        ).enqueue(object : Callback<ResponseDiaryData> {
            override fun onResponse(
                call: Call<ResponseDiaryData>,
                response: Response<ResponseDiaryData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        IS_EDITED = true
                        EDITED_DEPTH = 0
                        finish()
                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseDiaryData>, t: Throwable) {
                Log.d("deleteDiary ERROR", "$t")
            }

        })
    }

    private fun showError(error : ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
    }

    private fun getFormedDate(wroteAt: String): String {
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREAN).parse(wroteAt)!!
        return SimpleDateFormat("yyyy. MM. dd. EEEE", Locale.KOREA).format(dateFormat)
    }

    private fun setPickerDate(wroteAt: String) {
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREAN).parse(wroteAt)!!
        diary_year = SimpleDateFormat("yyyy", Locale.KOREA).format(dateFormat).toInt()
        diary_month = SimpleDateFormat("MM", Locale.KOREA).format(dateFormat).toInt()
        diary_date = SimpleDateFormat("dd", Locale.KOREA).format(dateFormat).toInt()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setDepthBackground(depth: Int) {
        binding.apply {
            objectDepth1.setGone()
            objectDepth2.setGone()
            objectDepth3.setGone()
            objectDepth4.setGone()
            objectDepth5.setGone()
            objectDepth6.setGone()

            when (depth) {
                0 -> {
                    root.background = resources.getDrawable(R.drawable.gradient_rectangle_depth1, null)
                    objectDepth1.setVisible()
                }
                1 -> {
                    root.background = resources.getDrawable(R.drawable.gradient_rectangle_depth2, null)
                    objectDepth2.setVisible()
                }
                2 -> {
                    root.background = resources.getDrawable(R.drawable.gradient_rectangle_depth3, null)
                    objectDepth3.setVisible()
                }
                3 -> {
                    root.background = resources.getDrawable(R.drawable.gradient_rectangle_depth4, null)
                    objectDepth4.setVisible()
                }
                4 -> {
                    root.background = resources.getDrawable(R.drawable.gradient_rectangle_depth5, null)
                    objectDepth5.setVisible()
                }
                5 -> {
                    root.background = resources.getDrawable(R.drawable.gradient_rectangle_depth6, null)
                    objectDepth6.setVisible()
                }
                6 -> {
                    root.background = resources.getDrawable(R.drawable.gradient_rectangle_depth7, null)
                }
            }
        }
    }

    private fun fadeOutLoadingView() {
        binding.apply {
            viewDiaryLoading.visibility = View.VISIBLE
            viewDiaryLoading.alpha = 1f
            viewDiaryLoading.animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        viewDiaryLoading.setGone()
                    }
                })
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setLoadingViewBackground(depth: Int) {
        binding.apply {
            viewDiaryLoading.background = when (depth) {
                0 -> resources.getDrawable(R.drawable.gradient_rectangle_depth1, null)
                1 -> resources.getDrawable(R.drawable.gradient_rectangle_depth2, null)
                2 -> resources.getDrawable(R.drawable.gradient_rectangle_depth3, null)
                3 -> resources.getDrawable(R.drawable.gradient_rectangle_depth4, null)
                4 -> resources.getDrawable(R.drawable.gradient_rectangle_depth5, null)
                5 -> resources.getDrawable(R.drawable.gradient_rectangle_depth6, null)
                6 -> resources.getDrawable(R.drawable.gradient_rectangle_depth7, null)
                else -> resources.getDrawable(R.drawable.gradient_rectangle_depth6, null)
            }
        }
    }

}