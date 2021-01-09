package com.example.momo_android.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityDiaryBinding
import com.example.momo_android.databinding.ActivityDiaryEditDeepBinding

class DiaryEditDeepActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDiaryEditDeepBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryEditDeepBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mainSeekbar = binding.mainSeekBar
        val lineSeekbar = binding.lineSeekBar
        val textSeekbar = binding.textSeekBar

        val lineThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_line_thumb, null, false
        )

        val textThumb = LayoutInflater.from(this).inflate(
            R.layout.seekbar_text_thumb, null, false
        )

        // 처음 실행될 때
        lineSeekbar.progress = mainSeekbar.progress
        lineSeekbar.thumb = lineThumb.getThumb(mainSeekbar.progress)

        lineSeekbar.setOnTouchListener { _, _ -> true }

        textSeekbar.progress = mainSeekbar.progress
        textSeekbar.thumb = textThumb.getThumb(mainSeekbar.progress)
        //(textThumb.findViewById(R.id.tv_seekbar_depth) as TextView).text = getDepth(mainSeekbar.progress)
        textSeekbar.setOnTouchListener { _, _ -> true }



    }

//    private fun getDepth(progress: Int): String {
//        val depth = when(progress) {
//            0 -> "2m"
//            1 -> return "30m"
//            2 -> return "100m"
//            3 -> return "300m"
//            4 -> return "700m"
//            5 -> return "1,005m"
//            else -> return "심해"
//        }
//    }

    private fun View.getThumb(progress: Int): BitmapDrawable {
//        (thumbView.findViewById(R.id.tvProgress) as TextView).text = progress.toString() + ""

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

}