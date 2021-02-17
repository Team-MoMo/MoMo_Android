package com.momo.momo_android.login.ui

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

        binding.apply {

            btnLogin.setOnClickListener {
                postLogin()
            }

            btnLoginBack.setOnClickListener {
                finish()
            }

            btnGoSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }

            btnFindPw.setOnClickListener {
                val intent = Intent(this@LoginActivity, FindPasswordActivity::class.java)
                startActivity(intent)
            }

            etEmail.onFocusChangeListener = editTextFocusChangeListener
            etPasswd.onFocusChangeListener = editTextFocusChangeListener

            etEmail.setOnClickListener(editTextOnClickListener)
            etPasswd.setOnClickListener(editTextOnClickListener)

        }

    }

    private val editTextOnClickListener = View.OnClickListener {
        editTextListener()
    }

    private val editTextFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        editTextListener()
    }

    private fun editTextListener() {
        binding.apply {
            tvLoginAlert.setGone()
            etEmail.isCursorVisible = true
            etPasswd.isCursorVisible = true
        }
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
                        SharedPreferenceController.setAccessToken(applicationContext, response.body()!!.data.token)
                        SharedPreferenceController.setUserId(applicationContext, response.body()!!.data.user.id)
                        SharedPreferenceController.setPassword(applicationContext, binding.etPasswd.text.toString())

                        // 홈으로 이동
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
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