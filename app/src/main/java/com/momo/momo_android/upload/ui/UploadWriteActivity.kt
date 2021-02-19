package com.momo.momo_android.upload.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityUploadWriteBinding
import com.momo.momo_android.util.*
import com.momo.momo_android.util.ui.BackPressEditText

class UploadWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadWriteBinding

    companion object {
        var companion_depth=0 //UploadDepth의 깊이 임시저장
        var activity : Activity? = null//img_back에서 여러 activity 지울때
        //변수설정:Intent
        var sentenceId=0
        var emotionId=0
        var contents=""
        var wroteAt=""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadWriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        activity = this

        //intent받은 값으로 변수와 화면 text 데이터 설정
        getIntentExtra()
        //intent 받은 감정 값으로 감정 text와 img 설정
        showFeeling(emotionId)

        //토글버튼, editText 터치
        setListener()

        //setFocus
        setFocusListener()


        binding.apply {
            //EditText외 부분 터치시 키보드 안뜨게. 그 외 부분에 다 터치 인식
            constraintlayout.toggle_visible()
            cardview.toggle_visible()

            //< 뒤로가기버튼
            imgBack.setOnClickListener {
                checkEditDiary()
            }

            //다음버튼
            tvNext.setOnClickListener {
                //홈화면
                if (etDiary.text.toString() != "") {
                    setIntentToDepth()
                }
                else{
                    showToast("일기를 작성해 주세요!")//서버에러 방지위해 일기작성 필수화
                }

            }
            //EditText 활성화, 키보드 켜진상태에서 백버튼 눌렀을때
            etDiary.setOnBackPressListener(onBackPressListener)
        }
    }

    //Intent 받은 값으로 초기설정
    fun getIntentExtra(){
        binding.apply {
            sentenceId = intent.getIntExtra("sentenceId", 0)
            emotionId = intent.getIntExtra("emotionId", 0)
            wroteAt=intent.getStringExtra("wroteAt").toString()

            tvDate.text=intent.getStringExtra("date").toString()
            tvAuthor.text=intent.getStringExtra("author")
            tvBook.text=intent.getStringExtra("book")
            tvPublisher.text=intent.getStringExtra("publisher")
            tvSentence.text=intent.getStringExtra("sentence")
        }
    }

    //감정,감정이미지 설정
    private fun showFeeling(feeling: Int) {
        binding.apply {
            tvFeeling.text= getEmotionString(feeling,this@UploadWriteActivity)
            imgFeeling.setImageResource(getEmotionImage(feeling))
        }
    }

    //초기 EditText 터치시 문장 접히는 효과를 위해, Focus를 주는 함수
    private fun setFocusListener(){
        binding.apply {
            etDiary.setOnFocusChangeListener { v, hasFocus ->
                toggleOff()
                etDiary.requestFocus()
                etDiary.hint=""

                if (!hasFocus){//focus를 뺏어서 문장 드러나게함
                    toggleOn()
                }

            }
        }
    }

    //토글 On
    private fun toggleOn(){
        binding.apply {
            togglebtn.rotation=0.0f
            tvSentence.setVisible()
        }
    }

    //토글 Off
    private fun toggleOff(){
        binding.apply {
            togglebtn.rotation=180.0f
            tvSentence.setGone()
        }
    }

    //토글버튼, EditText
    private fun setListener(){
        binding.apply {
            //토글 기능
            togglebtn.setOnClickListener {
                if(tvSentence.visibility==View.GONE){
                    toggleOn()
                }
                else{
                    toggleOff()
                }
            }
            //EditText터치시
            etDiary.setOnClickListener {
                toggleOff()
                etDiary.requestFocus()
                etDiary.hint=""
            }
        }
    }
    //BackKey 눌렸을때 변화 설정
    private val onBackPressListener: BackPressEditText.OnBackPressListener = object :
        BackPressEditText.OnBackPressListener {
        override fun onBackPress() {
            binding.apply {
                toggleOn()
            }
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

    //Diary 내용이 있는지 확인하고 알맞은 팝업
    private fun checkEditDiary() {
        // 일기를 1자라도 수정했을 경우 팝업
        // 일기를 수정하지 않은 경우 팝업 없이 finish()
        if(binding.etDiary.text.toString() == "") {
            finish()
            overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
        } else {
            val backModal = ModalUploadWriteBack(this)
            backModal.start()
            backModal.setOnClickListener {
                if(it == "확인") {
                    finish()
                    overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
                }
            }
        }
    }

    //문장데이터 안보이게 하는 토글 버튼. 회전하여 구현
    private fun View.toggle_visible(){
        this.setOnClickListener {
            binding.apply {
                etDiary.unshowKeyboard()
                toggleOn()
            }
        }
    }

    //UploadDepth으로 Intent보내기
    private fun setIntentToDepth(){
        binding.apply {
            val intent= Intent(this@UploadWriteActivity, UploadDeepActivity::class.java)
            contents = etDiary.text.toString()
            intent.putExtra("contents", contents)
            intent.putExtra("sentenceId", sentenceId)
            intent.putExtra("emotionId", emotionId)
            intent.putExtra("wroteAt",wroteAt)

            intent.putExtra("date",tvDate.text.toString())
            intent.putExtra("author",tvAuthor.text.toString())
            intent.putExtra("book",tvBook.text.toString())
            intent.putExtra("publisher",tvPublisher.text.toString())
            intent.putExtra("sentence",tvSentence.text.toString())
            intent.putExtra("companion_depth",companion_depth)

            startActivity(intent)
            overridePendingTransition(R.anim.horizontal_left_in, R.anim.horizontal_right_out)
        }
    }

    //뒤로가기 눌렸을때
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
    }

}