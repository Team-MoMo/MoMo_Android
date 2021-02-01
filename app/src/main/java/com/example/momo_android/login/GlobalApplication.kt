package com.example.momo_android.login

import android.app.Application
import com.kakao.auth.*

class GlobalApplication : Application() {

    private object KakaoSDKAdapter : KakaoAdapter() {
        /*
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         * @return Session의 설정값.
         */
        override fun getSessionConfig(): ISessionConfig {
            return object : ISessionConfig {
                override fun getAuthTypes(): Array<AuthType> {
                    return arrayOf(AuthType.KAKAO_LOGIN_ALL)
                    // 로그인을 하는 방식을 지정하는 부분.
                    // AuthType 로는 다음 네 가지 방식이 있다.
                    // KAKAO_TALK:
                    // 카카오톡으로 로그인, KAKAO_STORY: 카카오스토리로 로그인
                    // KAKAO_ACCOUNT:
                    // 웹뷰를 통한 로그인,
                    // KAKAO_TALK_EXCLUDE_NATIVE_LOGIN:
                    // 카카오톡으로만 로그인+계정 없으면 계정생성 버튼 제공
                    // KAKAO_LOGIN_ALL:
                    // 모든 로그인방식 사용 가능.
                    // 정확히는, 카카오톡이나 카카오스토리가 있으면 그 쪽으로 로그인 기능을 제공하고,
                    // 둘 다 없으면 웹뷰를 통한 로그인을 제공한다
                }

                override fun isUsingWebviewTimer(): Boolean {
                    return false
                    // SDK 로그인시 사용되는 WebView 에서
                    // pause 와 resume 시에 Timer 를 설정하여 CPU소모를 절약한다.
                    // true 를 리턴할경우 webview 로그인을 사용하는 화면에서
                    // 모든 webview 에 onPause 와 onResume 시에 Timer 를 설정해 주어야 한다.
                    // 지정하지 않을 시 false 로 설정된다.
                }

                override fun isSecureMode(): Boolean {
                    return false
                    // 로그인시 access token과 refresh token을 저장할 때의 암호화 여부를 결정한다.
                }

                override fun getApprovalType(): ApprovalType? {
                    return ApprovalType.INDIVIDUAL
                    // 일반 사용자가 아닌 Kakao와 제휴된 앱에서만 사용되는 값으로,
                    // 값을 채워주지 않을경우 ApprovalType.INDIVIDUAL 값을 사용하게 된다.
                }

                override fun isSaveFormData(): Boolean {
                    return true
                    // Kakao SDK 에서 사용되는 WebView에서
                    // email 입력폼의 데이터를 저장할지 여부를 결정한다.
                    // true일 경우, 다음번에 다시 로그인 시 email 폼을 누르면
                    // 이전에 입력했던 이메일이 나타난다.
                }
            }
        }

        override fun getApplicationConfig(): IApplicationConfig {
            return IApplicationConfig { getGlobalApplicationContext()?.applicationContext!! }
        }
    }

    companion object {
        var instance: GlobalApplication? = null

        fun getGlobalApplicationContext() : GlobalApplication? {
            checkNotNull(this) { "this application does not inherit com.kakao.GlobalApplication" }
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        KakaoSDK.init(KakaoSDKAdapter)
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }
}