package com.momo.momo_android.signup.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivitySignUpBinding
import com.momo.momo_android.home.ui.HomeActivity
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.setting.ui.PrivacyPolicyActivity
import com.momo.momo_android.setting.ui.TermsOfServiceActivity
import com.momo.momo_android.signup.data.RequestUserData
import com.momo.momo_android.signup.data.ResponseUserData
import com.momo.momo_android.util.*
import okhttp3.ResponseBody
import org.json.JSONObject
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

        applyButtons()

        setPasswordCheckListener()

        setListeners()

    }

    private fun applyButtons() {
        binding.apply {
            btnSignupClose.setOnClickListener {
                finish()
            }

            btnSignup.setOnClickListener {
                emailController()
            }

            btnGoPrivacy.setOnClickListener {
                changeIntent(PrivacyPolicyActivity())
            }

            btnGoService.setOnClickListener {
                changeIntent(TermsOfServiceActivity())
            }
        }
    }

    private fun setPasswordCheckListener() {
        binding.apply {
            etSignupPasswd.checkPassword()
            etSignupPwCheck.checkPassword()
        }
    }

    private fun setListeners() {
        binding.apply {
            etSignupEmail.editTextListener(tvEmail, tvEmailError, btnEmailErase)
            etSignupPasswd.editTextListener(tvPasswd, tvPwError, btnPwErase)
            etSignupPwCheck.editTextListener(tvPasswdCheck, tvPwckError, btnPwCheckErase)

            checkboxPrivacy.setOnClickListener(checkboxOnClickListener)
            checkboxService.setOnClickListener(checkboxOnClickListener)
        }
    }

    private val checkboxOnClickListener = View.OnClickListener {
        binding.apply {
            checkboxPrivacy.setCheckboxDefault(tvCbPrivacyError1, tvCbPrivacyError2, imgChPrivacyError)
            checkboxService.setCheckboxDefault(tvCbServiceError1, tvCbServiceError2, imgChServiceError)
        }
    }

    private fun emailController() {
        binding.apply {
            setRedError(tvEmail, etSignupEmail, tvEmailError)

            if (etSignupEmail.text.isEmpty()) {
                tvEmailError.text = "이메일을 입력해 주세요"
            } else if(etSignupEmail.text.isNotEmpty() &&
                !android.util.Patterns.EMAIL_ADDRESS.matcher(etSignupEmail.text.toString()).matches()) {
                tvEmailError.text = "올바른 이메일 형식이 아닙니다"
            } else {
                setDefault(tvEmail, etSignupEmail, tvEmailError)
                checkDuplicate() // 이메일 중복체크
            }
        }
    }

    private fun passwordController() {
        binding.apply {
            setRedError(tvPasswd, etSignupPasswd, tvPwError)

            if (etSignupPasswd.text.isEmpty()) {
                tvPwError.text = "비밀번호를 입력해 주세요"
            } else if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$", etSignupPasswd.text.toString())) {
                tvPwError.text = "영문 + 숫자 6자리 이상 입력해 주세요"
            } else {
                setDefault(tvPasswd, etSignupPasswd, tvPwError)
                passwordCheckController()
            }
        }
    }

    private fun passwordCheckController() {

        binding.apply {
            setRedError(tvPasswdCheck, etSignupPwCheck, tvPwckError)

            if (etSignupPwCheck.text.isEmpty()) {
                tvPwckError.text = "비밀번호를 다시 입력해 주세요"
            } else if (etSignupPasswd.text.toString() != etSignupPwCheck.text.toString()) {
                tvPwckError.text = "비밀번호가 일치하지 않습니다"
            } else {
                setDefault(tvPasswdCheck, etSignupPwCheck, tvPwckError)
                checkboxController()
            }
        }
    }

    private fun checkboxController() {
        binding.apply {
            checkboxPrivacy.setCheckboxRedError(tvCbPrivacyError1, tvCbPrivacyError2, imgChPrivacyError)
            checkboxService.setCheckboxRedError(tvCbServiceError1, tvCbServiceError2, imgChServiceError)

            if (checkboxPrivacy.isChecked && checkboxService.isChecked) {
                postSignUp()
            }
        }
    }

    private fun CheckBox.setCheckboxDefault(tv: TextView, tv2: TextView, img: ImageView) {
        if (this.isChecked) {
            tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.black_2_nav))
            tv2.setTextColor(ContextCompat.getColor(applicationContext, R.color.black_2_nav))
            img.setImageResource(R.drawable.btn_arrow_right_black)
        }
    }

    private fun CheckBox.setCheckboxRedError(tv: TextView, tv2: TextView, img: ImageView) {
        if (!this.isChecked) {
            tv.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
            tv2.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_2_error))
            img.setImageResource(R.drawable.btn_arrow_right_red)
        }
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
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {
                        SharedPreferenceController.setAccessToken(applicationContext, it.data.token)
                        SharedPreferenceController.setUserId(applicationContext, it.data.user.id)
                        SharedPreferenceController.setPassword(applicationContext, it.data.user.password)

                        changeIntent(HomeActivity())
                        finishAffinity()
                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d("postSignUp ERROR", "$t")
            }

        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
    }

    private fun checkDuplicate() {
        RequestToServer.service.checkDuplicate(
            email = binding.etSignupEmail.text.toString()
        ).enqueue(object : Callback<ResponseUserData> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ResponseUserData>,
                response: Response<ResponseUserData>
            ) {
                when (response.code()) {
                    200 -> {
                        binding.apply { setDefault(tvEmail, etSignupEmail, tvEmailError) }
                        passwordController()
                    }
                    400 -> {
                        binding.apply {
                            setRedError(tvEmail, etSignupEmail, tvEmailError)
                            tvEmailError.text = "MOMO에 이미 가입된 이메일이에요!"
                        }
                    }
                    else -> Log.d("checkDuplicate 500", response.message())
                }
            }

            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d("checkDuplicate ERROR", "$t")
            }

        })
    }

    private fun EditText.editTextListener(tv: TextView, tv_error: TextView, button: ImageView) {

        // FocusChangedListener
        this.setOnFocusChangeListener { _, hasFocus ->
            setDefault(tv, this, tv_error)

            if (this.text.isNotEmpty()) {
                this.clearText(button)
            }

            if (!hasFocus) {
                button.setGone()
                emailController()
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


    private fun EditText.checkPassword() {

        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.apply {
                    if (etSignupPwCheck.text.isNotEmpty()) {
                        if (etSignupPasswd.text.toString() != etSignupPwCheck.text.toString()) {
                            setRedError(tvPasswdCheck, etSignupPwCheck, tvPwckError)
                            tvPwckError.text = "비밀번호가 일치하지 않습니다"
                        } else {
                            setDefault(tvPasswdCheck, etSignupPwCheck, tvPwckError)
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun changeIntent(activity: AppCompatActivity) {
        val intent = Intent(this@SignUpActivity, activity::class.java)
        startActivity(intent)
    }

}