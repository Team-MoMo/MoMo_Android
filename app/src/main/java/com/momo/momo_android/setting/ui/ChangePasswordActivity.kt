package com.momo.momo_android.setting.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityChangePasswordBinding
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.setting.ResponseWithdrawalData
import com.momo.momo_android.setting.data.RequestChangePasswordData
import com.momo.momo_android.util.*
import kotlinx.android.synthetic.main.activity_change_password.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        applyButtons()

        setListeners()

    }

    private fun applyButtons() {
        binding.apply {
            btnClose.setOnClickListener {
                finish()
            }

            btnChangePassword.setOnClickListener {
                nowPasswordController()
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            etNowPasswd.editTextListener(tvNowPasswd, tvNowPasswdError, btnNowPasswdErase)
            etNewPasswd.editTextListener(tvNewPasswd, tvNewPasswdError, btnNewPasswdErase)
            etCheckPasswd.editTextListener(tvCheckPasswd, tvCheckPasswdError, btnCheckPasswdErase)
        }
    }

    private fun nowPasswordController() {
        binding.apply {
            setRedError(tvNowPasswd, etNowPasswd, tvNowPasswdError)

            if (etNowPasswd.text.isEmpty()) {
                tvNowPasswdError.text = "현재 비밀번호를 입력해 주세요"
            } else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", etNowPasswd.text.toString())) {
                tvNowPasswdError.text = "영문 + 숫자 6자리 이상 입력해 주세요"
            } else if(SharedPreferenceController.getPassword(this@ChangePasswordActivity)
                != etNowPasswd.text.toString()) {
                tvNowPasswdError.text = "현재 비밀번호랑 일치하지 않습니다"
            } else {
                setDefault(tvNowPasswd, etNowPasswd, tvNowPasswdError)
                newPasswordController()
            }
        }
    }

    private fun newPasswordController() {
        binding.apply {
            setRedError(tvNewPasswd, etNewPasswd, tvNewPasswdError)

            if (etNewPasswd.text.isEmpty()) {
                tvNewPasswdError.text = "새로운 비밀번호를 입력해 주세요"
            } else if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", etNewPasswd.text.toString())) {
                tvNewPasswdError.text = "영문 + 숫자 6자리 이상 입력해 주세요"
            } else {
                setDefault(tvNewPasswd, etNewPasswd, tvNewPasswdError)
                checkPasswordController()
            }
        }
    }

    private fun checkPasswordController() {
        binding.apply {
            setRedError(tvCheckPasswd, etCheckPasswd, tvCheckPasswdError)

            if (etCheckPasswd.text.isEmpty()) {
                tvCheckPasswdError.text = "비밀번호를 다시 입력해 주세요"
            } else if(binding.etNewPasswd.text.toString() != etCheckPasswd.text.toString()) {
                tvCheckPasswdError.text = "비밀번호가 일치하지 않습니다"
            } else {
                setDefault(tvCheckPasswd, etCheckPasswd, tvCheckPasswdError)
                putChangePassword()
            }
        }
    }

    private fun EditText.editTextListener(tv: TextView, tv_error: TextView, button: ImageView) {

        // FocusChangedListener
        this.setOnFocusChangeListener { _, hasFocus ->
            setDefault(tv, this, tv_error)

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
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(this@editTextListener.text.isNotEmpty()) {
                    this@editTextListener.clearText(button)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setDefault(textView: TextView, editText: EditText, tvError: TextView) {
        binding.apply {
            textView.setTextColor(ContextCompat.getColor(applicationContext, R.color.blue_2))
            editText.background = resources.getDrawable(R.drawable.et_area_default, null)
            tvError.setInVisible()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setRedError(textView: TextView, editText: EditText, tvError: TextView) {
        binding.apply {
            textView.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
            editText.background = resources.getDrawable(R.drawable.et_area_error, null)
            tvError.setVisible()
        }
    }

    // 변경 통신
    private fun putChangePassword() {
        RequestToServer.service.putChangePassword(
            Authorization = SharedPreferenceController.getAccessToken(this),
            userId = SharedPreferenceController.getUserId(this),
            RequestChangePasswordData(newPassword = binding.etNewPasswd.text.toString())
        ).enqueue(object : Callback<ResponseWithdrawalData> {
            override fun onResponse(
                call: Call<ResponseWithdrawalData>,
                response: Response<ResponseWithdrawalData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        // 새 패스워드 저장
                        SharedPreferenceController.setPassword(
                            applicationContext,
                            binding.etNewPasswd.toString()
                        )
                        finish()
                        applicationContext.showToast("비밀번호가 변경되었습니다.")
                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseWithdrawalData>, t: Throwable) {
                Log.d("putChangePassword ERROR", "$t")
            }

        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
    }


}