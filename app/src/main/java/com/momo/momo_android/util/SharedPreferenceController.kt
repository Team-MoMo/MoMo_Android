package com.momo.momo_android.util

import android.content.Context

object SharedPreferenceController {

    private val TOKEN = "TOKEN"
    private val ON_BOARDING = "ON_BOARDING"
    private val USER_ID = "USER_ID"
    private val PASSWORD = "PASSWORD"
    private val SOCIAL = "SOCIAL"
    private val PASSCODE = "PASSCODE"
    private val IS_LOCKED = "IS_LOCKED"


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


    // 비밀번호 변경 - 현재 비밀번호 체크
    fun setPassword(context: Context, passwd: String) {
        val pref = context.getSharedPreferences(PASSWORD, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("PASSWORD", passwd)
        editor.apply()
    }

    fun getPassword(context: Context): String? {
        val pref = context.getSharedPreferences(PASSWORD, Context.MODE_PRIVATE)
        return pref.getString("PASSWORD", "")
    }

    fun clearPassword(context: Context) {
        val pref = context.getSharedPreferences(PASSWORD, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }


    // 암호잠금 설정
    fun setLockStatus(context: Context, isLocked: Boolean) {
        val pref = context.getSharedPreferences(IS_LOCKED, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(IS_LOCKED, isLocked)
        editor.apply()
    }

    fun getLockStatus(context: Context): Boolean {
        val pref = context.getSharedPreferences(IS_LOCKED, Context.MODE_PRIVATE)
        return pref.getBoolean(IS_LOCKED, false)
    }

    fun setPassCode(context: Context, password: String) {
        val pref = context.getSharedPreferences(PASSCODE, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(PASSCODE, password)
        editor.apply()
    }

    fun getPassCode(context: Context): String? {
        val pref = context.getSharedPreferences(PASSCODE, Context.MODE_PRIVATE)
        return pref.getString(PASSCODE, "")
    }

    fun clearPassCode(context: Context) {
        val pref = context.getSharedPreferences(PASSCODE, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }


    // 온보딩 한번 체크
    fun setOnBoarding(context: Context, input: String) {
        val prefs = context.getSharedPreferences(ON_BOARDING, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("ON_BOARDING", input)
        editor.apply()
    }

    fun getOnBoarding(context: Context): String? {
        val prefs = context.getSharedPreferences(ON_BOARDING, Context.MODE_PRIVATE)
        return prefs.getString("ON_BOARDING", "")
    }


    // 소셜로그인으로 들어온 계정 체크
    fun setSocialLogin(context: Context, input: String) {
        val prefs = context.getSharedPreferences(SOCIAL, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("SOCIAL", input)
        editor.apply()
    }

    fun getSocialLogin(context: Context): String? {
        val prefs = context.getSharedPreferences(SOCIAL, Context.MODE_PRIVATE)
        return prefs.getString("SOCIAL", "")
    }

    fun clearSocialLogin(context: Context) {
        val pref = context.getSharedPreferences(SOCIAL, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }

    // 전체 삭제
    fun clearAll(context: Context) {
        clearAccessToken(context)
        clearUserId(context)
        clearPassword(context)
        clearSocialLogin(context)
        clearPassCode(context)
        setLockStatus(context, false)
    }
}