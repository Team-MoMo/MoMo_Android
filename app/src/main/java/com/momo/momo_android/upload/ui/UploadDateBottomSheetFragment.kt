package com.momo.momo_android.upload.ui

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.NumberPicker
import android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE
import androidx.appcompat.app.AppCompatActivity
import com.momo.momo_android.R
import com.momo.momo_android.home.data.ResponseDiaryList
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.util.SharedPreferenceController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.momo.momo_android.databinding.BottomsheetYmdDatePickerBinding
import com.momo.momo_android.diary.ui.DiaryActivity
import com.momo.momo_android.upload.ui.UploadFeelingActivity.Companion.upload_date
import com.momo.momo_android.upload.ui.UploadFeelingActivity.Companion.upload_month
import com.momo.momo_android.upload.ui.UploadFeelingActivity.Companion.upload_year
import com.momo.momo_android.util.getCurrentDate
import com.momo.momo_android.util.showToast
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*

class UploadDateBottomSheetFragment (val itemClick: (IntArray) -> Unit) : BottomSheetDialogFragment() {
    private var _Binding: BottomsheetYmdDatePickerBinding? = null
    private val Binding get() = _Binding!!

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
        _Binding = BottomsheetYmdDatePickerBinding.inflate(layoutInflater)

        Binding.tvChangeDate.text = "날짜 변경"
        return Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDatePicker()

        setPickerValueListener()

        setPickerScrollListener()

        applyButtons()

    }

    private fun applyButtons() {
        Binding.apply {
            btnApply.setOnClickListener {
                val pick = intArrayOf(year.value, month.value, date.value)
                itemClick(pick)

                if (btnApply.isEnabled) {
                    dismiss()
                }
            }

            btnClose.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }

    private fun initDatePicker() {
        year = Binding.includeYmdPicker.year
        month = Binding.includeYmdPicker.month
        date = Binding.includeYmdPicker.date

        setDateRange()

        year.value = upload_year
        month.value = upload_month
        date.value = upload_date

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

        // 최소 날짜 표시
        year.minValue = currentYear - 1
        month.minValue = 1
        date.minValue = 1

        // 최대 날짜 표시
        year.maxValue = currentYear

        // year에 따라 month maxValue 변경
        month.maxValue = when (upload_year) {
            currentYear -> currentMonth
            else -> 12
        }

        // month에 따라 month, date maxValue 변경
        if(upload_month == currentMonth) {
            month.maxValue = currentMonth
            date.maxValue = currentDate
        } else {
            getMonthDateMax(upload_year, upload_month)
        }

    }

    private fun setPickerValueListener() {
        // year picker change listener
        year.setOnValueChangedListener { _, _, _ ->
            if (year.value == currentYear) {
                month.maxValue = currentMonth
                if (month.value == currentMonth) {
                    date.maxValue = currentDate
                } else {
                    getMonthDateMax(currentYear, month.value)
                }
            } else {
                month.maxValue = 12
                getMonthDateMax(year.value, month.value)
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
                getMonthDateMax(year.value, month.value)
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

    // 해당 날짜에 쓰인 일기가 있는지 확인
    private fun requestCheckDiary(year: Int, month: Int, date: Int) {

        RequestToServer.service.getHomeDiaryList(
            authorization = context?.let { SharedPreferenceController.getAccessToken(it) },
            order = "filter",
            year = year,
            month = month,
            day = date,
            userId =SharedPreferenceController.getUserId(view!!.context)
        ).enqueue(object : retrofit2.Callback<ResponseDiaryList> {
            override fun onResponse(
                call: Call<ResponseDiaryList>,
                response: Response<ResponseDiaryList>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let {

                        Binding.btnApply.isEnabled = it.data.isNullOrEmpty()

                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseDiaryList>, t: Throwable) {
                Log.d("checkDiary ERROR", "$t")
            }

        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        context?.showToast(ob.getString("message"))
    }

    // 달 별로 일수 다른거 미리 세팅해둔 함수
    private fun getMonthDateMax(year: Int, month: Int) {
        Binding.includeYmdPicker.date.maxValue = when (month) {
            2 -> checkFebruaryDate(year)
            4, 6, 9, 11 -> 30
            1, 3, 5, 7, 8, 10, 12 -> 31
            else -> 31
        }
    }

    // 윤년 계산
    private fun checkFebruaryDate(year: Int): Int {
        return if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) 29
        else 28
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity).supportActionBar?.show()
    }


}