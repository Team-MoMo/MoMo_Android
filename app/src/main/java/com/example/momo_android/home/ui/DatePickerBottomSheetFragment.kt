package com.example.momo_android.home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.example.momo_android.R
import com.example.momo_android.databinding.BottomsheetScrollDatePickerBinding
import com.example.momo_android.home.ui.ScrollFragment.Companion.QUERY_MONTH
import com.example.momo_android.home.ui.ScrollFragment.Companion.QUERY_YEAR
import com.example.momo_android.util.ScrollDatePickerListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class DatePickerBottomSheetFragment(
    private val clickListener: ScrollDatePickerListener
) : BottomSheetDialogFragment() {

    private var _viewBinding: BottomsheetScrollDatePickerBinding? = null
    private val viewBinding get() = _viewBinding!!

    private var selectedYear = 0
    private var selectedMonth = 0
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1


    override fun getTheme(): Int = R.style.RoundBottomSheetDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = BottomsheetScrollDatePickerBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initDatePicker()
    }

    private fun setListeners() {
        viewBinding.apply {
            imageButtonClose.setOnClickListener(fragmentOnClickListener)
            buttonApply.setOnClickListener(fragmentOnClickListener)
            includeYmPicker.year.setOnValueChangedListener(datePickerChangedListener)
            includeYmPicker.month.setOnValueChangedListener(datePickerChangedListener)
        }
    }

    private fun initDatePicker() {
        // year picker setting - 순서중요 !!
        setYearPickerSetting(currentYear)
        initYearPickerValue()

        // month picker setting - 순서중요 !!
        setMonthPickerSetting(currentYear, currentMonth)
        initMonthPickerValue()

        // date picker setting - 순서중요 !!
        setDatePickerSetting()
    }

    private fun setYearPickerSetting(currentYear: Int) {
        viewBinding.includeYmPicker.year.apply {
            minValue = currentYear - 1
            maxValue = currentYear
        }
    }

    private fun initYearPickerValue() {
        selectedYear = QUERY_YEAR
        viewBinding.includeYmPicker.year.value = selectedYear
    }

    private fun setMonthPickerSetting(currentYear: Int, currentMonth: Int) {
        viewBinding.includeYmPicker.apply {
            month.minValue = 1
            month.maxValue =
                if (year.value == currentYear) {
                    currentMonth + 1
                    Log.d("TAG", "setMonthMinMaxValue: $currentMonth")
                } else {
                    12
                }
        }
    }

    private fun initMonthPickerValue() {
        selectedMonth = QUERY_MONTH
        viewBinding.includeYmPicker.month.value = selectedMonth
    }

    private fun setDatePickerSetting() {
        viewBinding.includeYmPicker.apply {
            // 순환 안되게 막기
            year.wrapSelectorWheel = false
            month.wrapSelectorWheel = false
            // editText 입력 방지
            year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }
    }

    private val fragmentOnClickListener = View.OnClickListener {
        when (it.id) {
            viewBinding.imageButtonClose.id -> {
                this.dismiss()
            }
            viewBinding.buttonApply.id -> {
                updateQueryData()
                clickListener.onClickDatePickerApplyButton(selectedYear, selectedMonth)
                this.dismiss()
            }
        }
    }

    private val datePickerChangedListener = NumberPicker.OnValueChangeListener { _, _, _ ->
        setMonthPickerSetting(currentYear, currentMonth)
        selectedYear = viewBinding.includeYmPicker.year.value
        selectedMonth = viewBinding.includeYmPicker.month.value
        updateApplyButtonStatus()
    }

    private fun updateQueryData() {
        QUERY_YEAR = selectedYear
        QUERY_MONTH = selectedMonth
    }

    private fun updateApplyButtonStatus() {
        viewBinding.buttonApply.isEnabled =
            !(selectedYear == QUERY_YEAR && selectedMonth == QUERY_MONTH)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}