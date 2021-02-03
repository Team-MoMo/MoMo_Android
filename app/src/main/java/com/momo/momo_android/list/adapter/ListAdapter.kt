package com.momo.momo_android.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momo.momo_android.R
import com.momo.momo_android.list.ui.ListActivity
import com.momo.momo_android.util.ItemClickListener
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter (private val context : Context) : RecyclerView.Adapter<ListViewHolder>() {

    var data = mutableListOf<ListData>()

    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(data[position])

        val aContext: ListActivity = ListActivity.mContext as ListActivity

        // 리사이클러뷰 아이템이 하나 혹은 0개일 때 스크롤 막기
        if (data.size == 0 || data.size == 1) {
            aContext.disableScroll()
        } else {
            aContext.enableScroll()
        }

        // > 버튼 클릭 시 다이어리 뷰로 전환
        holder.itemView.imagebutton_list_next.setOnClickListener {
            itemClickListener.onClickItem(it, position)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}