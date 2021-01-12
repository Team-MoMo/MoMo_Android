package com.example.momo_android.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.databinding.ItemScrollOvalBinding
import com.example.momo_android.home.data.ResponseDiaryList
import com.example.momo_android.util.OvalListeners
import kotlinx.android.synthetic.main.item_scroll_oval.view.*
import java.util.*


class ScrollOvalAdapter(
    private val clickListener: OvalListeners,
    private val diaryList: List<ResponseDiaryList.Data>
) : RecyclerView.Adapter<ScrollOvalViewHolder>() {

    private var _viewBinding: ItemScrollOvalBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val itemCount = diaryList.size


    override fun getItemCount(): Int {
        return if (itemCount <= 4) 4
        else itemCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollOvalViewHolder {
        _viewBinding = ItemScrollOvalBinding.inflate(LayoutInflater.from(parent.context))
        setMatchParentToRecyclerView()
        return ScrollOvalViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ScrollOvalViewHolder, position: Int) {
        if (position < itemCount) {
            holder.onBind(position, diaryList[position])
            holder.itemView.imageButton_oval.setOnClickListener {
                clickListener.onClickOvalItem(it, "")
            }
        } else {
            holder.onEmptyBind()
        }
    }

    private fun setMatchParentToRecyclerView() {
        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        viewBinding.root.layoutParams = layoutParams
    }
}