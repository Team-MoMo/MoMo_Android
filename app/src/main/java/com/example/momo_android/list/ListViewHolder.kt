package com.example.momo_android.list

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
    private val writer : TextView = itemView.findViewById(R.id.tv_list_writer_name)
    private val bookTitle : TextView = itemView.findViewById(R.id.tv_list_book_title)
    private val publisher : TextView = itemView.findViewById(R.id.tv_list_book_publisher)
    private val diaryContent : UnderlineTextView = itemView.findViewById(R.id.tv_list_diary)

    fun onBind(data : ListData) {
        Glide.with(itemView).load(data.emotionImg).into(emotionImg)
        emotionText.text = data.emotionText
        date.text = data.date
        day.text = data.day
        depth.text = data.depth
        sentence.text = data.sentence
        writer.text = data.writer
        bookTitle.text = data.bookTitle
        publisher.text = data.publisher
        diaryContent.text = data.diaryContent
    }

}