package com.momo.momo_android.util

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.momo.momo_android.R
import java.util.*

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

/* 키보드 제어 */
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

fun getDepthString(depthIdx : Int, context: Context): String {
    val res = context.resources
    return when (depthIdx) {
        0 -> res.getString(R.string.depth_2m)
        1 -> res.getString(R.string.depth_30m)
        2 -> res.getString(R.string.depth_100m)
        3 -> res.getString(R.string.depth_300m)
        4 -> res.getString(R.string.depth_700m)
        5 -> res.getString(R.string.depth_1005m)
        6 -> res.getString(R.string.depth_under)
        else -> "error"
    }
}

fun getEmotionString(emotionIdx: Int, context: Context): String {
    val res = context.resources
    return when (emotionIdx) {
        1 -> res.getString(R.string.emotion_love)
        2 -> res.getString(R.string.emotion_happy)
        3 -> res.getString(R.string.emotion_console)
        4 -> res.getString(R.string.emotion_angry)
        5 -> res.getString(R.string.emotion_sad)
        6 -> res.getString(R.string.emotion_bored)
        7 -> res.getString(R.string.emotion_memory)
        8 -> res.getString(R.string.emotion_daily)
        else -> "error"
    }
}

fun getEmotionBlack(emotionIdx: Int): Int {
    return when (emotionIdx) {
        1 -> R.drawable.ic_love_14_black
        2 -> R.drawable.ic_happy_14_black
        3 -> R.drawable.ic_console_14_black
        4 -> R.drawable.ic_angry_14_black
        5 -> R.drawable.ic_sad_14_black
        6 -> R.drawable.ic_bored_14_black
        7 -> R.drawable.ic_memory_14_black
        else -> R.drawable.ic_daily_14_black
    }
}

fun getEmotionWhite(emotionIdx: Int) : Int {
    return when (emotionIdx) {
        1 -> R.drawable.ic_love_14_white
        2 -> R.drawable.ic_happy_14_white
        3 -> R.drawable.ic_console_14_white
        4 -> R.drawable.ic_angry_14_white
        5 -> R.drawable.ic_sad_14_white
        6 -> R.drawable.ic_bored_14_white
        7 -> R.drawable.ic_memory_14_white
        else -> R.drawable.ic_daily_14_white
    }
}


//현재날짜 Year,Month,Date,Day String 배열로 전달
fun getCurrentDate(): Array<String>{
    val currentDate = Calendar.getInstance()
    val year = currentDate.get(Calendar.YEAR).toString()
    val month = (currentDate.get(Calendar.MONTH) + 1).toString()
    val date = (currentDate.get(Calendar.DATE)).toString()
    val dayInt= currentDate.get(Calendar.DAY_OF_WEEK)
    val day=getCurrentDay(dayInt)

    return arrayOf(year,month,date,day)
}

//현재날짜 Day의 Int형을 ~요일 형태로 변환
fun getCurrentDay(currentDay: Int): String {
    return when (currentDay) {
        1 -> "일요일"
        2 -> "월요일"
        3 -> "화요일"
        4 -> "수요일"
        5 -> "목요일"
        6 -> "금요일"
        7 -> "토요일"
        else -> ""
    }
}

/* 투명 status bar 설정 함수 */
fun setStatusBarTransparent(window: Window) {
    @Suppress("deprecation")
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    window.statusBarColor = Color.TRANSPARENT
}

/* edittext 지우는 x버튼 */
fun EditText.clearText(button: ImageView) {
    button.setVisible()
    button.setOnClickListener {
        this.setText("")
    }
}

fun View.setContextCompatBackgroundColor(color: Int) {
    setBackgroundColor(ContextCompat.getColor(this.context, color))
}

fun TextView.setContextCompatTextColor(color: Int) {
    setTextColor(ContextCompat.getColor(this.context, color))
}

fun View.setContextCompatBackgroundTintList(color: Int) {
    backgroundTintList = ContextCompat.getColorStateList(this.context, color)
}