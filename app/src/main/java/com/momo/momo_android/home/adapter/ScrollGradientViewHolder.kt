package com.momo.momo_android.home.adapter

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ItemScrollGradientBinding
import com.momo.momo_android.diary.ui.DiaryActivity
import com.momo.momo_android.home.data.ResponseDiaryList
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.util.OvalClickListeners
import com.momo.momo_android.util.SharedPreferenceController
import com.momo.momo_android.util.getDepthString
import com.momo.momo_android.util.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScrollGradientViewHolder(
    private val queryYear: Int,
    private val queryMonth: Int,
    private val binding: ItemScrollGradientBinding
) : RecyclerView.ViewHolder(binding.root), OvalClickListeners {


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
        binding.apply {
            constraintLayout.removeAllViews()
            textViewDepth.text = getDepthString(position, binding.root.context)
            when (position) {
                0 -> viewStubGradient.layoutResource = R.layout.view_stub_depth_1
                1 -> viewStubGradient.layoutResource = R.layout.view_stub_depth_2
                2 -> viewStubGradient.layoutResource = R.layout.view_stub_depth_3
                3 -> viewStubGradient.layoutResource = R.layout.view_stub_depth_4
                4 -> viewStubGradient.layoutResource = R.layout.view_stub_depth_5
                5 -> viewStubGradient.layoutResource = R.layout.view_stub_depth_6
                6 -> viewStubGradient.layoutResource = R.layout.view_stub_depth_7
            }
            constraintLayout.addView(binding.viewStubGradient)
            constraintLayout.addView(binding.textViewDepth)
            constraintLayout.addView(binding.recyclerViewOval)
            viewStubGradient.inflate()
        }
    }

    private fun getServerDiaryData(depth: Int) {
        val call: Call<ResponseDiaryList> = RequestToServer.service.getScrollDiaryList(
            SharedPreferenceController.getAccessToken(binding.root.context),
            SharedPreferenceController.getUserId(binding.root.context),
            "depth",
            queryYear,
            queryMonth
        )
        call.enqueue(object : Callback<ResponseDiaryList> {
            override fun onFailure(call: Call<ResponseDiaryList>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.localizedMessage}")
            }

            override fun onResponse(
                call: Call<ResponseDiaryList>,
                response: Response<ResponseDiaryList>
            ) {
                when (response.isSuccessful) {
                    true -> setOvalRecyclerView(depth, response.body()!!.data)
                    false -> handleResponseStatusCode(response.code())
                }
            }
        })
    }

    private fun setOvalRecyclerView(depth: Int, wholeDiaryList: List<ResponseDiaryList.Data>) {
        val depthDiaryList = sortServerDiaryData(depth, wholeDiaryList)
        binding.recyclerViewOval.adapter = ScrollOvalAdapter(this, depthDiaryList)
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

    private fun handleResponseStatusCode(responseCode: Int) {
        when (responseCode) {
            400 -> binding.root.context.showToast("일기 전체 조회 실패 - 필요한 값이 없습니다.")
            500 -> binding.root.context.showToast("일기 전체 조회 실패 - 서버 내부 에러")
            else -> binding.root.context.showToast("일기 전체 조회 실패 - 예외 상황")
        }
    }

    private fun setFirstItemView() {
        binding.apply {
            constraintLayout.removeAllViews()
            constraintLayout.addView(this.viewLine)
        }
    }

    private fun setLastItemView() {
        binding.apply {
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
