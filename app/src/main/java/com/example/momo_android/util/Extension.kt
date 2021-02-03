package com.example.momo_android.util

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.text.Layout
import android.util.Log
import android.util.Xml
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.momo_android.R
import org.xmlpull.v1.XmlPullParser

/* 확장함수 */

/* show Toast */
fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

/* visibility */
fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setInVisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

/*키보드 숨기기*/
fun EditText.showKeyboard() {
    if (requestFocus()) {
        // edittext에 초점이 맞춰지면 키보드 올라옴
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        setSelection(text.length)
    }
}

fun EditText.unshowKeyboard() {
    if (requestFocus()) {
        // edittext에 초점이 맞춰지면 키보드 내려감
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(this.windowToken, 0)
        setSelection(text.length)
    }
}

/* 월을 2자리수로 변환해주는 함수 (6 -> 06)*/
fun getMonth(month: Int): String {
    return if (month in 1..9) {
        "0$month"
    } else {
        "$month"
    }
}

/* 일을 2자리수로 변환해주는 함수 (6 -> 06)*/
fun getDate(date: Int): String {
    return if (date in 1..9) {
        "0$date"
    } else {
        "$date"
    }
}

