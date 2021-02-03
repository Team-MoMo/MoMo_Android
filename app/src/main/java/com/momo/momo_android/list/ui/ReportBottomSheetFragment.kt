package com.momo.momo_android.list.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.NumberPicker
import com.momo.momo_android.R
import com.momo.momo_android.databinding.BottomsheetReportBinding
import com.momo.momo_android.list.ui.ReportActivity.Companion.report_month
import com.momo.momo_android.list.ui.ReportActivity.Companion.report_year
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class ReportBottomSheetFragment(val itemClick: (IntArray) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetReportBinding? = null
    private val binding get() = _binding!!

    private var selectYear = 0
    private var selectMonth = 0

    private lateinit var year : NumberPicker
    private lateinit var month : NumberPicker

    private lateinit var currentDate: Calendar

    override fun getTheme(): Int = R.style.RoundBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDatePicker()

        initApplyButton()

        initCloseButton()
    }

    private fun initApplyButton() {
        binding.buttonApply.setOnClickListener {
            val pickDate = intArrayOf(selectYear, selectMonth)

            itemClick(pickDate)

            this.dismiss()
        }
    }

    private fun initCloseButton() {
        binding.imageButtonClose.setOnClickListener {
            this.dismiss()
        }
    }

    private fun setDateValue() {
        selectYear = year.value
        selectMonth = month.value
    }

    private fun initDatePicker() {
        year = binding.includeYmPicker.year
        month = binding.includeYmPicker.month

        setDateRange()

        // year.value와 month.value에 activity에서 가져온 값 대입
        year.value = report_year
        month.value = report_month

        setDateValue()

        setMaxMonth()

        setPickerLimit()

        setListenerOnDatePicker()
    }

    private fun setDateRange() {
        // 현재 날짜 가져오기
        currentDate = Calendar.getInstance()

        // minValue = 최소 날짜 표시
        year.minValue = currentDate.get(Calendar.YEAR) - 1
        month.minValue = 1

        // maxValue = 최대 날짜 표시
        year.maxValue = currentDate.get(Calendar.YEAR)

        // year에 따라 month maxValue 변경
        if(year.value == currentDate.get(Calendar.YEAR)) {
            month.maxValue = currentDate.get(Calendar.MONTH) + 1
        } else {
            month.maxValue = 12
        }
    }

    private fun setMaxMonth() {
        // month에 따라 month maxValue 변경
        if(year.value == currentDate.get(Calendar.YEAR) && month.value == currentDate.get(
                Calendar.MONTH) + 1) {
            month.maxValue = currentDate.get(Calendar.MONTH) + 1
        } else {
            month.maxValue = 12
        }
    }

    private fun setPickerLimit() {
        // 순환 안되게 막기
        year.wrapSelectorWheel = false
        month.wrapSelectorWheel = false

        // edittext 입력 방지
        year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    private fun setListenerOnDatePicker() {
        // year picker change listener
        year.setOnValueChangedListener { _, _, _ ->

            if(year.value == currentDate.get(Calendar.YEAR)) {
                month.maxValue = currentDate.get(Calendar.MONTH) + 1
            } else {
                month.maxValue = 12
            }
            setDateValue()
        }

        // month picker change listener
        month.setOnValueChangedListener { _, _, _ ->

            if(year.value == currentDate.get(Calendar.YEAR) && month.value == currentDate.get(
                    Calendar.MONTH) + 1) {
                month.maxValue = currentDate.get(Calendar.MONTH) + 1
            } else {
                month.maxValue = 12
            }
            setDateValue()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}