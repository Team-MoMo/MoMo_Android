package com.example.momo_android.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.databinding.ItemScrollGradientBinding


class ScrollGradientAdapter(
    private val queryYear: Int,
    private val queryMonth: Int
) : RecyclerView.Adapter<ScrollGradientViewHolder>() {

    private var _viewBinding: ItemScrollGradientBinding? = null
    private val viewBinding get() = _viewBinding!!


    override fun getItemCount(): Int = 9
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollGradientViewHolder {
        _viewBinding = ItemScrollGradientBinding.inflate(LayoutInflater.from(parent.context))
        setMatchParentToItem()
        return ScrollGradientViewHolder(queryYear, queryMonth, viewBinding)
    }

    override fun onBindViewHolder(holder: ScrollGradientViewHolder, position: Int) {
        holder.onBind(position)
    }

    private fun setMatchParentToItem() {
        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        viewBinding.root.layoutParams = layoutParams
    }
}