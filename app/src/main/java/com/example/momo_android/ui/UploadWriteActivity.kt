package com.example.momo_android.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.momo_android.databinding.ActivityUploadWriteBinding
import com.example.momo_android.util.*

class UploadWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadWriteBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        val feeling=intent.getStringExtra("feeling")
        binding.tvFeeling.text=feeling.toString()
        val date=intent.getStringExtra("date")
        binding.tvDate.text=date.toString()
        binding.tvAuthor.text=intent.getStringExtra("author")
        binding.tvBook.text=intent.getStringExtra("book")
        binding.tvPublisher.text=intent.getStringExtra("publisher")
        binding.tvSentence.text=intent.getStringExtra("sentence")


        //토글 기능
        binding.togglebtn.setOnClickListener {
            if(binding.tvSentence.visibility==View.GONE){
                binding.togglebtn.rotation=0.0f
                binding.tvSentence.setVisible()
            }
            else{
                binding.togglebtn.rotation=180.0f
                binding.tvSentence.setGone()
            }
        }

        //EditText터치시
        binding.etDiary.setOnClickListener {
            binding.togglebtn.rotation=180.0f
            binding.tvSentence.setGone()
        }

        //EditText영역 클릭 반응 위해 범위 넓혀줌
        binding.clickArea.setOnClickListener {
            binding.etDiary.setFocusableInTouchMode(true)
            binding.etDiary.requestFocus()
            binding.etDiary.showKeyboard()
           //binding.clickArea.setGone()//키보드 올라오면서 얘도 밀어올려질 수 있으므로
            //=>밀어올려지는 AdjustPan인가 그거 안써서 괜찮을듯.

            binding.togglebtn.rotation=180.0f
            binding.tvSentence.setGone()
        }
        //EditText외 부분 터치시 키보드 안뜨게. 그 외 부분에 다 터치 인식
        binding.constraintlayout.toggle_visible()
        binding.cardview.toggle_visible()

        binding.line.setOnClickListener {
            binding.etDiary.unshowKeyboard()
            binding.togglebtn.rotation=0.0f
            binding.tvSentence.setVisible()
        }

        onBackPressed()

    }

    fun ConstraintLayout.toggle_visible(){
        this.setOnClickListener {
            binding.etDiary.unshowKeyboard()
            binding.togglebtn.rotation=0.0f
            binding.tvSentence.setVisible()
        }
    }

    override fun onBackPressed() {
        binding.etDiary.unshowKeyboard()
        binding.togglebtn.rotation=0.0f
        binding.tvSentence.setVisible()
    }
    /*
    fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return true
    }*/


}