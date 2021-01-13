package com.example.momo_android.home.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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

    private var isDay = true
    private var diaryId = 0
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private val currentMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1)
    private val currentDate = Calendar.getInstance().get(Calendar.DATE)


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
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        setCurrentDate()
        setDayNightStatus()
        getServerDiaryData()
    }

    private fun setListeners() {
        viewBinding.apply {
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            buttonUpload.setOnClickListener(fragmentOnClickListener)
            buttonShowFull.setOnClickListener(fragmentOnClickListener)
            imageButtonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private fun setCurrentDate() {
        val currentDay = getCurrentDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
        viewBinding.textViewDate.text =
            "${currentYear}년\n${currentMonth}월 ${currentDate}일 ${currentDay}요일"
    }

    private fun getCurrentDay(currentDay: Int): String {
        return when (currentDay) {
            1 -> "일"
            2 -> "월"
            3 -> "화"
            4 -> "수"
            5 -> "목"
            6 -> "금"
            7 -> "토"
            else -> ""
        }
    }

    private fun setDayNightStatus() {
        val currentHourDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) // 24시간 포맷
        if (currentHourDay in 6..18) { // 06:00 ~ 18:59
            setDayView()
            isDay = true
        } else {
            setNightView()
            isDay = false
        }
    }

    private fun setDayView() {
        viewBinding.apply {
            constraintLayout.setBackgroundResource(R.drawable.gradient_home_day)
            imageViewSky.setImageResource(R.drawable.day_cloud)
            textViewDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_3))
            imageButtonMy.setImageResource(R.drawable.btn_ic_my_blue)
            textViewEmotion.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_2))
            imageViewLogo.setImageResource(R.drawable.ic_depth)
            textViewDepth.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_1))
            textViewQuotation.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_4))
            textViewAuthor.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_3))
            textViewTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_3))
            textViewPublisher.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_5_publish))
            viewLine.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.line_light_gray))
            textViewDiary.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_4))
            textViewDiaryEmpty.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_4))
            buttonUpload.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_1))
            buttonUpload.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue_7)
            buttonShowFull.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_1))
            buttonShowFull.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue_7)
            imageViewSea.setImageResource(R.drawable.day_sea)
        }
    }

    private fun setNightView() {
        viewBinding.apply {
            constraintLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_blue_grey))
            imageViewSky.setImageResource(R.drawable.night_star)
            textViewDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_7))
            imageButtonMy.setImageResource(R.drawable.btn_ic_my)
            textViewEmotion.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_4))
            imageViewLogo.setImageResource(R.drawable.ic_depth_white)
            textViewDepth.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_7))
            textViewQuotation.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_7))
            textViewAuthor.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_4))
            textViewTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_4))
            textViewPublisher.setTextColor(ContextCompat.getColor(requireContext(), R.color.black_5_publish))
            viewLine.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_3))
            textViewDiary.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_7))
            textViewDiaryEmpty.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_7))
            buttonUpload.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_7))
            buttonUpload.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue_1)
            buttonShowFull.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_7))
            buttonShowFull.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue_1)
            imageViewSea.setImageResource(R.drawable.night_sea)
        }
    }

    private fun getServerDiaryData() {
        var serverDiaryList = listOf<ResponseDiaryList.Data>()
        RequestToServer.service.getHomeDiaryList(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTYxMDI4NTcxOCwiZXhwIjoxNjE4MDYxNzE4LCJpc3MiOiJtb21vIn0.BudOmb4xI78sbtgw81wWY8nfBD2A6Wn4vS4bvlzSZYc",
            "filter",
            currentYear,
            currentMonth,
            currentDate,
            2
        ).enqueue(object : Callback<ResponseDiaryList> {
            override fun onResponse(
                call: Call<ResponseDiaryList>,
                responseList: Response<ResponseDiaryList>
            ) {
                when (responseList.code()) {
                    200 -> serverDiaryList = responseList.body()!!.data
                    400 -> Log.d("TAG", "onResponse: ${responseList.code()} + 필요한 값이 없습니다.")
                    500 -> Log.d("TAG", "onResponse: ${responseList.code()} + 일기 전체 조회 실패(서버 내부 에러)")
                    else -> Log.d("TAG", "onResponse: ${responseList.code()} + 예외 상황")
                }
                setServerDiaryData(serverDiaryList)
            }

            override fun onFailure(call: Call<ResponseDiaryList>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.localizedMessage}")
            }
        })
    }

    private fun setServerDiaryData(diaryList: List<ResponseDiaryList.Data>) {
        when (diaryList.size) {
            0 -> {
                setEmptyView()
                DIARY_STATUS = false
            }
            else -> {
                setDiaryView()
                diaryId = diaryList[0].id
                setEmotionData(diaryList[0].emotionId, isDay)
                setDepthData(diaryList[0].depth)
                setBookDiaryData(diaryList[0])
            }
        }
        fadeOutLoadingView()
    }

    private fun setEmptyView() { // visible invisible 처리
        viewBinding.apply {
            textViewDiaryEmpty.visibility = TextView.VISIBLE
            buttonUpload.visibility = Button.VISIBLE

            imageViewEmotion.visibility = ImageView.GONE
            textViewEmotion.visibility = TextView.GONE
            imageViewLogo.visibility = ImageView.GONE
            textViewDepth.visibility = TextView.GONE
            textViewQuotation.visibility = TextView.GONE
            textViewAuthor.visibility = TextView.GONE
            textViewTitle.visibility = TextView.GONE
            textViewPublisher.visibility = TextView.GONE
            viewLine.visibility = View.GONE
            textViewDiary.visibility = TextView.GONE
            buttonShowFull.visibility = Button.GONE
            imageButtonUpload.visibility = ImageButton.GONE
        }
    }

    private fun setDiaryView() {
        viewBinding.apply {
            textViewDiaryEmpty.visibility = TextView.GONE
            buttonUpload.visibility = Button.GONE

            imageViewEmotion.visibility = ImageView.VISIBLE
            textViewEmotion.visibility = TextView.VISIBLE
            imageViewLogo.visibility = ImageView.VISIBLE
            textViewDepth.visibility = TextView.VISIBLE
            textViewQuotation.visibility = TextView.VISIBLE
            textViewAuthor.visibility = TextView.VISIBLE
            textViewTitle.visibility = TextView.VISIBLE
            textViewPublisher.visibility = TextView.VISIBLE
            viewLine.visibility = View.VISIBLE
            textViewDiary.visibility = TextView.VISIBLE
            buttonShowFull.visibility = Button.VISIBLE
            imageButtonUpload.visibility = ImageButton.VISIBLE
        }
    }

    private fun setEmotionData(emotionId: Int, isDay: Boolean) {
        viewBinding.apply {
            when (emotionId) {
                1 -> {
                    when (isDay) {
                        true -> imageViewEmotion.setImageResource(R.drawable.and_ic_love_day)
                        false -> imageViewEmotion.setImageResource(R.drawable.and_ic_love_night)
                    }
                    textViewEmotion.text = "사랑"
                }
                2 -> {
                    when (isDay) {
                        true -> imageViewEmotion.setImageResource(R.drawable.and_ic_happy_day)
                        false -> imageViewEmotion.setImageResource(R.drawable.and_ic_happy_night)
                    }
                    textViewEmotion.text = "행복"
                }
                3 -> {
                    when (isDay) {
                        true -> imageViewEmotion.setImageResource(R.drawable.and_ic_console_day)
                        false -> imageViewEmotion.setImageResource(R.drawable.and_ic_console_night)
                    }
                    textViewEmotion.text = "위로"
                }
                4 -> {
                    when (isDay) {
                        true -> imageViewEmotion.setImageResource(R.drawable.and_ic_angry_day)
                        false -> imageViewEmotion.setImageResource(R.drawable.and_ic_angry_night)
                    }
                    textViewEmotion.text = "화남"
                }
                5 -> {
                    when (isDay) {
                        true -> imageViewEmotion.setImageResource(R.drawable.and_ic_sad_day)
                        false -> imageViewEmotion.setImageResource(R.drawable.and_ic_sad_night)
                    }
                    textViewEmotion.text = "슬픔"
                }
                6 -> {
                    when (isDay) {
                        true -> imageViewEmotion.setImageResource(R.drawable.and_ic_bored_day)
                        false -> imageViewEmotion.setImageResource(R.drawable.and_ic_bored_night)
                    }
                    textViewEmotion.text = "우울"
                }
                7 -> {
                    when (isDay) {
                        true -> imageViewEmotion.setImageResource(R.drawable.and_ic_memory_day)
                        false -> imageViewEmotion.setImageResource(R.drawable.and_ic_memory_night)
                    }
                    textViewEmotion.text = "추억"
                }
                8 -> {
                    when (isDay) {
                        true -> imageViewEmotion.setImageResource(R.drawable.and_ic_daily_day)
                        false -> imageViewEmotion.setImageResource(R.drawable.and_ic_daily_night)
                    }
                    textViewEmotion.text = "일상"
                }
                else -> Log.d("TAG", "setEmotionData: unknown emotion")
            }
        }
    }

    private fun setDepthData(depth: Int) {
        viewBinding.apply {
            when (depth) {
                1 -> textViewDepth.text = "2m"
                2 -> textViewDepth.text = "30m"
                3 -> textViewDepth.text = "100m"
                4 -> textViewDepth.text = "300m"
                5 -> textViewDepth.text = "700m"
                6 -> textViewDepth.text = "1,0005m"
                7 -> textViewDepth.text = "심해"
                else -> Log.d("TAG", "setEmotionData: unknown depth")
            }
        }
    }

    private fun setBookDiaryData(data: ResponseDiaryList.Data) {
        viewBinding.apply {
            textViewQuotation.text = data.sentence.contents
            textViewAuthor.text = data.sentence.writer
            textViewTitle.text = "<${data.sentence.bookName}>"
            textViewPublisher.text = "(${data.sentence.publisher})"
            textViewDiary.text = data.contents
        }
    }

    private fun fadeOutLoadingView() {
        viewBinding.viewLoading.apply {
            alpha = 1f
            animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        viewBinding.viewLoading.visibility = View.GONE
                    }
                })
        }
    }

    private val fragmentOnClickListener = View.OnClickListener {
        viewBinding.apply {
            when (it.id) {
                imageButtonMy.id -> Log.d("TAG", "clicked: ")
                buttonShowFull.id -> setIntentToDiaryActivity()
                buttonUpload.id -> setIntentToUploadActivity()
                imageButtonUpload.id -> setIntentToUploadActivity()
                imageButtonList.id -> setIntentToListActivity()
            }
        }
    }

    private fun setIntentToDiaryActivity() {
        val intent = Intent(requireContext(), DiaryActivity::class.java)
        intent.putExtra("diaryId", diaryId)
        startActivity(intent)
    }

    private fun setIntentToUploadActivity() {
        val intent = Intent(requireContext(), UploadFeelingActivity::class.java)
        intent.putExtra("intentFrom", "Home -> Upload")
        intent.putExtra("diaryStatus", DIARY_STATUS)
        startActivity(intent)
    }

    private fun setIntentToListActivity() {
        val intent = Intent(requireContext(), ListActivity::class.java)
        intent.putExtra("year", currentYear)
        intent.putExtra("month", currentMonth)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        var DIARY_STATUS = true
    }
}
