package com.momo.momo_android.login.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityMainLoginBinding
import com.momo.momo_android.home.ui.HomeActivity
import com.momo.momo_android.login.data.RequestSocialLoginData
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.signup.data.ResponseUserData
import com.momo.momo_android.util.SharedPreferenceController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.momo.momo_android.util.setStatusBarTransparent
import com.momo.momo_android.util.showToast
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainLoginBinding

    private val GOOGLE_LOGIN = 1111

    private var googleSignInClient: GoogleSignInClient? = null
    private lateinit var callback: SessionCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setStatusBarTransparent(window)

        initLoginKakao()

        initLoginGoogle()

        initLoginMomo()

        initBackground()

    }

    private fun initLoginKakao() {
        binding.btnLoginKakao.setOnClickListener {
            callback = SessionCallback()
            Session.getCurrentSession().addCallback(callback)
            Session.getCurrentSession().open(AuthType.KAKAO_TALK, this)
            Session.getCurrentSession().checkAndImplicitOpen()
        }
    }

    private fun initLoginGoogle() {
        binding.btnLoginGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            val signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, GOOGLE_LOGIN)
        }
    }

    private fun initLoginMomo() {
        binding.btnLoginMomo.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initBackground() {
        val depth = intent.getIntExtra("depth", 5)
        setDepthBackground(depth)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setDepthBackground(depth: Int) {
        binding.apply {
            loginMainBg.background = when (depth) {
                0 -> resources.getDrawable(R.drawable.gradient_rectangle_depth1, null)
                1 -> resources.getDrawable(R.drawable.gradient_rectangle_depth2, null)
                2 -> resources.getDrawable(R.drawable.gradient_rectangle_depth3, null)
                3 -> resources.getDrawable(R.drawable.gradient_rectangle_depth4, null)
                4 -> resources.getDrawable(R.drawable.gradient_rectangle_depth5, null)
                5 -> resources.getDrawable(R.drawable.gradient_rectangle_depth6, null)
                6 -> resources.getDrawable(R.drawable.gradient_rectangle_depth7, null)
                else -> resources.getDrawable(R.drawable.gradient_rectangle_depth6, null)
            }
        }
    }

    // 구글 로그인
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account!!.idToken

            postSocialLogin("google", idToken.toString())

        } catch (e: ApiException) {
            Log.w("구글로그인", "signInResult:failed code=" + e.statusCode)
        }
    }


    // 카카오 로그인 콜백
    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            // 로그인 세션이 열렸을 때
            UserManagement.getInstance().me( object : MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    // 로그인이 성공했을 때
                    val accessToken = Session.getCurrentSession().tokenInfo
                    postSocialLogin("kakao", accessToken.accessToken)
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    // 로그인 도중 세션이 비정상적인 이유로 닫혔을 때
                    applicationContext.showToast("로그인 도중 오류가 발생했습니다. 다시 시도해주세요.")
                }
            })
        }
        override fun onSessionOpenFailed(exception: KakaoException?) {
            // 로그인 세션이 정상적으로 열리지 않았을 때
            if (exception != null) {
                com.kakao.util.helper.log.Logger.e(exception)
                applicationContext.showToast("로그인 도중 오류가 발생했습니다.")
            }
        }

    }

    // 소셜로그인 통신
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
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {

                        SharedPreferenceController.setAccessToken(applicationContext, it.data.token)
                        SharedPreferenceController.setUserId(applicationContext, it.data.user.id)
                        SharedPreferenceController.setSocialLogin(applicationContext, "true")

                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finishAffinity()

                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d("postSocialLogin ERROR", "$t")
            }

        })
    }

    private fun showError(error : ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        if (requestCode == GOOGLE_LOGIN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

}