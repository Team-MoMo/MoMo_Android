package com.example.momo_android.diary.ui

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
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
import androidx.core.animation.doOnEnd
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityDiaryEditDeepBinding
import com.example.momo_android.diary.data.RequestEditDiaryData
import com.example.momo_android.diary.data.ResponseDiaryData
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.util.showToast
import retrofit2.Call
import retrofit2.Response
import kotlin.math.abs

class DiaryEditDeepActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDiaryEditDeepBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryEditDeepBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 총 3개의 시크바 사용
        val mainSeekbar = binding.mainSeekBar
        val lineSeekbar = binding.lineSeekBar
        val textSeekbar = binding.textSeekBar
        val svDeep = binding.svDiaryEditDeep
        val btn_back = binding.btnBack
        val tv_deep_date = binding.tvDeepDate
        val btn_edit_deep = binding.btnEditDeep


        // 스크롤뷰 스크롤 막기
        svDeep.setOnTouchListener { _, _ -> true }

        btn_back.setOnClickListener {
            finish()
        }

        tv_deep_date.text = intent.getStringExtra("diary_day")
        binding.imgDeepEmotion.setImageResource(getEmotionImg(DiaryActivity.responseData[0].emotionId))
        binding.tvDeepEmotion.text = getEmotionStr(DiaryActivity.responseData[0].emotionId)
        val depth = DiaryActivity.responseData[0].depth

        val lineThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_line_thumb, null, false
        )

        val textThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_text_thumb, null, false
        )

        // 수정 전 단계를 progress에 넣어줘야함 !!
        mainSeekbar.progress = depth
        scrollToDepth() // 수정 전 단계에 따라 배경색 변화


        // 처음 실행될 때 - line과 text 시크바를 main과 동일한 단계로 설정
        lineSeekbar.progress = mainSeekbar.progress
        lineSeekbar.thumb = lineThumb.getThumb()
        lineSeekbar.setOnTouchListener { _, _ -> true }

        textSeekbar.progress = mainSeekbar.progress
        (textThumb.findViewById(R.id.tv_seekbar_depth) as TextView).text = getDepth(textSeekbar.progress)
        textSeekbar.thumb = textThumb.getThumb()
        textSeekbar.setOnTouchListener { _, _ -> true }


        // 한 단계의 height = 디바이스 height
        val displayHeight = applicationContext.resources.displayMetrics.heightPixels
        for(i in 1..7) {
            val params = resources.getIdentifier(
                "@id/bg_deep${i}",
                "id",
                this.packageName
            )
            val img = view.findViewById(params) as ImageView
            val layoutParams = img.layoutParams
            layoutParams.height = displayHeight
        }

        // mainSeekbar listener
        mainSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                lineSeekbar.progress = progress
                lineSeekbar.thumb = lineThumb.getThumb()

                textSeekbar.progress = progress
                (textThumb.findViewById(R.id.tv_seekbar_depth) as TextView).text = getDepth(textSeekbar.progress)
                textSeekbar.thumb = textThumb.getThumb()
                Log.d("이상하다", getDepth(progress))

                svDeep.smoothScrollToView(depthImg())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })


        // 수정하기 버튼
        btn_edit_deep.setOnClickListener {
            // 깊이수정 통신
            requestEditDiary(mainSeekbar.progress)
            finish()
            this.showToast("깊이가 수정되었습니다.")
        }



    }

    private fun requestEditDiary(depth : Int) {

        RequestToServer.service.editDiary(
            Authorization = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTYxMDI4NTcxOCwiZXhwIjoxNjE4MDYxNzE4LCJpc3MiOiJtb21vIn0.BudOmb4xI78sbtgw81wWY8nfBD2A6Wn4vS4bvlzSZYc",
            params = DiaryActivity.responseData[0].id,
            RequestEditDiaryData(
                depth = depth,
                contents = DiaryActivity.responseData[0].contents,
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
                        Log.d("깊이 수정 성공", response.body().toString())
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


    // 깊이에 따른 배경색 매치
    private fun depthImg() : ImageView {
        return when (binding.mainSeekBar.progress) {
            0 -> binding.bgDeep1
            1 -> binding.bgDeep2
            2 -> binding.bgDeep3
            3 -> binding.bgDeep4
            4 -> binding.bgDeep5
            5 -> binding.bgDeep6
            else -> binding.bgDeep7
        }
    }

    // 맨 처음에 수정 전 깊이로 세팅
    private fun scrollToDepth() {
        val h = Handler()
        h.postDelayed(
            { binding.svDiaryEditDeep.scrollTo(0, depthImg().top) }
            , 100
        )
    }

    private fun getDepth(progress: Int): String {
        return when(progress) {
            0 -> "2m"
            1 -> "30m"
            2 -> "100m"
            3 -> "300m"
            4 -> "700m"
            5 -> "1,005m"
            else -> "심해"
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


    // scrollview 부드럽게 움직이도록
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

}