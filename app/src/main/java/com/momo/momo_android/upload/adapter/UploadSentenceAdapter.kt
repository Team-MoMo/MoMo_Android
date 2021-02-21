package com.momo.momo_android.upload.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityUploadSentenceBinding
import com.momo.momo_android.upload.data.UploadSentenceData
import com.momo.momo_android.util.ItemClickListener

class UploadSentenceAdapter (private val context: Context) : RecyclerView.Adapter<UploadSentenceViewHolder>(){
    var data= mutableListOf<UploadSentenceData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadSentenceViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.layout_select_sentence_card,parent,false)
        return UploadSentenceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size //Int =data.size로 줄일수 있음.
    }

    //ViewHolder onBind
    override fun onBindViewHolder(holder: UploadSentenceViewHolder, position: Int) {
        holder.onBind(data[position])
        holder.itemView.setOnClickListener{
            itemClickListener.onClickItem(it,position)
        }
    }

    //클릭리스너 선언_인터페이스
    private lateinit var itemClickListener: ItemClickListener

    //클릭리스너 등록 메소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}