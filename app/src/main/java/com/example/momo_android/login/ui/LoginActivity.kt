package com.example.momo_android.login.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.momo_android.databinding.ActivityDiaryBinding
import com.example.momo_android.databinding.ActivityLoginBinding
import com.example.momo_android.home.ui.HomeActivity
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.signup.data.RequestUserData
import com.example.momo_android.signup.data.ResponseUserData
import com.example.momo_android.signup.ui.SignUpActivity
import com.example.momo_android.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val btn_login = binding.btnLogin
        val et_email = binding.etEmail
        val et_passwd = binding.etPasswd
        val tv_login_alert = binding.tvLoginAlert

        btn_login.setOnClickListener {
            postLogin()
        }

        binding.btnGoSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnFindPw.setOnClickListener {
            val intent = Intent(this, FindPasswordActivity::class.java)
            startActivity(intent)
        }

        et_email.onFocusChangeListener = editTextFocusChangeListener
        et_passwd.onFocusChangeListener = editTextFocusChangeListener

        et_email.setOnClickListener(editTextOnClickListener)
        et_passwd.setOnClickListener(editTextOnClickListener)

    }

    private val editTextOnClickListener = View.OnClickListener {
        binding.tvLoginAlert.setGone()
        binding.etEmail.isCursorVisible = true
        binding.etPasswd.isCursorVisible = true
    }

    private val editTextFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        binding.tvLoginAlert.setGone()
        binding.etEmail.isCursorVisible = true
        binding.etPasswd.isCursorVisible = true
    }

    private fun postLogin() {
        RequestToServer.service.postLogin(
            RequestUserData(
                email = binding.etEmail.text.toString(),
                password = binding.etPasswd.text.toString()
            )
        ).enqueue(object : Callback<ResponseUserData> {
            override fun onResponse(
                call: Call<ResponseUserData>,
                response: Response<ResponseUserData>
            ) {
                when {
                    response.code() == 200 -> {
                        // 토큰 저장
                        SharedPreferenceController.setAccessToken(applicationContext,
                            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTYxMDI4NTcxOCwiZXhwIjoxNjE4MDYxNzE4LCJpc3MiOiJtb21vIn0.BudOmb4xI78sbtgw81wWY8nfBD2A6Wn4vS4bvlzSZYc")
                        // 유저 아이디 저장
                        //SharedPreferenceController.setUserId(applicationContext, response.body()!!.data.user.id)
                        SharedPreferenceController.setUserId(applicationContext, 2)

                        // 홈으로 이동
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                    response.code() == 400 -> {
                        Log.d("postSignUp 400", response.message())
                        binding.etEmail.isCursorVisible = false
                        binding.etPasswd.isCursorVisible = false
                        if(binding.etEmail.text.isNotEmpty() || binding.etPasswd.text.isNotEmpty()) {
                            binding.etEmail.unshowKeyboard()
                            binding.tvLoginAlert.setVisible()
                        }
                    }
                    else -> {
                        Log.d("postSignUp 500", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d("postLogin ERROR", "$t")
            }

        })
    }

}