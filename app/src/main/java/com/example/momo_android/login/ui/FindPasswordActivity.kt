package com.example.momo_android.login.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityFindPasswordBinding
import com.example.momo_android.util.setInVisible
import com.example.momo_android.util.setVisible

class FindPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val tv_findpw_email = binding.tvFindpwEmail
        val et_findpw_email = binding.etFindpwEmail
        val btn_email_erase = binding.btnEmailErase
        val tv_email_error = binding.tvEmailError
        val btn_find_passwd = binding.btnFindPasswd

        binding.btnFindClose.setOnClickListener {
            finish()
        }

        binding.btnFindPasswd.setOnClickListener {
            if(et_findpw_email.text.isEmpty()) {
                btn_find_passwd.isEnabled = false
            } else if(et_findpw_email.text.isNotEmpty() &&
                !android.util.Patterns.EMAIL_ADDRESS.matcher(et_findpw_email.text.toString()).matches()) {
                tv_findpw_email.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
                et_findpw_email.background = resources.getDrawable(R.drawable.et_area_error, null)
                tv_email_error.setVisible()
                tv_email_error.text = "올바른 이메일 형식이 아닙니다"
            } else {
                // 서버 통신
//                tv_findpw_email.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
//                et_findpw_email.background = resources.getDrawable(R.drawable.signup_et_area_error, null)
//                tv_email_error.setVisible()
//                tv_email_error.text = "가입된 이메일이 없습니다"

                val findModal = ModalFindpwCount(this)
                findModal.start()
            }
        }

        et_findpw_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_findpw_email.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
                et_findpw_email.background = resources.getDrawable(R.drawable.et_area_default, null)
                tv_email_error.setInVisible()

                if(et_findpw_email.text.isNotEmpty()) {
                    et_findpw_email.clearText(btn_email_erase)
                    btn_find_passwd.background = resources.getDrawable(R.drawable.btn_active, null)
                    btn_find_passwd.isEnabled = true
                } else {
                    btn_find_passwd.background = resources.getDrawable(R.drawable.btn_inactive, null)
                    btn_find_passwd.isEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        et_findpw_email.setOnFocusChangeListener { _, _ ->
            tv_findpw_email.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            et_findpw_email.background = resources.getDrawable(R.drawable.et_area_default, null)
            tv_email_error.setInVisible()
        }



    }

    // edittext 지우는 x버튼
    private fun EditText.clearText(button : ImageView) {
        button.setVisible()
        button.setOnClickListener {
            this.setText("")
        }
    }
}