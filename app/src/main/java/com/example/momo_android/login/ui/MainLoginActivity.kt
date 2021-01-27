package com.example.momo_android.login.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityMainLoginBinding
import com.example.momo_android.home.ui.HomeActivity
import com.example.momo_android.login.data.RequestSocialLoginData
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.signup.data.ResponseUserData
import com.example.momo_android.util.SharedPreferenceController
import com.example.momo_android.util.setVisible
import com.example.momo_android.util.unshowKeyboard
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_diary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainLoginBinding

    private val RC_SIGN_IN = 1111
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

        val btn_kakao = binding.btnLoginKakao
        val btn_google = binding.btnLoginGoogle
        val btn_momo = binding.btnLoginMomo

        btn_google.setOnClickListener {
            // 구글 로그인
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            val signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }

        btn_momo.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val depth = intent.getIntExtra("deep", 5)
        setDepthBackground(depth)

    }

    private fun setDepthBackground(depth: Int) {
        when (depth) {
            0 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep1, null)
            }
            1 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep2, null)
            }
            2 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep3, null)
            }
            3 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep4, null)
            }
            4 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep5, null)
            }
            5 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep6, null)
            }
            6 -> {
                binding.loginMainBg.background = resources.getDrawable(R.drawable.bg_deep7, null)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account!!.idToken

            postSocialLogin("google", idToken.toString())

        } catch (e: ApiException) {
            Log.w("구글로그인", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun postSocialLogin(socialName : String, accessToken : String) {
        RequestToServer.service.postSocialLogin(
            RequestSocialLoginData(
                socialName = socialName,
                accessToken = accessToken
            )
        ).enqueue(object : Callback<ResponseUserData> {
            override fun onResponse(
                call: Call<ResponseUserData>,
                response: Response<ResponseUserData>
            ) {
                Log.d("postSocialLogin", response.toString())
                when {
                    response.code() == 200 -> {
                        // 토큰 저장
                        SharedPreferenceController.setAccessToken(applicationContext, response.body()!!.data.token)
                        // 유저 아이디 저장
                        SharedPreferenceController.setUserId(applicationContext, response.body()!!.data.user.id)

                        // 홈으로 이동
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                    response.code() == 400 -> {
                        Log.d("postSocialLogin 400", response.message())
                    }
                    else -> {
                        Log.d("postSocialLogin 500", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d("postSocialLogin ERROR", "$t")
            }

        })
    }

}