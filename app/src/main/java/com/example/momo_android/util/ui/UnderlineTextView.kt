package com.example.momo_android.util.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.annotation.IntDef
import com.example.momo_android.R
import com.example.momo_android.util.ui.UnderlineTextView.UnderLinePosition.Companion.POSITION_BASELINE
import com.example.momo_android.util.ui.UnderlineTextView.UnderLinePosition.Companion.POSITION_BELOW

class UnderlineTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(POSITION_BASELINE, POSITION_BELOW)
    annotation class UnderLinePosition {
        companion object {
            const val POSITION_BASELINE = 0
            const val POSITION_BELOW = 1
        }
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    var lineColor: Int
    get() = linePaint.color
    set(value) {
        if (linePaint.color != value) {
            linePaint.color = value
            invalidate()
        }
    }

    var lineHeight: Float
    get() = linePaint.strokeWidth
    set(value) {
        if (linePaint.strokeWidth != value) {
            linePaint.strokeWidth = value
            updateSpacing()
        }
    }

    var lineTopOffset = 0F
    set(value) {
        if (field != value) {
            field = value
            updateSpacing()
        }
    }

    @UnderLinePosition
    var linePosition = POSITION_BASELINE

    private val rect = Rect()

    private var internalAdd: Float = lineSpacingExtra

    private inline val extraSpace
    get() = lineTopOffset + lineHeight

    init {
        val density = context.resources.displayMetrics.density

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnderlineTextView, defStyleAttr, 0)
        lineColor = typedArray.getColor(R.styleable.UnderlineTextView_underlineColor, currentTextColor)
        lineTopOffset = typedArray.getDimension(R.styleable.UnderlineTextView_underlineOffset, 0f)
        lineHeight = typedArray.getDimension(R.styleable.UnderlineTextView_underlineHeight, density * 1)
        linePosition = typedArray.getInt(R.styleable.UnderlineTextView_underLinePosition, POSITION_BASELINE)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight + (extraSpace + 0.5f).toInt())
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        canvas?.takeIf { !text.isNullOrEmpty() }?.let {
            val count = lineCount
            val layout = layout
            var xStart: Float
            var xStop: Float
            var yStart: Float
            var firstCharInLine: Int
            var lastCharInLine: Int
            var lastLine: Boolean
            var offset: Int
            val lineSpacing = lineSpacingExtra * lineSpacingMultiplier
            for (i in 0 until count) {
                val baseline = getLineBounds(i, rect)
                lastLine = i == count - 1
                offset = if (lastLine) 0 else 1
                firstCharInLine = layout.getLineStart(i)
                lastCharInLine = layout.getLineEnd(i)
                xStart = layout.getPrimaryHorizontal(firstCharInLine)
                xStop = layout.getPrimaryHorizontal(lastCharInLine - offset)
                yStart = when (linePosition) {
                    POSITION_BASELINE -> baseline + lineTopOffset
                    POSITION_BELOW -> (rect.bottom + lineTopOffset) - if (lastLine) 0F else lineSpacing
                    else -> throw NotImplementedError("")
                }
                if (lastLine) {
                    canvas.drawRect(xStart, yStart, xStop, yStart + lineHeight, linePaint)
                } else {
                    canvas.drawRect(xStart, yStart, xStop + 35, yStart + lineHeight, linePaint)
                }
            }

            // (텍스트가 여러 줄인 경우) 텍스트가 버튼을 가리지 않게 적절히 잘리도록 함
            if (this.width * 2 <= this.paint.measureText(this.text.toString()).toInt()) {
                var splitedText = this.text.substring(0, layout.getLineEnd(1) - 4)
                splitedText = "$splitedText…"
                this.text = splitedText
            }
            // (텍스트가 한 줄인 경우) 텍스트가 버튼을 가리지 않고 바로 다음 줄로 넘어가도록 함
            else if ((this.width * 0.97 < this.paint.measureText(this.text.toString()).toInt()) &&
                    (this.width >= this.paint.measureText(this.text.toString()).toInt())) {
                val originText = StringBuffer(this.text)
                originText.insert(layout.getLineEnd(0) - 1, "\n")
                if (lineCount == 1) {
                    this.text = originText
                }
            }
        }
        super.onDraw(canvas)
    }

    private fun updateSpacing() {
        setLineSpacing(internalAdd, 1f)
    }

    override fun setLineSpacing(add: Float, mult: Float) {
        internalAdd = add
        super.setLineSpacing(add + extraSpace, 1f)
    }
}