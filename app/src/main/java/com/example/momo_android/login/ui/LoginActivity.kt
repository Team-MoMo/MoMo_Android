package com.example.momo_android.login.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.example.momo_android.databinding.ActivityDiaryBinding
import com.example.momo_android.databinding.ActivityLoginBinding
import com.example.momo_android.util.setGone
import com.example.momo_android.util.setInVisible
import com.example.momo_android.util.setVisible
import com.example.momo_android.util.unshowKeyboard

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
            et_email.isCursorVisible = false
            et_passwd.isCursorVisible = false
            if(et_email.text.isNotEmpty() || et_passwd.text.isNotEmpty()) {
                et_email.unshowKeyboard()
                tv_login_alert.setVisible()
            }
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
}