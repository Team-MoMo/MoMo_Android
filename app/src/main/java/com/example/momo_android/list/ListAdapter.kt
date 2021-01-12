package com.example.momo_android.list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.R
import com.example.momo_android.diary.ui.DiaryActivity
import com.example.momo_android.list.ui.ListActivity
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter (private val context : Context) : RecyclerView.Adapter<ListViewHolder>() {

    var data = mutableListOf<ListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position])

        val aContext : ListActivity = ListActivity.mContext as ListActivity

        // 리사이클러뷰 아이템이 하나 혹은 0개일 때 스크롤 막기
        if (data.size == 0 || data.size == 1) {
            aContext.disableScroll()
        }
        else {
            aContext.enableScroll()
        }

        // > 버튼 클릭 시 다이어리 뷰로 전환
       holder.itemView.imagebutton_list_next.setOnClickListener {
           val intentFrom = "List -> Diary"
           val emotionText = data[position].emotionText
           val date = data[position].date
           val day = data[position].day
           val depth = data[position].depth
           val sentence = data[position].sentence
           val writer = data[position].writer
           val bookTitle = data[position].bookTitle
           val publisher = data[position].publisher
           val diaryContent = data[position].diaryContent

           val intent = Intent(holder.itemView.context, DiaryActivity::class.java)
           intent.putExtra("intentFrom", intentFrom)
           intent.putExtra("emotionText", emotionText)
           intent.putExtra("date", date)
           intent.putExtra("day", day)
           intent.putExtra("depth", depth)
           intent.putExtra("sentence", sentence)
           intent.putExtra("writer", writer)
           intent.putExtra("bookTitle", bookTitle)
           intent.putExtra("publisher", publisher)
           intent.putExtra("diaryContent", diaryContent)

           ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int = data.size

}