package com.momo.momo_android.home.adapter

import android.content.res.Resources
import android.util.DisplayMetrics
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.momo.momo_android.databinding.ItemScrollOvalBinding
import com.momo.momo_android.home.data.ResponseDiaryList
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class ScrollOvalViewHolder(
    private val binding: ItemScrollOvalBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var itemDistance by Delegates.notNull<Float>()
    private val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics


    fun onBind(diaryData: ResponseDiaryList.Data) {
        getItemAreaWidth()
        setOvalXPosition(diaryData.position)
        setDiaryData(diaryData)
    }

    fun onEmptyBind() {
        binding.apply {
            imageButtonOval.visibility = ImageButton.INVISIBLE
            textViewDate.visibility = TextView.INVISIBLE
            textViewCategory.visibility = TextView.INVISIBLE
        }
    }

    private fun getItemAreaWidth() {
        // {(디바이스 너비 - 아이템 너비 - 좌우여백) / (한 행당 아이템 최대 개수 - 1)} * 아이템 번호 + 좌측 여백
        val deviceWidthPixels = displayMetrics.widthPixels
        val itemWidthPixels = ITEM_SIZE * displayMetrics.density
        val horizontalMarginPixels = HORIZONTAL_MARGIN * 2 * displayMetrics.density
        itemDistance =
            (deviceWidthPixels.toFloat() - itemWidthPixels - horizontalMarginPixels) / (ITEM_AMOUNT - 1)
    }

    private fun setOvalXPosition(xPosition: Int) {
        val leftMargin = ((itemDistance * xPosition) + (HORIZONTAL_MARGIN * displayMetrics.density))
        val layoutParams = binding.imageButtonOval.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart = leftMargin.toInt()
        binding.imageButtonOval.layoutParams = layoutParams
    }

    private fun setDiaryData(diaryData: ResponseDiaryList.Data) {
        convertUpdatedAtToDate(diaryData.wroteAt)
        binding.textViewCategory.text = diaryData.emotion.name
    }

    private fun convertUpdatedAtToDate(updatedAt: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREAN)
        val parsedDate = dateFormat.parse(updatedAt)
        val diaryDate = SimpleDateFormat("MM/dd", Locale.KOREA).format(parsedDate!!)
        binding.textViewDate.text = diaryDate
    }

    companion object {
        const val ITEM_SIZE = 80
        const val HORIZONTAL_MARGIN = 36
        const val ITEM_AMOUNT = 10
    }
}
