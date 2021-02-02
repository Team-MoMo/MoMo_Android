package com.example.momo_android.home.adapter

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.R
import com.example.momo_android.databinding.ItemScrollGradientBinding
import com.example.momo_android.diary.ui.DiaryActivity
import com.example.momo_android.home.data.ResponseDiaryList
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.util.OvalClickListeners
import com.example.momo_android.util.SharedPreferenceController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScrollGradientViewHolder(
    private val queryYear: Int,
    private val queryMonth: Int,
    private val viewBinding: ItemScrollGradientBinding
) : RecyclerView.ViewHolder(viewBinding.root), OvalClickListeners {

    private var wholeDiaryList = listOf<ResponseDiaryList.Data>()


    fun onBind(position: Int) {
        when (position) {
            0 -> setFirstItemView()
            1, 2, 3, 4, 5, 6, 7 -> {
                setDepthViews(position - 1)
                getServerDiaryData(position - 1)
            }
            8 -> setLastItemView()
        }
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

    private fun getServerDiaryData(depth: Int) {
        RequestToServer.service.getScrollDiaryList(
            SharedPreferenceController.getAccessToken(viewBinding.root.context),
            SharedPreferenceController.getUserId(viewBinding.root.context),
            "depth",
            queryYear,
            queryMonth
        ).enqueue(object : Callback<ResponseDiaryList> {
            override fun onResponse(
                call: Call<ResponseDiaryList>,
                responseList: Response<ResponseDiaryList>
            ) {
                when (responseList.code()) {
                    200 -> wholeDiaryList = responseList.body()!!.data
                    400 -> Log.d("TAG", "onResponse: ${responseList.code()} + 필요한 값이 없습니다.")
                    500 -> Log.d("TAG", "onResponse: ${responseList.code()} + 일기 전체 조회 실패(서버 내부 에러)")
                    else -> Log.d("TAG", "onResponse: ${responseList.code()} + 예외 상황")
                }
                setOvalRecyclerView(depth, wholeDiaryList)
            }

            override fun onFailure(call: Call<ResponseDiaryList>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.localizedMessage}")
            }
        })
    }

    private fun setOvalRecyclerView(depth: Int, wholeDiaryList: List<ResponseDiaryList.Data>) {
        val depthDiaryList = sortServerDiaryData(depth, wholeDiaryList)
        viewBinding.recyclerViewOval.adapter = ScrollOvalAdapter(this, depthDiaryList)
    }

    private fun sortServerDiaryData(
        depth: Int,
        diaryList: List<ResponseDiaryList.Data>
    ): List<ResponseDiaryList.Data> {
        val sortedDiaryList = arrayListOf<ResponseDiaryList.Data>()
        for (i in diaryList.indices) {
            if (diaryList[i].depth == depth) {
                sortedDiaryList.add(diaryList[i])
            }
        }
        return sortedDiaryList
    }

    private fun setFirstItemView() {
        viewBinding.apply {
            constraintLayout.removeAllViews()
            constraintLayout.addView(this.viewLine)
        }
    }

    private fun setLastItemView() {
        viewBinding.apply {
            constraintLayout.removeAllViews()
            constraintLayout.addView(this.imageViewBottom)
        }
    }

    override fun onClickOvalItem(view: View, diaryId: Int, diaryDepth: Int) {
        val intent = Intent(view.context, DiaryActivity::class.java)
        intent.putExtra("diaryId", diaryId)
        intent.putExtra("diaryDepth", diaryDepth)
        view.context.startActivity(intent)
    }
}
