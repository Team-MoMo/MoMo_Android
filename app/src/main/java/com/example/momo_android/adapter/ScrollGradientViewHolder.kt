package com.example.momo_android.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.R
import com.example.momo_android.databinding.ItemScrollGradientBinding
import com.example.momo_android.util.OvalListeners


class ScrollGradientViewHolder(
    private val viewBinding: ItemScrollGradientBinding
) : RecyclerView.ViewHolder(viewBinding.root), OvalListeners {


    fun onBind(position: Int) {
        when (position) {
            0, 1, 2, 3, 4, 5, 6 -> {
                setOvalRecyclerView()
                setDepthViews(position)
            }
            7 -> {
                setLastItemView()
                Log.d("TAG", "onBind: last item binded")
            }
        }
    }

    private fun setOvalRecyclerView() {
        viewBinding.recyclerViewOval.adapter = ScrollOvalAdapter(this)
    }

    private fun setDepthViews(position: Int) {
        viewBinding.apply {
            constraintLayout.removeAllViews()
            when (position) {
                0 -> {
                    textViewDepth.text = "2m"
                    viewStubGradient.layoutResource = R.layout.view_stub_depth_1
                }
                1 -> {
                    textViewDepth.text = "30m"
                    viewStubGradient.layoutResource = R.layout.view_stub_depth_2
                }
                2 -> {
                    textViewDepth.text = "100m"
                    viewStubGradient.layoutResource = R.layout.view_stub_depth_3
                }
                3 -> {
                    textViewDepth.text = "300m"
                    viewStubGradient.layoutResource = R.layout.view_stub_depth_4
                }
                4 -> {
                    textViewDepth.text = "700m"
                    viewStubGradient.layoutResource = R.layout.view_stub_depth_5
                }
                5 -> {
                    textViewDepth.text = "1,005m"
                    viewStubGradient.layoutResource = R.layout.view_stub_depth_6
                }
                6 -> {
                    textViewDepth.text = "심해"
                    viewStubGradient.layoutResource = R.layout.view_stub_depth_7
                }
            }
            constraintLayout.addView(viewBinding.viewStubGradient)
            constraintLayout.addView(viewBinding.textViewDepth)
            constraintLayout.addView(viewBinding.recyclerViewOval)
            viewStubGradient.inflate()
        }
    }

    private fun setLastItemView() {
        viewBinding.apply {
            constraintLayout.removeAllViews()
            constraintLayout.addView(viewBinding.imageViewBottom)
        }
    }

    override fun onClickOvalItem(view: View, diaryData: String) {
        Log.d("TAG", "onClickOvalItem: clicked")
    }
}
