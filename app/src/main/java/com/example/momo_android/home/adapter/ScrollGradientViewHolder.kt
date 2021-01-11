package com.example.momo_android.home.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.R
import com.example.momo_android.databinding.ItemScrollGradientBinding
import com.example.momo_android.home.data.ResponseDiaryList
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.util.OvalListeners
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ScrollGradientViewHolder(
    private val viewBinding: ItemScrollGradientBinding
) : RecyclerView.ViewHolder(viewBinding.root), OvalListeners {

    private var diaryList = listOf<ResponseDiaryList.Data>()


    fun onBind(position: Int) {
        when (position) {
            0, 1, 2, 3, 4, 5, 6 -> {
                setDepthViews(position)
                getServerDiaryData(position)
                setOvalRecyclerView(position)
            }
            7 -> {
                setLastItemView()
            }
        }
    }

    private fun setOvalRecyclerView(position: Int) {
        viewBinding.recyclerViewOval.adapter = ScrollOvalAdapter(this, diaryList)
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

    private fun getServerDiaryData(position: Int) {
        val currentCalendar = Calendar.getInstance()
        RequestToServer.service.getScrollDiaryList(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTYxMDI4NTcxOCwiZXhwIjoxNjE4MDYxNzE4LCJpc3MiOiJtb21vIn0.BudOmb4xI78sbtgw81wWY8nfBD2A6Wn4vS4bvlzSZYc",
            2,
            currentCalendar.get(Calendar.YEAR),
            currentCalendar.get(Calendar.MONTH) + 1,
            "depth",
            position
        ).enqueue(object : Callback<ResponseDiaryList> {
            override fun onResponse(
                call: Call<ResponseDiaryList>,
                responseList: Response<ResponseDiaryList>
            ) {
                when (responseList.code()) {
                    200 -> diaryList = responseList.body()!!.data
                    400 -> Log.d("TAG", "onResponse: ${responseList.code()} + 필요한 값이 없습니다.")
                    500 -> Log.d("TAG", "onResponse: ${responseList.code()} + 일기 전체 조회 실패(서버 내부 에러)")
                    else -> Log.d("TAG", "onResponse: ${responseList.code()} + 예외 상황")
                }
            }

            override fun onFailure(call: Call<ResponseDiaryList>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.localizedMessage}")
            }
        })
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
