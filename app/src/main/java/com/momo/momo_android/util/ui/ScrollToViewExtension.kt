package com.momo.momo_android.util.ui

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ScrollView
import androidx.core.animation.doOnEnd
import kotlin.math.abs

fun View.getThumb(): BitmapDrawable {

    this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val bitmap = Bitmap.createBitmap(
        this.measuredWidth,
        this.measuredHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.layout(0, 0, this.measuredWidth, this.measuredHeight)
    this.draw(canvas)

    return BitmapDrawable(resources, bitmap)
}


// scrollview 부드럽게 움직이도록
fun ScrollView.computeDistanceToView(view: View): Int {
    return abs(calculateRectOnScreen(this).top - (this.scrollY + calculateRectOnScreen(view).top))
}

fun calculateRectOnScreen(view: View): Rect {
    val location = IntArray(2)
    view.getLocationOnScreen(location)
    return Rect(
        location[0],
        location[1],
        location[0] + view.measuredWidth,
        location[1] + view.measuredHeight
    )
}

fun ScrollView.smoothScrollToView(
    view: View,
    marginTop: Int = 0,
    maxDuration: Long = 500L,
    onEnd: () -> Unit = {}
) {
    if (this.getChildAt(0).height <= this.height) {
        onEnd()
        return
    }
    val y = computeDistanceToView(view) - marginTop
    val ratio = abs(y - this.scrollY) / (this.getChildAt(0).height - this.height).toFloat()
    ObjectAnimator.ofInt(this, "scrollY", y).apply {
        duration = (maxDuration * ratio).toLong()
        interpolator = AccelerateDecelerateInterpolator()
        doOnEnd {
            onEnd()
        }
        start()
    }
}