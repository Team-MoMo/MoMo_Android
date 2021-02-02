package com.example.momo_android.lock.ui

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.momo_android.splash.SplashActivity
import com.example.momo_android.util.SharedPreferenceController


class AppLifecycleObserver(
    private val context: Context
) : LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        val isLocked = SharedPreferenceController.getLockStatus(context)
        if(isLocked) {
            Log.d("TAG", "onForeground: 02")
            val intent = Intent(context, LockOffActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }
}