package com.momo.momo_android.diary.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityDiaryEditDeepBinding
import com.momo.momo_android.diary.data.RequestEditDiaryData
import com.momo.momo_android.diary.data.ResponseDiaryData
import com.momo.momo_android.home.ui.ScrollFragment.Companion.EDITED_DEPTH
import com.momo.momo_android.home.ui.ScrollFragment.Companion.IS_EDITED
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.util.*
import com.momo.momo_android.util.ui.getThumb
import com.momo.momo_android.util.ui.smoothScrollToView
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response


class DiaryEditDeepActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryEditDeepBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryEditDeepBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setStatusBarTransparent(window)

        initDiaryData()

        initSeekBar()

        initBackground()

        applyButtons()

    }

    private fun applyButtons() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }

            btnEditDepth.setOnClickListener {
                requestEditDiary(mainSeekBar.progress)
            }
        }
    }

    private fun initDiaryData() {
        binding.apply {
            tvDepthDate.text = intent.getStringExtra("diary_date")
            imgDepthEmotion.setImageResource(getEmotionWhite(DiaryActivity.responseDiaryData[0].emotionId))
            tvDepthEmotion.text =
                getEmotionString(DiaryActivity.responseDiaryData[0].emotionId, applicationContext)
        }
    }

    private fun initSeekBar() {
        setMainSeekBar() // main SeekBar
        setSideSeekBar() // line & text SeekBar
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBackground() {
        binding.svDiaryEditDepth.setOnTouchListener { _, _ -> true } // 스크롤뷰 스크롤 막기
        setBackgroundHeight()

        Handler(Looper.myLooper()!!).postDelayed(
            { binding.svDiaryEditDepth.scrollTo(0, setScrollviewBackground().top) }, 100
        )
    }

    private fun setBackgroundHeight() {
        // 한 단계의 height = 디바이스 height
        val displayHeight = applicationContext.resources.displayMetrics.heightPixels
        for (i in 1..7) {
            val params = resources.getIdentifier(
                "@id/bg_depth${i}",
                "id",
                this.packageName
            )
            val img = binding.root.findViewById(params) as ImageView
            val layoutParams = img.layoutParams
            layoutParams.height = displayHeight
        }
    }

    private fun setMainSeekBar() {
        binding.apply {
            // 맨 처음에 mainSeekBar를 수정 전 깊이로 세팅
            mainSeekBar.progress = DiaryActivity.responseDiaryData[0].depth

            // mainSeekbar listener
            mainSeekBar.setOnSeekBarChangeListener(seekBarListener)
        }
    }

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun setSideSeekBar() {
        // line & text SeekBar를 main과 동일한 단계로 설정

        val lineThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_line_thumb, null, false
        )

        val textThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_text_thumb, null, false
        )

        binding.apply {
            lineSeekBar.progress = mainSeekBar.progress
            lineSeekBar.thumb = lineThumb.getThumb()
            lineSeekBar.setOnTouchListener { _, _ -> true }

            textSeekBar.progress = mainSeekBar.progress
            (textThumb.findViewById(R.id.tv_seekbar_depth) as TextView).text =
                getDepthString(textSeekBar.progress, applicationContext)
            textSeekBar.thumb = textThumb.getThumb()
            textSeekBar.setOnTouchListener { _, _ -> true }
        }
    }

    private val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            setSideSeekBar()
            binding.svDiaryEditDepth.smoothScrollToView(setScrollviewBackground())
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {}

        override fun onStopTrackingTouch(p0: SeekBar?) {}

    }

    private fun requestEditDiary(depth: Int) {

        RequestToServer.service.editDiary(
            Authorization = SharedPreferenceController.getAccessToken(this),
            params = DiaryActivity.responseDiaryData[0].id,
            body = RequestEditDiaryData(
                depth = depth,
                contents = DiaryActivity.responseDiaryData[0].contents,
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

                        val intent = Intent(applicationContext, DiaryActivity::class.java)
                        intent.putExtra("diaryDepth", it.data.depth)
                        setResult(1000, intent)
                        finish()
                        applicationContext.showToast("깊이가 수정되었습니다.")
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

    // 깊이에 따른 배경색 매치
    private fun setScrollviewBackground(): ImageView {
        binding.apply {
            return when (mainSeekBar.progress) {
                0 -> bgDepth1
                1 -> bgDepth2
                2 -> bgDepth3
                3 -> bgDepth4
                4 -> bgDepth5
                5 -> bgDepth6
                else -> bgDepth7
            }
        }
    }

}