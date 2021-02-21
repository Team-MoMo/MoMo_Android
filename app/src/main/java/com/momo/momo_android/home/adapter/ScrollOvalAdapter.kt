package com.momo.momo_android.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.momo.momo_android.databinding.ItemScrollOvalBinding
import com.momo.momo_android.home.data.ResponseDiaryList
import com.momo.momo_android.util.OvalClickListeners
import kotlinx.android.synthetic.main.item_scroll_oval.view.*


class ScrollOvalAdapter(
    private val clickListener: OvalClickListeners,
    private val diaryList: List<ResponseDiaryList.Data>
) : RecyclerView.Adapter<ScrollOvalViewHolder>() {

    private var _binding: ItemScrollOvalBinding? = null
    private val binding get() = _binding!!


    override fun getItemCount(): Int {
        return if (diaryList.size <= 4) 4
        else diaryList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollOvalViewHolder {
        _binding = ItemScrollOvalBinding.inflate(LayoutInflater.from(parent.context))
        setMatchParentToRecyclerView()
        return ScrollOvalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrollOvalViewHolder, position: Int) {
        if (position < diaryList.size) {
            holder.onBind(diaryList[position])
            holder.itemView.imageButton_oval.setOnClickListener {
                clickListener.onClickOvalItem(it, diaryList[position].id, diaryList[position].depth)
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
        binding.root.layoutParams = layoutParams
    }
}