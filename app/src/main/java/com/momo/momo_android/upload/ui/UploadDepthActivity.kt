package com.momo.momo_android.upload.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityUploadDeepBinding
import com.momo.momo_android.diary.ui.DiaryActivity
import com.momo.momo_android.home.ui.ScrollFragment.Companion.IS_EDITED
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.upload.data.RequestUploadDiaryData
import com.momo.momo_android.upload.data.ResponseUploadDiaryData
import com.momo.momo_android.util.*
import com.momo.momo_android.util.ui.getThumb
import kotlinx.android.synthetic.main.activity_upload_deep.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadDepthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadDeepBinding

    private var emotionId: Int = 0
    private var sentenceId: Int = 0
    private var contents: String = ""
    private var wroteAt: String = ""
    private var depth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDeepBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setStatusBarTransparent(window)

        initUploadData()

        initSeekBar()

        initBackground()

        applyButtons()

    }

    private fun applyButtons() {
        binding.apply {
            // 뒤로가기
            btnBack.setOnClickListener {
                UploadWriteActivity.companion_depth = mainSeekBar.progress
                finish()
                overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
            }

            // 닫기
            btnClose.setOnClickListener {
                val exitModal = ModalUploadDeepExit(this@UploadDepthActivity)
                exitModal.start()
                exitModal.setOnClickListener {
                    if (it == "닫기") {
                        finishUploadFlow()
                    }
                }
            }

            // 기록하기
            btnUploadDepth.setOnClickListener {
                // 통신
                uploadDiary(contents, sentenceId, emotionId, mainSeekBar.progress, wroteAt)
            }
        }
    }

    private fun initUploadData() {
        emotionId = intent.getIntExtra("emotionId", 0)
        sentenceId = intent.getIntExtra("sentenceId", 0)
        contents = intent.getStringExtra("contents").toString()
        wroteAt = intent.getStringExtra("wroteAt").toString()
        depth = intent.getIntExtra("depth", 0)
        val date = intent.getStringExtra("date")

        //감정, 이미지, 날짜 화면에 입히기.
        getEmotionString(emotionId, applicationContext)
        getEmotionWhite(emotionId)
        binding.tvDepthDate.text = date
    }

    private fun initSeekBar() {
        setMainSeekBar() // main SeekBar
        setSideSeekBar() // line & text SeekBar
    }

    private fun setMainSeekBar() {
        binding.apply {
            // default = 2m
            mainSeekBar.progress = depth

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
            binding.svUploadDepth.smoothScrollToView(setScrollviewBackground())
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {}

        override fun onStopTrackingTouch(p0: SeekBar?) {}

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBackground() {
        binding.svUploadDepth.setOnTouchListener { _, _ -> true } // 스크롤뷰 스크롤 막기
        setBackgroundHeight()

        Handler(Looper.myLooper()!!).postDelayed(
            { binding.svUploadDepth.scrollTo(0, setScrollviewBackground().top) }, 100
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

    private fun uploadDiary(
        contents: String,
        sentenceId: Int,
        emotionId: Int,
        depth: Int,
        wroteAt: String
    ) {
        RequestToServer.service.uploadDiary(
            Authorization = SharedPreferenceController.getAccessToken(this),
            body = RequestUploadDiaryData(
                contents = contents,
                depth = depth,
                userId = SharedPreferenceController.getUserId(this),
                sentenceId = sentenceId,
                emotionId = emotionId,
                wroteAt = wroteAt
            )
        ).enqueue(object : Callback<ResponseUploadDiaryData> {
            override fun onResponse(
                call: Call<ResponseUploadDiaryData>,
                response: Response<ResponseUploadDiaryData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        Log.d(
                            "uploadDiary-server",
                            "success : ${it.data}, message : ${response.message()}"
                        )

                        // 다이어리 뷰로 이동
                        IS_EDITED = true
                        val intent = Intent(this@UploadDepthActivity, DiaryActivity::class.java)
                        intent.putExtra("diaryId", it.data.id)
                        startActivity(intent)

                        finishUploadFlow()

                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseUploadDiaryData>, t: Throwable) {
                Log.d("uploadDiary-server", "fail : ${t.message}")
            }

        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
        Log.d("UploadSentence-server", ob.getString("message"))
    }

    private fun finishUploadFlow() {
        // 업로드 플로우 액티비티 모두 종료
        UploadFeelingActivity.activity?.finish()
        UploadSentenceActivity.activity?.finish()
        UploadWriteActivity.activity?.finish()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        UploadWriteActivity.companion_depth = binding.mainSeekBar.progress
        finish()
        overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
    }

}