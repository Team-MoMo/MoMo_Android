package com.example.momo_android.upload.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityUploadWriteBinding
import com.example.momo_android.util.*

class UploadWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadWriteBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        val feeling=intent.getIntExtra("feeling",0)
        val sentenceId = intent.getIntExtra("sentenceId", 0)
        val emotionId = intent.getIntExtra("emotionId", 0)
        var contents = ""
        //val date = intent.getStringExtra("date")

        when(feeling){
            1->{
                binding.tvFeeling.text="사랑"
                binding.imgFeeling.setImageResource(R.drawable.ic_love_14_black)
            }
            2->{
                binding.tvFeeling.text="행복"
                binding.imgFeeling.setImageResource(R.drawable.ic_happy_14_black)
            }
            3->{
                binding.tvFeeling.text="위로"
                binding.imgFeeling.setImageResource(R.drawable.ic_console_14_black)
            }
            4->{
                binding.tvFeeling.text="화남"
                binding.imgFeeling.setImageResource(R.drawable.ic_angry_14_black)
            }
            5->{
                binding.tvFeeling.text="슬픔"
                binding.imgFeeling.setImageResource(R.drawable.ic_sad_14_black)
            }
            6->{
                binding.tvFeeling.text="우울"
                binding.imgFeeling.setImageResource(R.drawable.ic_bored_14_black)
            }
            7->{
                binding.tvFeeling.text="추억"
                binding.imgFeeling.setImageResource(R.drawable.ic_memory_14_black)
            }
            8->{
                binding.tvFeeling.text="일상"
                binding.imgFeeling.setImageResource(R.drawable.ic_daily_14_black)
            }
        }

        val date=intent.getStringExtra("date")
        binding.tvDate.text=date.toString()
        binding.tvAuthor.text=intent.getStringExtra("author")
        binding.tvBook.text=intent.getStringExtra("book")
        binding.tvPublisher.text=intent.getStringExtra("publisher")
        binding.tvSentence.text=intent.getStringExtra("sentence")

        //< 뒤로가기버튼
        binding.imgBack.setOnClickListener {
            //홈화면
            val intent= Intent(this@UploadWriteActivity, UploadSentenceActivity::class.java)
            intent.putExtra("feeling",feeling)
            startActivity(intent)
        }

        //< 뒤로가기버튼
        binding.tvNext.setOnClickListener {
            //홈화면
            val intent= Intent(this@UploadWriteActivity, UploadDeepActivity::class.java)

            if (binding.etDiary.text.toString() != "") {
                contents = binding.etDiary.text.toString()
            }
            intent.putExtra("contents", contents)
            intent.putExtra("sentenceId", sentenceId)
            intent.putExtra("emotionId", emotionId)

            startActivity(intent)
        }
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