package com.example.momo_android.list

import android.content.Context
import android.icu.util.Measure
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.R
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter (private val context : Context) : RecyclerView.Adapter<ListViewHolder>() {

    var data = mutableListOf<ListData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position])

        /*holder.itemView.constraintlayout_list.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        //holder.itemView.tv_list_diary.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val maxWidth = (holder.itemView.constraintlayout_list.measuredWidth * 0.68).toInt()
        Log.d("width", maxWidth.toString())

        if (data[position].diaryContent.length >= maxWidth) {
            val text = data[position].diaryContent.substring(0, maxWidth - 2)
            val edittext = "$text..."
            Log.d("test", edittext)
            data[position].diaryContent = edittext
        }*/

    }

    override fun getItemCount(): Int = data.size


}