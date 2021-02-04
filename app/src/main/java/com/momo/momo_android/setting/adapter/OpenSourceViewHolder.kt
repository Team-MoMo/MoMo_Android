package com.momo.momo_android.setting.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.momo.momo_android.R
import com.momo.momo_android.setting.data.OpenSourceData

class OpenSourceViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){//val,var하면 선언과 동시에 초기화.

    private val name: TextView =itemView.findViewById(R.id.tv_open_source)

    fun onBind(data: OpenSourceData) {//sampledata.kt가 객체로 들어오게됨.
        name.text = data.name

    }

}