package com.momo.momo_android.diary.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityDiaryEditDeepBinding
import com.momo.momo_android.diary.data.RequestEditDiaryData
import com.momo.momo_android.diary.data.ResponseDiaryData
import com.momo.momo_android.home.ui.ScrollFragment.Companion.EDITED_DEPTH
import com.momo.momo_android.home.ui.ScrollFragment.Companion.IS_EDITED
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.util.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import kotlin.math.abs


class DiaryEditDeepActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryEditDeepBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryEditDeepBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT


        binding.btnBack.setOnClickListener {
            finish()
        }

        initDiaryData()

        initSeekBar()

        binding.btnEditDepth.setOnClickListener {
            requestEditDiary(binding.mainSeekBar.progress)
        }


    }

    private fun initDiaryData() {
        binding.apply {
            svDiaryEditDepth.setOnTouchListener { _, _ -> true } // 스크롤뷰 스크롤 막기
            tvDepthDate.text = intent.getStringExtra("diary_date")
            imgDepthEmotion.setImageResource(getEmotionWhite(DiaryActivity.responseDiaryData[0].emotionId))
            tvDepthEmotion.text =
                getEmotionString(DiaryActivity.responseDiaryData[0].emotionId, applicationContext)
        }
    }

    private fun initSeekBar() {
        setBackgroundHeight()

        setMainSeekBar() // main SeekBar
        setSideSeekBar() // line & text SeekBar

        // mainSeekbar listener
        binding.mainSeekBar.setOnSeekBarChangeListener(seekBarListener)
    }

    private fun setBackgroundHeight() {
        // 한 단계의 height = 디바이스 height
        val displayHeight = applicationContext.resources.displayMetrics.heightPixels
        for (i in 1..7) {
            val params = resources.getIdentifier(
                "@id/bg_deep${i}",
                "id",
                this.packageName
            )
            val img = binding.root.findViewById(params) as ImageView
            val layoutParams = img.layoutParams
            layoutParams.height = displayHeight
        }
    }

    private fun setMainSeekBar() {
        // 맨 처음에 mainSeekBar를 수정 전 깊이로 세팅
        binding.mainSeekBar.progress = DiaryActivity.responseDiaryData[0].depth

        val h = Handler(mainLooper)
        h.postDelayed(
            { binding.svDiaryEditDepth.scrollTo(0, setBackground(binding.mainSeekBar.progress).top) }, 100
        )
    }

    private fun setSideSeekBar() {
        // line & text SeekBar를 main과 동일한 단계로 설정

        val lineThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_line_thumb, null, false
        )

        val textThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_text_thumb, null, false
        )

        binding.apply {
            lineSeekBar.progress = binding.mainSeekBar.progress
            lineSeekBar.thumb = lineThumb.getThumb()
            lineSeekBar.setOnTouchListener { _, _ -> true }

            textSeekBar.progress = binding.mainSeekBar.progress
            (textThumb.findViewById(R.id.tv_seekbar_depth) as TextView).text =
                getDepthString(binding.textSeekBar.progress, applicationContext)
            textSeekBar.thumb = textThumb.getThumb()
            textSeekBar.setOnTouchListener { _, _ -> true }
        }
    }

    private val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            setSideSeekBar()
            binding.svDiaryEditDepth.smoothScrollToView(setBackground(binding.mainSeekBar.progress))
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
                        EDITED_DEPTH = response.body()!!.data.depth

                        val intent = Intent(applicationContext, DiaryActivity::class.java)
                        intent.putExtra("diaryDepth", response.body()!!.data.depth)
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
    private fun setBackground(progress : Int): ImageView {
        binding.apply {
            return when (progress) {
                0 -> bgDeep1
                1 -> bgDeep2
                2 -> bgDeep3
                3 -> bgDeep4
                4 -> bgDeep5
                5 -> bgDeep6
                else -> bgDeep7
            }
        }
    }

    private fun View.getThumb(): BitmapDrawable {

        this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap = Bitmap.createBitmap(
            this.measuredWidth,
            this.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        this.layout(0, 0, this.measuredWidth, this.measuredHeight)
        this.draw(canvas)

        return BitmapDrawable(resources, bitmap)
    }

    private fun ScrollView.computeDistanceToView(view: View): Int {
        return abs(calculateRectOnScreen(this).top - (this.scrollY + calculateRectOnScreen(view).top))
    }

    private fun calculateRectOnScreen(view: View): Rect {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return Rect(
            location[0],
            location[1],
            location[0] + view.measuredWidth,
            location[1] + view.measuredHeight
        )
    }

    private fun ScrollView.smoothScrollToView(
        view: View,
        marginTop: Int = 0,
        maxDuration: Long = 500L,
        onEnd: () -> Unit = {}
    ) {
        if (this.getChildAt(0).height <= this.height) {
            onEnd()
            return
        }
        val y = computeDistanceToView(view) - marginTop
        val ratio = abs(y - this.scrollY) / (this.getChildAt(0).height - this.height).toFloat()
        ObjectAnimator.ofInt(this, "scrollY", y).apply {
            duration = (maxDuration * ratio).toLong()
            interpolator = AccelerateDecelerateInterpolator()
            doOnEnd {
                onEnd()
            }
            start()
        }
    }

}