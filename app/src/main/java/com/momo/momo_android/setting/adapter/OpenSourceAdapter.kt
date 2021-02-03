package com.momo.momo_android.setting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityOpenSourceBinding
import com.momo.momo_android.setting.data.OpenSourceData
import com.momo.momo_android.util.ItemClickListener

class OpenSourceAdapter (private val context: Context) : RecyclerView.Adapter<OpenSourceViewHolder>(){
    var data= mutableListOf<OpenSourceData>()
    private lateinit var binding: ActivityOpenSourceBinding//뷰바인딩

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenSourceViewHolder {
        var view=
            LayoutInflater.from(context).inflate(R.layout.layout_open_source,parent,false)
        return OpenSourceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size //ppt에서 :Int =data.size랑 같은 의미. 줄일수 있음.
    }

    override fun onBindViewHolder(holder:OpenSourceViewHolder, position: Int) {
        holder.onBind(data[position])
        holder.itemView.setOnClickListener{
            itemClickListener.onClickItem(it,position)
        }
    }

    //클릭리스너 선언
    private lateinit var itemClickListener: ItemClickListener

    //클릭리스너 등록 메소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }



}