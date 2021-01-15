package com.example.momo_android.upload.adapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.R
import com.example.momo_android.upload.data.UploadSentenceData

class UploadSentenceViewHolder (itemView:View):RecyclerView.ViewHolder(itemView){//val,var하면 선언과 동시에 초기화.

    private val author:TextView=itemView.findViewById(R.id.tv_author)
    private val book:TextView=itemView.findViewById(R.id.tv_book)
    private val publisher:TextView=itemView.findViewById(R.id.tv_publisher)
    private val sentence:TextView=itemView.findViewById(R.id.tv_sentence)
    private val cardview:ConstraintLayout=itemView.findViewById(R.id.cardview)

    fun onBind(data: UploadSentenceData) {//sampledata.kt가 객체로 들어오게됨.
        author.text = data.author
        book.text = data.book
        publisher.text = data.publisher
        sentence.text = data.sentence


        /*
        cardview.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val context: Context = v!!.context
                val intent = Intent(v!!.context, UploadWriteActivity::class.java)
                intent.putExtra("author", data.author)
                intent.putExtra("book", data.book)
                intent.putExtra("publisher", data.publisher)
                intent.putExtra("sentence", data.sentence)
                context.startActivity(intent)
            }
        })*/
    }

}