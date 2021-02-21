package com.momo.momo_android.login.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.momo.momo_android.databinding.ActivityLoginBinding
import com.momo.momo_android.home.ui.HomeActivity
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.signup.data.RequestUserData
import com.momo.momo_android.signup.data.ResponseUserData
import com.momo.momo_android.signup.ui.SignUpActivity
import com.momo.momo_android.util.*
import okhttp3.ResponseBody
import org.json.JSONObject
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

        applyButtons()

        setEditTextListeners()

    }

    private fun applyButtons() {
        binding.apply {
            btnLogin.setOnClickListener {
                postLogin()
            }

            btnLoginBack.setOnClickListener {
                finish()
            }

            btnGoSignup.setOnClickListener {
                changeIntent(SignUpActivity())
            }

            btnFindPw.setOnClickListener {
                changeIntent(FindPasswordActivity())
            }
        }
    }

    private fun setEditTextListeners() {
        binding.apply {
            etEmail.onFocusChangeListener = editTextFocusChangeListener
            etPasswd.onFocusChangeListener = editTextFocusChangeListener

            etEmail.setOnClickListener(editTextOnClickListener)
            etPasswd.setOnClickListener(editTextOnClickListener)
        }
    }

    private val editTextOnClickListener = View.OnClickListener {
        editTextListener()
    }

    private val editTextFocusChangeListener = View.OnFocusChangeListener { _, _ ->
        editTextListener()
    }

    private fun editTextListener() {
        binding.apply {
            tvLoginAlert.setGone()
            etEmail.isCursorVisible = true
            etPasswd.isCursorVisible = true
        }
    }

    private fun changeIntent(activity: AppCompatActivity) {
        val intent = Intent(this@LoginActivity, activity::class.java)
        startActivity(intent)
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
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        SharedPreferenceController.setAccessToken(applicationContext, it.data.token)
                        SharedPreferenceController.setUserId(applicationContext, it.data.user.id)
                        SharedPreferenceController.setPassword(applicationContext, binding.etPasswd.text.toString())

                        changeIntent(HomeActivity())
                        finishAffinity()
                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d("postLogin ERROR", "$t")
            }

        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Log.d("postSignUp 400", ob.getString("message"))

        binding.apply {
            etEmail.isCursorVisible = false
            etPasswd.isCursorVisible = false
            if(etEmail.text.isNotEmpty() || etPasswd.text.isNotEmpty()) {
                etEmail.unshowKeyboard()
                tvLoginAlert.setVisible()
            }
        }
    }

}