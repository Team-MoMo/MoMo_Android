package com.momo.momo_android.home.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.momo.momo_android.R
import com.momo.momo_android.databinding.FragmentHomeBinding
import com.momo.momo_android.diary.ui.DiaryActivity
import com.momo.momo_android.home.data.ResponseDiaryList
import com.momo.momo_android.list.ui.ListActivity
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.setting.ui.SettingActivity
import com.momo.momo_android.splash.SplashActivity
import com.momo.momo_android.upload.ui.UploadFeelingActivity
import com.momo.momo_android.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private var isDay = true
    private var diaryId = 0
    private var diaryDepth = 0

    private lateinit var currentYear: String
    private lateinit var currentMonth: String
    private lateinit var currentDate: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    // UI 작업 수행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setCurrentDate()
        setDayNightStatus()
        getServerDiaryData()
        setOnBackPressedCallBack()
    }

    override fun onResume() {
        super.onResume()
        updateEditedData()
        if (!onBackPressedCallback.isEnabled) {
            onBackPressedCallback.isEnabled = true
        }
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.isEnabled = false
    }

    private fun setListeners() {
        binding.apply {
            fragmentOnClickListener.let {
                imageButtonMy.setOnClickListener(it)
                buttonUpload.setOnClickListener(it)
                buttonShowFull.setOnClickListener(it)
                imageButtonUpload.setOnClickListener(it)
                imageButtonList.setOnClickListener(it)
            }
        }
    }

    private fun setCurrentDate() {
        getCurrentDate().apply {
            currentYear = this[0]
            currentMonth = this[1]
            currentDate = this[2]
            binding.textViewDate.text =
                "${currentYear}년\n${currentMonth}월 ${currentDate}일 ${this[3]}"
        }
    }

    private fun setDayNightStatus() {
        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 6..18 -> setDayView()
            else -> setNightView()
        }
        setLoadingViewBackground()
    }

    private fun getServerDiaryData() {
        val call: Call<ResponseDiaryList> = RequestToServer.service.getHomeDiaryList(
            SharedPreferenceController.getAccessToken(requireContext()),
            SharedPreferenceController.getUserId(requireContext()),
            "filter",
            currentYear.toInt(),
            currentMonth.toInt(),
            currentDate.toInt()
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
                    true -> setDiaryView(response.body()!!.data)
                    false -> handleResponseStatusCode(response.code())
                }
            }
        })
    }

    private fun setOnBackPressedCallBack() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as HomeActivity).showFinishToast()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun updateEditedData() {
        if (UPDATE_HOME_FRAGMENT) {
            setCurrentDate()
            setDayNightStatus()
            getServerDiaryData()
            UPDATE_HOME_FRAGMENT = false
        }
    }

    private fun setDayView() {
        isDay = true
        binding.apply {
            constraintLayout.setBackgroundResource(R.drawable.gradient_home_day)
            imageViewSky.setImageResource(R.drawable.day_cloud)
            textViewDate.setContextCompatTextColor(R.color.blue_3)
            imageButtonMy.setImageResource(R.drawable.btn_ic_my_blue)
            textViewEmotion.setContextCompatTextColor(R.color.blue_2)
            imageViewLogo.setImageResource(R.drawable.ic_depth)
            textViewDepth.setContextCompatTextColor(R.color.blue_1)
            textViewQuotation.setContextCompatTextColor(R.color.black_4)
            textViewAuthor.setContextCompatTextColor(R.color.blue_3)
            textViewTitle.setContextCompatTextColor(R.color.blue_3)
            textViewPublisher.setContextCompatTextColor(R.color.black_5_publish)
            viewLine.setContextCompatBackgroundColor(R.color.line_light_gray)
            textViewDiary.setContextCompatTextColor(R.color.black_4)
            textViewDiaryEmpty.setContextCompatTextColor(R.color.black_4)
            buttonUpload.setContextCompatTextColor(R.color.blue_1)
            buttonUpload.setContextCompatBackgroundTintList(R.color.blue_7)
            buttonShowFull.setContextCompatTextColor(R.color.blue_1)
            buttonShowFull.setContextCompatBackgroundTintList(R.color.blue_7)
            imageViewSea.setImageResource(R.drawable.day_sea)
        }
    }

    private fun setNightView() {
        isDay = false
        binding.apply {
            constraintLayout.setContextCompatBackgroundColor(R.color.dark_blue_grey)
            imageViewSky.setImageResource(R.drawable.night_star)
            textViewDate.setContextCompatTextColor(R.color.blue_7)
            imageButtonMy.setImageResource(R.drawable.btn_ic_my)
            textViewEmotion.setContextCompatTextColor(R.color.blue_4)
            imageViewLogo.setImageResource(R.drawable.ic_depth_white)
            textViewDepth.setContextCompatTextColor(R.color.blue_7)
            textViewQuotation.setContextCompatTextColor(R.color.blue_7)
            textViewAuthor.setContextCompatTextColor(R.color.blue_4)
            textViewTitle.setContextCompatTextColor(R.color.blue_4)
            textViewPublisher.setContextCompatTextColor(R.color.black_5_publish)
            viewLine.setContextCompatBackgroundColor(R.color.blue_3)
            textViewDiary.setContextCompatTextColor(R.color.blue_7)
            textViewDiaryEmpty.setContextCompatTextColor(R.color.blue_7)
            buttonUpload.setContextCompatTextColor(R.color.blue_7)
            buttonUpload.setContextCompatBackgroundTintList(R.color.blue_1)
            buttonShowFull.setContextCompatTextColor(R.color.blue_7)
            buttonShowFull.setContextCompatBackgroundTintList(R.color.blue_1)
            imageViewSea.setImageResource(R.drawable.night_sea)
        }
    }

    private fun setDiaryView(diaryList: List<ResponseDiaryList.Data>) {
        when (diaryList.size) {
            0 -> {
                DIARY_STATUS = false
                setEmptyVisibility()
            }
            else -> {
                DIARY_STATUS = true
                setDiaryVisibility()
                setDiaryViewData(diaryList[0])
            }
        }
        fadeOutLoadingView()
    }

    private fun setDiaryViewData(diaryData: ResponseDiaryList.Data) {
        diaryId = diaryData.id
        setBookDiaryData(diaryData)
        setEmotionData(diaryData.emotionId, isDay)
        binding.textViewDepth.text = getDepthString(diaryData.depth, requireContext())
    }

    private fun handleResponseStatusCode(responseCode: Int) {
        binding.root.context.apply {
            when (responseCode) {
                400 -> showToast("일기 전체 조회 실패 - 필요한 값이 없습니다.")
                500 -> showToast("일기 전체 조회 실패 - 서버 내부 에러")
                else -> {
                    showToast("로그인 정보가 만료되어 재로그인이 필요합니다.")
                    SharedPreferenceController.clearAll(requireContext())
                    setIntentToSplashActivity()
                }
            }
        }
        setEmptyVisibility()
    }

    private fun fadeOutLoadingView() {
        binding.viewLoading.apply {
            alpha = 1f
            animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = View.GONE
                    }
                })
        }
    }

    private fun setLoadingViewBackground() {
        binding.viewLoading.apply {
            when (isDay) {
                true -> setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                false -> setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_blue_grey
                    )
                )
            }
            visibility = View.VISIBLE
        }
    }

    private fun setEmptyVisibility() {
        binding.apply {
            textViewDiaryEmpty.setVisible()
            buttonUpload.setVisible()

            imageViewEmotion.setGone()
            textViewEmotion.setGone()
            imageViewLogo.setGone()
            textViewDepth.setGone()
            textViewQuotation.setGone()
            textViewAuthor.setGone()
            textViewTitle.setGone()
            textViewPublisher.setGone()
            viewLine.setGone()
            textViewDiary.setGone()
            buttonShowFull.setGone()
        }
    }

    private fun setDiaryVisibility() {
        binding.apply {
            textViewDiaryEmpty.setGone()
            buttonUpload.setGone()

            imageViewEmotion.setVisible()
            textViewEmotion.setVisible()
            imageViewLogo.setVisible()
            textViewDepth.setVisible()
            textViewQuotation.setVisible()
            textViewAuthor.setVisible()
            textViewTitle.setVisible()
            textViewPublisher.setVisible()
            viewLine.setVisible()
            textViewDiary.setVisible()
            buttonShowFull.setVisible()
        }
    }

    private fun setEmotionData(emotionId: Int, isDay: Boolean) {
        binding.textViewEmotion.text = getEmotionString(emotionId, requireContext())
        binding.imageViewEmotion.apply {
            setImageResource(getEmotionWhite(emotionId))
            when (isDay) {
                true -> setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue_2))
                false -> setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue_4))
            }
        }
    }

    private fun setBookDiaryData(data: ResponseDiaryList.Data) {
        binding.apply {
            textViewQuotation.text = data.sentence.contents
            textViewAuthor.text = data.sentence.writer
            textViewTitle.text = "<${data.sentence.bookName}>"
            textViewPublisher.text = "(${data.sentence.publisher})"
            textViewDiary.text = data.contents
        }
    }

    private val fragmentOnClickListener = View.OnClickListener {
        binding.apply {
            when (it.id) {
                imageButtonMy.id -> setIntentToSettingActivity()
                buttonShowFull.id -> setIntentToDiaryActivity()
                buttonUpload.id -> setIntentToUploadActivity()
                imageButtonUpload.id -> setIntentToUploadActivity()
                imageButtonList.id -> {
//                    setIntentToListActivity()
                    requireContext().showToast("로그인 정보가 만료되어 재로그인이 필요합니다.")
                    SharedPreferenceController.clearAll(requireContext())
                    setIntentToSplashActivity()
                }
            }
        }
    }

    private fun setIntentToSettingActivity() {
        val intent = Intent(requireContext(), SettingActivity::class.java)
        startActivity(intent)
    }

    private fun setIntentToDiaryActivity() {
        val intent = Intent(requireContext(), DiaryActivity::class.java)
        intent.putExtra("diaryId", diaryId)
        intent.putExtra("diaryDepth", diaryDepth)
        startActivity(intent)
    }

    private fun setIntentToUploadActivity() {
        val intent = Intent(requireContext(), UploadFeelingActivity::class.java)
        intent.putExtra("diaryStatus", DIARY_STATUS)
        startActivity(intent)
    }

    private fun setIntentToListActivity() {
        val intent = Intent(requireContext(), ListActivity::class.java)
        intent.putExtra("year", currentYear.toInt())
        intent.putExtra("month", currentMonth.toInt())
        startActivity(intent)
    }

    private fun setIntentToSplashActivity() {
        Intent(requireContext(), SplashActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onBackPressedCallback.remove()
    }

    companion object {
        var DIARY_STATUS = true
        var UPDATE_HOME_FRAGMENT = true
    }
}
