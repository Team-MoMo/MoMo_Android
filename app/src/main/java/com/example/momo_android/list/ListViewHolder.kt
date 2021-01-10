package com.example.momo_android.list

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.momo_android.R
import com.example.momo_android.util.UnderlineTextView

class ListViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val emotionImg : ImageView = itemView.findViewById(R.id.img_list_emotion)
    private val emotionText : TextView = itemView.findViewById(R.id.tv_list_emotion)
    private val date : TextView = itemView.findViewById(R.id.tv_list_date)
    private val day : TextView = itemView.findViewById(R.id.tv_list_day)
    private val depth : TextView = itemView.findViewById(R.id.tv_list_depth)
    private val sentence : TextView = itemView.findViewById(R.id.tv_list_book_sentence)
    private val bookInfo : TextView = itemView.findViewById(R.id.tv_list_book_info)
    private val diaryContent : UnderlineTextView = itemView.findViewById(R.id.tv_list_diary)

    @SuppressLint("SetTextI18n")
    fun onBind(data : ListData) {
        Glide.with(itemView).load(data.emotionImg).into(emotionImg)
        emotionText.text = data.emotionText
        date.text = data.date
        day.text = data.day
        depth.text = data.depth
        sentence.text = data.sentence
        bookInfo.text = TextUtils.concat(data.writer, "  ", data.bookTitle, "  ", setTextColor(data.publisher))
        diaryContent.text = data.diaryContent
    }

    @SuppressLint("ResourceAsColor")
    private fun setTextColor(string: String) : SpannableStringBuilder {
        val strChange = string
        val ssb = SpannableStringBuilder(strChange)
        ssb.setSpan(ForegroundColorSpan(R.color.black_5_publish), 0, strChange.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return ssb
    }

}