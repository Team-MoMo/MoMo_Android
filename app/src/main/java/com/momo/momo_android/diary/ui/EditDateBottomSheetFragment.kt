package com.momo.momo_android.diary.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.NumberPicker
import android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE
import com.momo.momo_android.R
import com.momo.momo_android.databinding.BottomsheetDiaryEditDateBinding
import com.momo.momo_android.diary.data.RequestEditDiaryData
import com.momo.momo_android.diary.data.ResponseDiaryData
import com.momo.momo_android.diary.ui.DiaryActivity.Companion.diary_date
import com.momo.momo_android.diary.ui.DiaryActivity.Companion.diary_month
import com.momo.momo_android.diary.ui.DiaryActivity.Companion.diary_year
import com.momo.momo_android.home.data.ResponseDiaryList
import com.momo.momo_android.home.ui.ScrollFragment
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.util.SharedPreferenceController
import com.momo.momo_android.util.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.momo.momo_android.util.getCurrentDate
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class EditDateBottomSheetFragment(val itemClick: (IntArray) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetDiaryEditDateBinding? = null
    private val binding get() = _binding!!

    private lateinit var year: NumberPicker
    private lateinit var month: NumberPicker
    private lateinit var date: NumberPicker

    private val currentYear = getCurrentDate()[0].toInt()
    private val currentMonth = getCurrentDate()[1].toInt()
    private val currentDate = getCurrentDate()[2].toInt()


    override fun getTheme(): Int = R.style.RoundBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(activity!!, theme)

        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).isHideable = true
        }

        // 모달 밖 영역 탭하면 모달 닫힘
        bottomSheetDialog.apply {
            setCanceledOnTouchOutside(true)
        }

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetDiaryEditDateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDatePicker()

        setPickerValueListener()

        setPickerScrollListener()

        applyButtons()

    }

    private fun applyButtons() {
        binding.apply {
            // 날짜 수정
            btnDiaryDateEdit.setOnClickListener {
                val pickDate = intArrayOf(year.value, month.value, date.value)
                itemClick(pickDate)

                requestEditDiary()
            }

            // 닫기
            btnCloseDiaryEditDate.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }

    private fun initDatePicker() {
        year = binding.includeYmdPicker.year
        month = binding.includeYmdPicker.month
        date = binding.includeYmdPicker.date

        setDateRange()

        year.value = diary_year
        month.value = diary_month
        date.value = diary_date

        // 순환 안되게 막기
        year.wrapSelectorWheel = false
        month.wrapSelectorWheel = false
        date.wrapSelectorWheel = false

        // edittext 입력 방지
        year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        date.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    private fun setDateRange() {

        // minValue = 최소 날짜 표시
        year.minValue = currentYear - 1
        month.minValue = 1
        date.minValue = 1

        // maxValue = 최대 날짜 표시
        year.maxValue = currentYear

        // year에 따라 month maxValue 변경
        month.maxValue = when (diary_year) {
            currentYear -> currentMonth
            else -> 12
        }

        // month에 따라 month, date maxValue 변경
        if (diary_month == currentMonth) {
            month.maxValue = currentMonth
            date.maxValue = currentDate
        } else {
            setMonthDateMax()
        }
    }

    private fun setPickerValueListener() {
        // year picker change listener
        year.setOnValueChangedListener { _, _, _ ->

            if (year.value == currentYear) {
                month.maxValue = currentMonth
                date.maxValue = currentDate
            } else {
                month.value = currentMonth
                date.value = currentDate
                month.maxValue = 12
                setMonthDateMax()
            }

        }

        // month picker change listener
        month.setOnValueChangedListener { _, _, _ ->

            if (year.value == currentYear && month.value == currentMonth) {
                // 현재 년도에 현재 날짜일 때
                month.maxValue = currentMonth
                date.maxValue = currentDate
            } else {
                month.maxValue = 12
                setMonthDateMax()
            }

        }
    }

    private fun setPickerScrollListener() {
        // 스크롤 했을 때 해당 날짜에 일기가 있는지 체크
        year.setOnScrollListener(pickerScrollListener)
        month.setOnScrollListener(pickerScrollListener)
        date.setOnScrollListener(pickerScrollListener)
    }

    private val pickerScrollListener = NumberPicker.OnScrollListener { _, state ->
        if (state == SCROLL_STATE_IDLE) {
            requestCheckDiary(year.value, month.value, date.value)
        }
    }

    // 달 별로 일수 다른거 미리 세팅해둔 함수
    private fun setMonthDateMax() {
        binding.includeYmdPicker.apply {
            date.maxValue = when (month.value) {
                2 -> 29
                4, 6, 9, 11 -> 30
                1, 3, 5, 7, 8, 10, 12 -> 31
                else -> 31
            }
        }
    }

    // 해당 날짜에 쓰인 일기가 있는지 확인
    private fun requestCheckDiary(year: Int, month: Int, date: Int) {

        RequestToServer.service.getHomeDiaryList(
            authorization = context?.let { SharedPreferenceController.getAccessToken(it) },
            order = "filter",
            year = year,
            month = month,
            day = date,
            userId = DiaryActivity.responseDiaryData[0].userId
        ).enqueue(object : retrofit2.Callback<ResponseDiaryList> {
            override fun onResponse(
                call: Call<ResponseDiaryList>,
                response: Response<ResponseDiaryList>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {

                        binding.btnDiaryDateEdit.isEnabled = it.data.isNullOrEmpty()

                    } ?: showError(response.errorBody())

            }

            override fun onFailure(call: Call<ResponseDiaryList>, t: Throwable) {
                Log.d("checkDiary ERROR", "$t")
            }

        })
    }

    private fun requestEditDiary() {

        RequestToServer.service.editDiary(
            Authorization = context?.let { SharedPreferenceController.getAccessToken(it) },
            params = DiaryActivity.responseDiaryData[0].id,
            body = RequestEditDiaryData(
                depth = DiaryActivity.responseDiaryData[0].depth,
                contents = DiaryActivity.responseDiaryData[0].contents,
                userId = DiaryActivity.responseDiaryData[0].userId,
                sentenceId = DiaryActivity.responseDiaryData[0].sentenceId,
                emotionId = DiaryActivity.responseDiaryData[0].emotionId,
                wroteAt = "$diary_year-$diary_month-$diary_date"
            )
        ).enqueue(object : retrofit2.Callback<ResponseDiaryData> {
            override fun onResponse(
                call: Call<ResponseDiaryData>,
                response: Response<ResponseDiaryData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {

                        ScrollFragment.IS_EDITED = true
                        ScrollFragment.EDITED_DEPTH = it.data.depth
                        requireContext().showToast("날짜가 수정되었습니다.")
                        dialog?.dismiss()

                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseDiaryData>, t: Throwable) {
                Log.d("editDiary ERROR", "$t")
            }

        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        context?.showToast(ob.getString("message"))
    }
}