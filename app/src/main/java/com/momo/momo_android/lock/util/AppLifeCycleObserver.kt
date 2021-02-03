package com.momo.momo_android.lock.util

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.momo.momo_android.lock.ui.LockOffActivity
import com.momo.momo_android.splash.SplashActivity.Companion.FROM_SPLASH
import com.momo.momo_android.util.SharedPreferenceController


class AppLifecycleObserver(
    private val context: Context
) : LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        val isLocked = SharedPreferenceController.getLockStatus(context)
        if (isLocked && !FROM_SPLASH) {
            val intent = Intent(context, LockOffActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }
}