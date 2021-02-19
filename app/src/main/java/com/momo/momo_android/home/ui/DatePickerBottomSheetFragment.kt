package com.momo.momo_android.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.momo.momo_android.R
import com.momo.momo_android.databinding.BottomsheetScrollDatePickerBinding
import com.momo.momo_android.home.ui.ScrollFragment.Companion.QUERY_MONTH
import com.momo.momo_android.home.ui.ScrollFragment.Companion.QUERY_YEAR
import com.momo.momo_android.util.ScrollDatePickerListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class DatePickerBottomSheetFragment(
    private val clickListener: ScrollDatePickerListener
) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetScrollDatePickerBinding? = null
    private val binding get() = _binding!!

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
        _binding = BottomsheetScrollDatePickerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initDatePicker()
    }

    private fun setListeners() {
        binding.apply {
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
        binding.includeYmPicker.year.apply {
            minValue = currentYear - 1
            maxValue = currentYear
        }
    }

    private fun initYearPickerValue() {
        selectedYear = QUERY_YEAR
        binding.includeYmPicker.year.value = selectedYear
    }

    private fun setMonthPickerSetting(currentYear: Int, currentMonth: Int) {
        binding.includeYmPicker.apply {
            month.minValue = 1
            if (year.value == currentYear) {
                month.maxValue = currentMonth
            } else {
                month.maxValue = 12
            }
        }
    }

    private fun initMonthPickerValue() {
        selectedMonth = QUERY_MONTH
        binding.includeYmPicker.month.value = selectedMonth
    }

    private fun setDatePickerSetting() {
        binding.includeYmPicker.apply {
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
            binding.imageButtonClose.id -> {
                this.dismiss()
            }
            binding.buttonApply.id -> {
                updateQueryData()
                clickListener.onClickDatePickerApplyButton(selectedYear, selectedMonth)
                this.dismiss()
            }
        }
    }

    private val datePickerChangedListener = NumberPicker.OnValueChangeListener { _, _, _ ->
        setMonthPickerSetting(currentYear, currentMonth)
        selectedYear = binding.includeYmPicker.year.value
        selectedMonth = binding.includeYmPicker.month.value
        updateApplyButtonStatus()
    }

    private fun updateQueryData() {
        QUERY_YEAR = selectedYear
        QUERY_MONTH = selectedMonth
    }

    private fun updateApplyButtonStatus() {
        binding.buttonApply.isEnabled =
            !(selectedYear == QUERY_YEAR && selectedMonth == QUERY_MONTH)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}