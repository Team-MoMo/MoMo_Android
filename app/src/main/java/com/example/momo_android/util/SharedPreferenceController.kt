package com.example.momo_android.util

import android.content.Context

object SharedPreferenceController {

    private val TOKEN = "TOKEN"
    private val MY_ACCOUNT = "MY_ACCOUNT"
    private val ON_BOARDING = "ON_BOARDING"
    private val CURRENT_TIME = "CURRENT_TIME"


    // 토큰
    fun setAccessToken(context: Context, authorization: String?) {
        val pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("access_token", authorization)
        editor.commit()
    }

    fun getAccessToken(context: Context): String? {
        val pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        return pref.getString("access_token", "")
    }


    // 자동로그인
    fun setMail(context: Context, input: String) {
        val prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("MAIL", input)
        editor.apply()
    }

    fun getMail(context: Context) : String? {
        val prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("MAIL", "")
    }

    fun setPasswd(context: Context, input: String) {
        val prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("PW", input)
        editor.apply()
    }

    fun getPasswd(context: Context) : String? {
        val prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("PW", "")
    }

    fun clearUser(context: Context) {
        val prefs = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }


    // 현재 시간 저장 - 토큰 갱신
    fun setCurrentTime(context: Context, input: String) {
        val prefs = context.getSharedPreferences(CURRENT_TIME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("CURRENT_TIME", input)
        editor.apply()
    }

    fun getCurrentTime(context: Context) : String? {
        val prefs = context.getSharedPreferences(CURRENT_TIME, Context.MODE_PRIVATE)
        return prefs.getString("CURRENT_TIME", "")
    }


    // 온보딩 한번 체크
    fun setOnBoarding(context: Context, input: String) {
        val prefs = context.getSharedPreferences(ON_BOARDING, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("ON_BOARDING", input)
        editor.apply()
    }

    fun getOnBoarding(context: Context) : String? {
        val prefs = context.getSharedPreferences(ON_BOARDING, Context.MODE_PRIVATE)
        return prefs.getString("ON_BOARDING", "")
    }


}