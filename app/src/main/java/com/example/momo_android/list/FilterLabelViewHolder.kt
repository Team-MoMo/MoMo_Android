package com.example.momo_android.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.R

class FilterLabelViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val labelText : TextView = itemView.findViewById(R.id.tv_filter_label)

    fun onBind(data : FilterLabelData) {
        labelText.text = data.labelText
    }
}