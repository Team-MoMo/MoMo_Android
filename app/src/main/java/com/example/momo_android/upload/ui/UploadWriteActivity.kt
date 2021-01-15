package com.example.momo_android.upload.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityUploadWriteBinding
import com.example.momo_android.upload.ModalUploadWriteBack
import com.example.momo_android.util.*
import com.example.momo_android.util.ui.BackPressEditText

class UploadWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadWriteBinding

    companion object {
        var depth=0
        var activity : Activity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadWriteBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        activity = this

        val feeling=intent.getIntExtra("feeling",0)
        val sentenceId = intent.getIntExtra("sentenceId", 0)
        val emotionId = intent.getIntExtra("emotionId", 0)
        var contents = ""
        val wroteAt=intent.getStringExtra("wroteAt")
        //val date = intent.getStringExtra("date")

        showFeeling(feeling)

        val date=intent.getStringExtra("date")
        binding.tvDate.text=date.toString()
        binding.tvAuthor.text=intent.getStringExtra("author")
        binding.tvBook.text=intent.getStringExtra("book")
        binding.tvPublisher.text=intent.getStringExtra("publisher")
        binding.tvSentence.text=intent.getStringExtra("sentence")

        //Log.d("depth_write","${UploadWriteActivity.depth}")


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
            binding.etDiary.requestFocus()
            binding.etDiary.hint=""
        }
        //EditText외 부분 터치시 키보드 안뜨게. 그 외 부분에 다 터치 인식
        binding.constraintlayout.toggle_visible()
        binding.cardview.toggle_visible()

        //< 뒤로가기버튼
        binding.imgBack.setOnClickListener {
            checkEditDiary()
        }

        //다음버튼
        binding.tvNext.setOnClickListener {
            //홈화면
            if (binding.etDiary.text.toString() != "") {
                val intent= Intent(this@UploadWriteActivity, UploadDeepActivity::class.java)
                contents = binding.etDiary.text.toString()
                intent.putExtra("contents", contents)
                intent.putExtra("sentenceId", sentenceId)
                intent.putExtra("emotionId", emotionId)
                intent.putExtra("wroteAt",wroteAt)

                intent.putExtra("feeling",feeling)
                intent.putExtra("date",binding.tvDate.text.toString())
                intent.putExtra("author",binding.tvAuthor.text.toString())
                intent.putExtra("book",binding.tvBook.text.toString())
                intent.putExtra("publisher",binding.tvPublisher.text.toString())
                intent.putExtra("sentence",binding.tvSentence.text.toString())
                intent.putExtra("depth",depth)

                //Log.d("depth_write","${UploadWriteActivity.depth}")

                startActivity(intent)
            }
            else{
                showToast("일기를 작성해 주세요!")
            }

        }


        binding.etDiary.setOnBackPressListener(onBackPressListener)


    }

    private val onBackPressListener: BackPressEditText.OnBackPressListener = object :
        BackPressEditText.OnBackPressListener {
        override fun onBackPress() {
            binding.togglebtn.rotation=0.0f
            binding.tvSentence.setVisible()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.action == KeyEvent.ACTION_DOWN) {
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                checkEditDiary()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    private fun checkEditDiary() {
        // 일기를 1자라도 수정했을 경우 팝업
        // 일기를 수정하지 않은 경우 팝업 없이 finish()
        if(binding.etDiary.text.toString().equals("")) {
            finish()
        } else {
            val backModal = ModalUploadWriteBack(this)
            backModal.start()
            backModal.setOnClickListener {
                if(it == "확인") {
                    finish()
                }
            }
        }
    }

    private fun View.toggle_visible(){
        this.setOnClickListener {
            binding.etDiary.unshowKeyboard()
            binding.togglebtn.rotation=0.0f
            binding.tvSentence.setVisible()
        }
    }


    private fun showFeeling(feeling:Int){
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
    }

}