package com.example.momo_android.ui

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

        val lineThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_line_thumb, null, false
        )

        val textThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_text_thumb, null, false
        )

        // 수정 전 단계를 progress에 넣어줘야함 !!
        mainSeekbar.progress = 4
        scrollToDepth() // 수정 전 단계에 따라 배경색 변화


        // 처음 실행될 때 - line과 text 시크바를 main과 동일한 단계로 설정
        lineSeekbar.progress = mainSeekbar.progress
        lineSeekbar.thumb = lineThumb.getThumb()
        lineSeekbar.setOnTouchListener { _, _ -> true }

        textSeekbar.progress = mainSeekbar.progress
        textSeekbar.thumb = textThumb.getThumb()
        (textThumb.findViewById(R.id.tv_seekbar_depth) as TextView).text = getDepth(mainSeekbar.progress)
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
                textSeekbar.thumb = textThumb.getThumb()
                (textThumb.findViewById(R.id.tv_seekbar_depth) as TextView).text = getDepth(textSeekbar.progress)
                Log.d("이상하다", getDepth(progress))

                svDeep.smoothScrollToView(depthImg())
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

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

}