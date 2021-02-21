package com.momo.momo_android.login.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityFindPasswordBinding
import com.momo.momo_android.login.data.RequestTempPasswordData
import com.momo.momo_android.login.data.ResponseTempPasswordData
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.util.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnFindClose.setOnClickListener {
            finish()
        }

        checkEmailCondition()

        setEmailListeners()

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setDefault() {
        binding.apply {
            tvFindpwEmail.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            etFindpwEmail.background = resources.getDrawable(R.drawable.et_area_default, null)
            tvEmailError.setInVisible()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setRedError() {
        binding.apply {
            tvFindpwEmail.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
            etFindpwEmail.background = resources.getDrawable(R.drawable.et_area_error, null)
            tvEmailError.setVisible()
        }
    }

    private fun checkEmailCondition() {
        binding.apply {
            btnFindPasswd.setOnClickListener {
                if(etFindpwEmail.text.isEmpty()) {
                    // 입력창이 비어있을 때
                    btnEmailErase.isEnabled = false
                } else if(etFindpwEmail.text.isNotEmpty() &&
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(etFindpwEmail.text.toString()).matches()) {
                    // 이메일 정규식이 만족하지 않을 때
                    setRedError()
                    tvEmailError.text = "올바른 이메일 형식이 아닙니다"
                } else {
                    progressBar.setVisible()
                    postTempPassword()
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setEmailListeners() {
        binding.apply {
            etFindpwEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    setDefault()

                    if (etFindpwEmail.text.isNotEmpty()) {
                        etFindpwEmail.clearText(btnEmailErase)
                        btnFindPasswd.background = resources.getDrawable(R.drawable.btn_active, null)
                        btnFindPasswd.isEnabled = true
                    } else {
                        btnFindPasswd.background = resources.getDrawable(R.drawable.btn_inactive, null)
                        btnFindPasswd.isEnabled = false
                    }

                }

                override fun afterTextChanged(p0: Editable?) {}

            })

            etFindpwEmail.setOnFocusChangeListener { _, _ ->
                setDefault()
            }
        }
    }

    private fun postTempPassword() {
        RequestToServer.service.postTempPassword(
            Authorization = SharedPreferenceController.getAccessToken(applicationContext),
            RequestTempPasswordData(
                email = binding.etFindpwEmail.text.toString()
            )
        ).enqueue(object : Callback<ResponseTempPasswordData> {
            override fun onResponse(
                call: Call<ResponseTempPasswordData>,
                response: Response<ResponseTempPasswordData>
            ) {
                binding.progressBar.setGone()
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        val findModal = ModalFindpwCount(this@FindPasswordActivity)
                        findModal.start(it.data.tempPasswordIssueCount)
                    } ?: showError(response.errorBody())

            }

            override fun onFailure(call: Call<ResponseTempPasswordData>, t: Throwable) {
                Log.d("postTempPassword ERROR", "$t")
            }

        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())

        when(ob.getString("message")) {
            "존재하지 않는 회원" -> {
                setRedError()
                binding.tvEmailError.text = "가입된 이메일이 없습니다"
            }
            else -> {
                val countOverModal = ModalFindpwCountOver(this@FindPasswordActivity)
                countOverModal.start()
            }
        }
    }
}