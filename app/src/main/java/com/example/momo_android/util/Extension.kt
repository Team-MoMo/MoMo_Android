package com.example.momo_android.util

import android.content.Context
import android.text.Layout
import android.util.Log
import android.util.Xml
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.momo_android.R
import org.xmlpull.v1.XmlPullParser

/* 확장함수 */

/* show Toast */
fun Context.showToast(msg : String) {
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

/* custom Toast */
/* 사용할 떄는 customToast(layoutInflater, R.layout.toast_ready, this) 이렇게 사용하면 돼요! */
fun customToast(layoutInflater : LayoutInflater, toastLayout : XmlPullParser, context : Context) {
    val customToast = layoutInflater.inflate(toastLayout, null)
    val toast = Toast(context)
    toast.duration = Toast.LENGTH_SHORT
    toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 0)
    toast.view = customToast
    toast.show()
}