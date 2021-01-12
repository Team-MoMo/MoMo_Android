package com.example.momo_android.diary.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.momo_android.databinding.ActivityDiaryEditWriteBinding
import com.example.momo_android.util.setGone
import com.example.momo_android.util.setVisible
import com.example.momo_android.util.showKeyboard
import com.example.momo_android.util.unshowKeyboard

class DiaryEditWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiaryEditWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryEditWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.etDiary.setText(intent.getStringExtra("tv_diary_content").toString())


        binding.togglebtn.rotation=180.0f
        binding.tvSentence.setGone()

        //토글 기능
        binding.togglebtn.setOnClickListener {
            if(binding.tvSentence.visibility== View.GONE){
                binding.togglebtn.rotation=0.0f
                binding.tvSentence.setVisible()
            }
            else{
                binding.togglebtn.rotation=180.0f
                binding.tvSentence.setGone()
            }
        }

        // edittext 클릭

        binding.etDiary.setOnClickListener {
            binding.togglebtn.rotation=180.0f
            binding.tvSentence.setGone()
        }


        //EditText외 부분 터치시 키보드 안뜨게. 그 외 부분에 다 터치 인식
        binding.constraintlayout.toggle_visible()
        binding.cardview.toggle_visible()

        binding.btnBack.setOnClickListener {
            val backModal = ModalDiaryEditBack(this)
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


}