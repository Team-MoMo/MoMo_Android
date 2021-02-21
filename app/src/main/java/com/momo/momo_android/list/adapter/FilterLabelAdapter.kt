package com.momo.momo_android.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momo.momo_android.R
import com.momo.momo_android.list.ui.ListActivity
import com.momo.momo_android.list.ui.ListActivity.Companion.filter_depth
import com.momo.momo_android.list.ui.ListActivity.Companion.filter_emotion
import com.momo.momo_android.list.ui.ListActivity.Companion.mContext
import kotlinx.android.synthetic.main.item_filter_label.view.*

class FilterLabelAdapter(private val context : Context) : RecyclerView.Adapter<FilterLabelViewHolder>() {

    var data = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterLabelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_filter_label, parent, false)
        return FilterLabelViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterLabelViewHolder, position: Int) {
        holder.onBind(data[position])

        holder.itemView.imagebutton_filter_delete.setOnClickListener {
            removeItem(position)
        }
    }

    override fun getItemCount(): Int = data.size

    // 필터 라벨 삭제
    private fun removeItem(position : Int) {

        if (filter_emotion != null || filter_depth != null) {
            if (filter_emotion != null && filter_depth == null) {
                filter_emotion = null
            }
            else if (filter_emotion == null && filter_depth != null) {
                filter_depth = null
            }
            else if (filter_emotion != null && filter_depth != null) {

                if (position == 0) {
                    //감정 라벨을 삭제했을 때
                    filter_emotion = null
                }
                else if (position == 1) {
                    //깊이 라벨을 삭제했을 때
                    filter_depth = null
                }
            }
        }

        data.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()

        val aContext : ListActivity = mContext

        aContext.activeFilterButton()

        aContext.loadFilteredData()
    }
}