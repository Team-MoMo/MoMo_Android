package com.example.momo_android.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.momo_android.R
import com.example.momo_android.databinding.FragmentHomeBinding
import com.example.momo_android.diary.ui.DiaryActivity
import com.example.momo_android.home.data.ResponseDiaryList
import com.example.momo_android.list.ui.ListActivity
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.upload.ui.UploadFeelingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentHomeBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    // UI 작업 수행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setDayNightView()
        getServerDiaryData()
    }

//    private fun setDayNightView() {
//        setDayView(true)
//        setNightView(false)
//    }

    private fun setDayView(isEmptyView: Boolean) {
        viewBinding.apply {
            when (isEmptyView) {
                true -> {
                    setDayEmptyListeners()
                    includeHomeDayDiary.root.visibility = View.INVISIBLE
                    includeHomeDayEmpty.root.visibility = View.VISIBLE
                    includeHomeNightDiary.root.visibility = View.INVISIBLE
                    includeHomeNightEmpty.root.visibility = View.INVISIBLE

                }
                false -> {
                    setDayDiaryListeners()
                    includeHomeDayDiary.root.visibility = View.VISIBLE
                    includeHomeDayEmpty.root.visibility = View.INVISIBLE
                    includeHomeNightDiary.root.visibility = View.INVISIBLE
                    includeHomeNightEmpty.root.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setNightView(isEmptyView: Boolean) {
        viewBinding.apply {
            when (isEmptyView) {
                true -> {
                    setNightEmptyListeners()
                    includeHomeDayDiary.root.visibility = View.INVISIBLE
                    includeHomeDayEmpty.root.visibility = View.INVISIBLE
                    includeHomeNightDiary.root.visibility = View.INVISIBLE
                    includeHomeNightEmpty.root.visibility = View.VISIBLE

                }
                false -> {
                    setNightDiaryListeners()
                    includeHomeDayDiary.root.visibility = View.INVISIBLE
                    includeHomeDayEmpty.root.visibility = View.INVISIBLE
                    includeHomeNightDiary.root.visibility = View.VISIBLE
                    includeHomeNightEmpty.root.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setDayDiaryListeners() {
        viewBinding.includeHomeDayDiary.apply {
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            buttonShowFull.setOnClickListener(fragmentOnClickListener)
            imageButtonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private fun setDayEmptyListeners() {
        viewBinding.includeHomeDayEmpty.apply {
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            buttonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private fun setNightDiaryListeners() {
        viewBinding.includeHomeNightDiary.apply {
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            buttonShowFull.setOnClickListener(fragmentOnClickListener)
            imageButtonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private fun setNightEmptyListeners() {
        viewBinding.includeHomeNightEmpty.apply {
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            buttonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private val fragmentOnClickListener = View.OnClickListener {
        viewBinding.apply {
            when (it.id) {
                includeHomeDayDiary.imageButtonMy.id,
                includeHomeDayEmpty.imageButtonMy.id,
                includeHomeNightDiary.imageButtonMy.id,
                includeHomeNightEmpty.imageButtonMy.id -> {
                    Log.d("TAG", "clicked: ")
                }
                includeHomeDayDiary.buttonShowFull.id,
                includeHomeNightDiary.buttonShowFull.id -> {
                    setIntentToDiaryActivity()
                }
                includeHomeDayEmpty.buttonUpload.id,
                includeHomeNightEmpty.buttonUpload.id -> {
                    setIntentToUploadActivity()
                }
                includeHomeDayDiary.imageButtonUpload.id,
                includeHomeNightDiary.imageButtonUpload.id -> {
                    setIntentToUploadActivity()
                }
                includeHomeDayDiary.imageButtonList.id,
                includeHomeDayEmpty.imageButtonList.id,
                includeHomeNightDiary.imageButtonList.id,
                includeHomeNightEmpty.imageButtonList.id -> {
                    setIntentToListActivity()
                }
            }
        }
    }

    private fun setIntentToDiaryActivity() {
        val intent = Intent(requireContext(), DiaryActivity::class.java)
        startActivity(intent)
    }

    private fun setIntentToUploadActivity() {
        val intent = Intent(requireContext(), UploadFeelingActivity::class.java)
        startActivity(intent)
    }

    private fun setIntentToListActivity() {
        val intent = Intent(requireContext(), ListActivity::class.java)
        startActivity(intent)
    }

    private fun getServerDiaryData() {
        val currentCalendar = Calendar.getInstance()
        RequestToServer.service.getHomeDiaryList(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTYxMDI4NTcxOCwiZXhwIjoxNjE4MDYxNzE4LCJpc3MiOiJtb21vIn0.BudOmb4xI78sbtgw81wWY8nfBD2A6Wn4vS4bvlzSZYc",
            "filter",
            currentCalendar.get(Calendar.YEAR),
            currentCalendar.get(Calendar.MONTH) + 1,
            currentCalendar.get(Calendar.DATE),
            2
        ).enqueue(object : Callback<ResponseDiaryList> {
            override fun onResponse(
                call: Call<ResponseDiaryList>,
                responseList: Response<ResponseDiaryList>
            ) {
                when (responseList.code()) {
                    200 -> setServerDiaryData(responseList.body()!!.data)
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

    private fun setServerDiaryData(diaryListData: List<ResponseDiaryList.Data>) {
        if (diaryListData.isEmpty()) {
            setDayView(true)
        } else {
            setDayView(false)
            setEmotionData(diaryListData[0].emotionId)
            setDepthData(diaryListData[0].depth)
            setBookData(diaryListData[0].sentence)
            setDiaryData(diaryListData[0].contents)

        }
    }

    private fun setEmotionData(emotionId: Int) {
        viewBinding.apply {
            when (emotionId) {
                1 -> {
                    includeHomeDayDiary.imageViewCategory.setImageResource(R.drawable.and_ic_love_day)
                    includeHomeDayDiary.textViewCategory.text = "사랑"
                    includeHomeNightDiary.imageViewCategory.setImageResource(R.drawable.and_ic_love_night)
                    includeHomeNightDiary.textViewCategory.text = "사랑"
                }
                2 -> {
                    includeHomeDayDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_day)
                    includeHomeDayDiary.textViewCategory.text = "행복"
                    includeHomeNightDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_night)
                    includeHomeNightDiary.textViewCategory.text = "행복"
                }
                3 -> {
                    includeHomeDayDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_day)
                    includeHomeDayDiary.textViewCategory.text = "위로"
                    includeHomeNightDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_night)
                    includeHomeNightDiary.textViewCategory.text = "위로"
                }
                4 -> {
                    includeHomeDayDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_day)
                    includeHomeDayDiary.textViewCategory.text = "화남"
                    includeHomeNightDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_night)
                    includeHomeNightDiary.textViewCategory.text = "화남"
                }
                5 -> {
                    includeHomeDayDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_day)
                    includeHomeDayDiary.textViewCategory.text = "슬픔"
                    includeHomeNightDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_night)
                    includeHomeNightDiary.textViewCategory.text = "슬픔"
                }
                6 -> {
                    includeHomeDayDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_day)
                    includeHomeDayDiary.textViewCategory.text = "우울"
                    includeHomeNightDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_night)
                    includeHomeNightDiary.textViewCategory.text = "우울"
                }
                7 -> {
                    includeHomeDayDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_day)
                    includeHomeDayDiary.textViewCategory.text = "추억"
                    includeHomeNightDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_night)
                    includeHomeNightDiary.textViewCategory.text = "추억"
                }
                8 -> {
                    includeHomeDayDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_day)
                    includeHomeDayDiary.textViewCategory.text = "일상"
                    includeHomeNightDiary.imageViewCategory.setImageResource(R.drawable.and_ic_happy_night)
                    includeHomeNightDiary.textViewCategory.text = "일상"
                }
                else -> Log.d("TAG", "setEmotionData: unknown emotion")
            }
        }

    }

    private fun setDepthData(depth: Int) {
        viewBinding.apply {
            when (depth) {
                1 -> {
                    includeHomeDayDiary.textViewDepth.text = "2m"
                    includeHomeNightDiary.textViewDepth.text = "2m"
                }
                2 -> {
                    includeHomeDayDiary.textViewDepth.text = "30m"
                    includeHomeNightDiary.textViewDepth.text = "30m"
                }
                3 -> {
                    includeHomeDayDiary.textViewDepth.text = "100m"
                    includeHomeNightDiary.textViewDepth.text = "100m"
                }
                4 -> {
                    includeHomeDayDiary.textViewDepth.text = "300m"
                    includeHomeNightDiary.textViewDepth.text = "300m"
                }
                5 -> {
                    includeHomeDayDiary.textViewDepth.text = "700m"
                    includeHomeNightDiary.textViewDepth.text = "700m"
                }
                6 -> {
                    includeHomeDayDiary.textViewDepth.text = "1,0005m"
                    includeHomeNightDiary.textViewDepth.text = "1,005m"
                }
                7 -> {
                    includeHomeDayDiary.textViewDepth.text = "심해"
                    includeHomeNightDiary.textViewDepth.text = "심해"
                }
                else -> Log.d("TAG", "setEmotionData: unknown depth")
            }
        }
    }

    private fun setBookData(book: ResponseDiaryList.Data.Sentence) {
        viewBinding.apply {
            includeHomeDayDiary.textViewQuotation.text = book.contents
            includeHomeDayDiary.textViewAuthor.text = book.writer
            includeHomeDayDiary.textViewTitle.text = book.bookName
            includeHomeDayDiary.textViewPublisher.text = book.publisher
        }
    }

    private fun setDiaryData(contents: String) {
        viewBinding.apply {
            includeHomeDayDiary.textViewDiary.text = contents
            includeHomeNightDiary.textViewDiary.text = contents
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}