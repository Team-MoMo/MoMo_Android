package com.momo.momo_android.upload.adapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.momo.momo_android.R
import com.momo.momo_android.upload.data.UploadSentenceData

class UploadSentenceViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){//val,var하면 선언과 동시에 초기화.

    private val author:TextView=itemView.findViewById(R.id.tv_author)
    private val book:TextView=itemView.findViewById(R.id.tv_book)
    private val publisher:TextView=itemView.findViewById(R.id.tv_publisher)
    private val sentence:TextView=itemView.findViewById(R.id.tv_sentence)

    //데이터 묶는 함수 생성
    fun onBind(data: UploadSentenceData) {
        author.text = data.author
        book.text = data.book
        publisher.text = data.publisher
        sentence.text = data.sentence

    }

}