package com.example.momo_android.signup.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivitySignUpBinding
import com.example.momo_android.home.ui.HomeActivity
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.signup.data.RequestUserData
import com.example.momo_android.signup.data.ResponseUserData
import com.example.momo_android.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val btn_close = binding.btnSignupClose
        val btn_signup = binding.btnSignup

        val tv_email = binding.tvEmail
        val tv_pass = binding.tvPasswd
        val tv_pass_check = binding.tvPasswdCheck

        val et_email = binding.etSignupEmail
        val et_pass = binding.etSignupPasswd
        val et_pass_check = binding.etSignupPwCheck

        val tv_email_error = binding.tvEmailError
        val tv_pass_error = binding.tvPwError
        val tv_passck_error = binding.tvPwckError

        val btn_email_erase = binding.btnEmailErase
        val btn_pass_erase = binding.btnPwErase
        val btn_passck_erase = binding.btnPwCheckErase

        val cb_privacy = binding.checkboxPrivacy
        val cb_service = binding.checkboxService



        btn_close.setOnClickListener {
            finish()
        }

        btn_signup.setOnClickListener {
            emailController()
        }

        et_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(et_email.text.isNotEmpty()) {
                    et_email.clearText(btn_email_erase)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        et_pass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(et_pass.text.isNotEmpty()) {
                    et_pass.clearText(btn_pass_erase)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        et_pass.checkPassword(btn_pass_erase)
        et_pass_check.checkPassword(btn_passck_erase)


        et_email.etFocusListener(tv_email, tv_email_error, btn_email_erase)
        et_pass.etFocusListener(tv_pass, tv_pass_error, btn_pass_erase)
        et_pass_check.etFocusListener(tv_pass_check, tv_passck_error, btn_passck_erase)


        cb_privacy.setOnClickListener(checkboxOnClickListener)
        cb_service.setOnClickListener(checkboxOnClickListener)


    }

    private val checkboxOnClickListener = View.OnClickListener {
        if(binding.checkboxPrivacy.isChecked) {
            binding.tvCbPrivacyError1.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            binding.tvCbPrivacyError2.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
        }

        if(binding.checkboxService.isChecked) {
            binding.tvCbServiceError1.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            binding.tvCbServiceError2.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
        }

    }

    private fun emailController() {

        val tv_email = binding.tvEmail
        val et_email = binding.etSignupEmail
        val tv_email_error = binding.tvEmailError

        tv_email.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
        et_email.background = resources.getDrawable(R.drawable.signup_et_area_error, null)
        tv_email_error.setVisible()

        if(et_email.text.isEmpty()) {
            tv_email_error.text = "이메일을 입력해 주세요"
        } else if(et_email.text.isNotEmpty() &&
            !android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches()) {
            tv_email_error.text = "올바른 이메일 형식이 아닙니다"
        } else {
            // 서버통신으로 확인
            // tv_email_error.text = "MOMO에 이미 가입된 이메일이에요!"
            tv_email.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            et_email.background = resources.getDrawable(R.drawable.signup_et_area, null)
            tv_email_error.setInVisible()
            passwordController()
        }
    }

    private fun passwordController() {
        val tv_pass = binding.tvPasswd
        val et_pass = binding.etSignupPasswd
        val tv_pass_error = binding.tvPwError

        tv_pass.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
        et_pass.background = resources.getDrawable(R.drawable.signup_et_area_error, null)
        tv_pass_error.setVisible()

        if(et_pass.text.isEmpty()) {
            tv_pass_error.text = "비밀번호를  입력해 주세요"
        } else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", et_pass.text.toString())) {
            tv_pass_error.text = "영문 + 숫자 6자리 이상 입력해 주세요"
        } else {
            tv_pass.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            et_pass.background = resources.getDrawable(R.drawable.signup_et_area, null)
            tv_pass_error.setInVisible()
            passwordCheckController()
        }
    }

    private fun passwordCheckController() {
        val tv_pass_check = binding.tvPasswdCheck
        val et_pass = binding.etSignupPasswd
        val et_pass_check = binding.etSignupPwCheck
        val tv_passck_error = binding.tvPwckError

        tv_pass_check.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
        et_pass_check.background = resources.getDrawable(R.drawable.signup_et_area_error, null)
        tv_passck_error.setVisible()

        if(et_pass_check.text.isEmpty()) {
            tv_passck_error.text = "비밀번호를 다시 입력해 주세요"
        } else if(et_pass.text.toString() != et_pass_check.text.toString()) {
            tv_passck_error.text = "비밀번호가 일치하지 않습니다"
        } else {
            tv_pass_check.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            et_pass_check.background = resources.getDrawable(R.drawable.signup_et_area, null)
            tv_passck_error.setInVisible()

            checkboxController()
        }
    }

    private fun checkboxController() {
        if(!binding.checkboxPrivacy.isChecked) {
            binding.tvCbPrivacyError1.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
            binding.tvCbPrivacyError2.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
        }

        if(!binding.checkboxService.isChecked) {
            binding.tvCbServiceError1.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
            binding.tvCbServiceError2.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
        }

        if(binding.checkboxPrivacy.isChecked && binding.checkboxService.isChecked) {
            postSignUp()
        }
    }

    private fun postSignUp() {
        RequestToServer.service.postSignUp(
            RequestUserData(
                email = binding.etSignupEmail.text.toString(),
                password = binding.etSignupPasswd.text.toString()
            )
        ).enqueue(object : Callback<ResponseUserData> {
            override fun onResponse(
                call: Call<ResponseUserData>,
                response: Response<ResponseUserData>
            ) {
                when {
                    response.code() == 201 -> {
                        // 토큰 저장
                        SharedPreferenceController.setAccessToken(applicationContext, response.body()!!.data.token)

                        // 홈으로 이동
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                    response.code() == 400 -> {
                        Log.d("postSignUp 400", response.message())
                    }
                    else -> {
                        Log.d("postSignUp 500", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d("postSignUp ERROR", "$t")
            }

        })
    }

    private fun EditText.etFocusListener(tv : TextView, tv_error : TextView, button : ImageView) {
        this.setOnFocusChangeListener { _, hasFocus ->
            tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            this.background = resources.getDrawable(R.drawable.signup_et_area, null)
            tv_error.setInVisible()

            if(this.text.isNotEmpty()) {
                this.clearText(button)
            }

            if(!hasFocus) {
                button.setGone()
                emailController()
            }
        }
    }

    private fun EditText.checkPassword(button: ImageView) {

        val tv_pass_check = binding.tvPasswdCheck
        val et_pass = binding.etSignupPasswd
        val et_pass_check = binding.etSignupPwCheck
        val tv_passck_error = binding.tvPwckError

        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(et_pass_check.text.isNotEmpty()) {
                    et_pass_check.clearText(button)

                    if(et_pass.text.toString() != et_pass_check.text.toString()) {
                        tv_pass_check.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
                        et_pass_check.background = resources.getDrawable(R.drawable.signup_et_area_error, null)
                        tv_passck_error.setVisible()
                        tv_passck_error.text = "비밀번호가 일치하지 않습니다"
                    } else {
                        tv_pass_check.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
                        et_pass_check.background = resources.getDrawable(R.drawable.signup_et_area, null)
                        tv_passck_error.setInVisible()
                    }
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