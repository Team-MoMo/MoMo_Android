package com.example.momo_android.setting.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityChangePasswordBinding
import com.example.momo_android.util.*
import kotlinx.android.synthetic.main.activity_change_password.*
import java.util.regex.Pattern

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        SharedPreferenceController.setPassword(this, "aaa111")
        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnChangePassword.setOnClickListener {
            nowPasswordController()
        }

        binding.etNowPasswd.editTextListener(binding.tvNowPasswd, binding.tvNowPasswdError, binding.btnNowPasswdErase)
        binding.etNewPasswd.editTextListener(binding.tvNewPasswd, binding.tvNewPasswdError, binding.btnNewPasswdErase)
        binding.etCheckPasswd.editTextListener(binding.tvCheckPasswd, binding.tvCheckPasswdError, binding.btnCheckPasswdErase)

    }

    private fun nowPasswordController() {
        val tv_now_passwd = binding.tvNowPasswd
        val et_now_passwd = binding.etNowPasswd
        val tv_now_error = binding.tvNowPasswdError

        tv_now_passwd.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
        et_now_passwd.background = resources.getDrawable(R.drawable.et_area_error, null)
        tv_now_error.setVisible()

        if(et_now_passwd.text.isEmpty()) {
            tv_now_error.text = "현재 비밀번호를 입력해 주세요"
        } else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", et_now_passwd.text.toString())) {
            tv_now_error.text = "영문 + 숫자 6자리 이상 입력해 주세요"
        } else if(SharedPreferenceController.getPassword(this) != et_now_passwd.text.toString()) {
            tv_now_error.text = "현재 비밀번호랑 일치하지 않습니다"
        } else {
            tv_now_passwd.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            et_now_passwd.background = resources.getDrawable(R.drawable.et_area_default, null)
            tv_now_error.setInVisible()
            newPasswordController()
        }
    }

    private fun newPasswordController() {
        val tv_new_passwd = binding.tvNewPasswd
        val et_new_passwd = binding.etNewPasswd
        val tv_new_error = binding.tvNewPasswdError

        tv_new_passwd.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
        et_new_passwd.background = resources.getDrawable(R.drawable.et_area_error, null)
        tv_new_error.setVisible()

        if(et_new_passwd.text.isEmpty()) {
            tv_new_error.text = "새로운 비밀번호를 입력해 주세요"
        } else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", et_new_passwd.text.toString())) {
            tv_new_error.text = "영문 + 숫자 6자리 이상 입력해 주세요"
        } else {
            tv_new_passwd.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            et_new_passwd.background = resources.getDrawable(R.drawable.et_area_default, null)
            tv_new_error.setInVisible()
            checkPasswordController()
        }
    }

    private fun checkPasswordController() {
        val tv_check_passwd = binding.tvCheckPasswd
        val et_check_passwd = binding.etCheckPasswd
        val tv_check_error = binding.tvCheckPasswdError

        tv_check_passwd.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
        et_check_passwd.background = resources.getDrawable(R.drawable.et_area_error, null)
        tv_check_error.setVisible()

        if(et_check_passwd.text.isEmpty()) {
            tv_check_error.text = "비밀번호를 다시 입력해 주세요"
        } else if(et_new_passwd.text.toString() != et_check_passwd.text.toString()) {
            tv_check_error.text = "비밀번호가 일치하지 않습니다"
        } else {
            tv_check_passwd.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            et_check_passwd.background = resources.getDrawable(R.drawable.et_area_default, null)
            tv_check_error.setInVisible()
            // 변경 통신
            this.showToast("완료 !!!")
        }
    }

    private fun EditText.editTextListener(tv : TextView, tv_error : TextView, button : ImageView) {

        // FocusChangedListener
        this.setOnFocusChangeListener { _, hasFocus ->
            tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            this.background = resources.getDrawable(R.drawable.et_area_default, null)
            tv_error.setInVisible()

            if(this.text.isNotEmpty()) {
                this.clearText(button)
            }

            if(!hasFocus) {
                button.setGone()
                nowPasswordController()
            }
        }

        // TextWatcher => clearText 기능을 위함
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(this@editTextListener.text.isNotEmpty()) {
                    this@editTextListener.clearText(button)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    // edittext 지우는 x버튼
    private fun EditText.clearText(button : ImageView) {
        button.setVisible()
        button.setOnClickListener {
            this.setText("")
        }
    }


}