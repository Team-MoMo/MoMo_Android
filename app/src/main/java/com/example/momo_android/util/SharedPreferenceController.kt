package com.example.momo_android.util

import android.content.Context

object SharedPreferenceController {

    private val TOKEN = "TOKEN"
    private val ON_BOARDING = "ON_BOARDING"
    private val USER_ID = "USER_ID"


    // 토큰
    fun setAccessToken(context: Context, authorization: String?) {
        val pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("access_token", authorization)
        editor.apply()
    }

    fun getAccessToken(context: Context): String? {
        val pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        return pref.getString("access_token", "")
    }

    fun clearAccessToken(context: Context) {
        val pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }

    // userId 유저아이디 = 서버에 보내주는 Int값
    fun setUserId(context: Context, userId: Int) {
        val pref = context.getSharedPreferences(USER_ID, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt("USER_ID", userId)
        editor.apply()
    }

    fun getUserId(context: Context): Int {
        val pref = context.getSharedPreferences(USER_ID, Context.MODE_PRIVATE)
        return pref.getInt("USER_ID", 0)
    }

    fun clearUserId(context: Context) {
        val pref = context.getSharedPreferences(USER_ID, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
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