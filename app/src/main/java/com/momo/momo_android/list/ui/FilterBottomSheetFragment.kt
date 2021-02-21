package com.momo.momo_android.list.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.NumberPicker
import com.momo.momo_android.R
import com.momo.momo_android.databinding.BottomsheetListFilterBinding
import com.momo.momo_android.list.ui.ListActivity.Companion.filter_current_month
import com.momo.momo_android.list.ui.ListActivity.Companion.filter_current_year
import com.momo.momo_android.list.ui.ListActivity.Companion.filter_depth
import com.momo.momo_android.list.ui.ListActivity.Companion.filter_emotion
import com.momo.momo_android.list.ui.ListActivity.Companion.filter_month
import com.momo.momo_android.list.ui.ListActivity.Companion.filter_year
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.StringBuilder
import java.util.*

class FilterBottomSheetFragment(val itemClick: (String, IntArray, Boolean, Int?, Int?) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetListFilterBinding? = null
    private val binding get() = _binding!!

    // ListActivity로 보낼 필터 정보
    private lateinit var selectDate : String
    private var selectYear = 0
    private var selectMonth = 0

    // 현재 날짜와 선택한 날짜가 같으면 true, 아니면 false
    private var isCurrentDate = false

    private var selectEmotion : Int? = null
    private var selectDepth : Int? = null

    private lateinit var currentDate : Calendar
    private lateinit var year : NumberPicker
    private lateinit var month : NumberPicker

    private var toggleIdx = 0

    override fun getTheme(): Int = R.style.RoundBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)

        setListenerOnBottomSheet(bottomSheetDialog)

        return bottomSheetDialog
    }

    private fun setListenerOnBottomSheet(bottomSheetDialog: BottomSheetDialog) {
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetListFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addDateToggle()

        initCloseButton()

        initEmotionCheckBox()

        initDepthCheckBox()

        initDatePicker()
    }

    private fun initDatePicker() {
        year = binding.includeFilterNumberPicker.year
        month = binding.includeFilterNumberPicker.month

        setDateRange()

        // year.value와 month.value에 activity에서 가져온 값을 대입하는 부분을 year와 month의 maxValue값 설정하는 곳 사이로 옮김
        year.value = filter_year
        month.value = filter_month
        printDate()

        selectEmotion = filter_emotion
        selectDepth = filter_depth
        setSelectedFilter(selectEmotion, selectDepth)

        setMaxMonth()

        setPickerLimit()

        setListenerOnDatePicker()

        initApplyButton()
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
        // year에 따라 month maxValue 변경
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
            printDate()
        }

        // month picker change listener
        month.setOnValueChangedListener { _, _, _ ->

            if(year.value == currentDate.get(Calendar.YEAR) && month.value == currentDate.get(
                    Calendar.MONTH) + 1) {
                month.maxValue = currentDate.get(Calendar.MONTH) + 1
            } else {
                month.maxValue = 12
            }
            printDate()
        }
    }

    // selected date print 함수
    private fun printDate() {
        val selectDate = StringBuilder(binding.includeFilterNumberPicker.year.value.toString())
        selectDate.append("년 ")
                .append(binding.includeFilterNumberPicker.month.value.toString())
                .append("월")

        binding.tvFilterSelectedDate.text = selectDate

        selectYear = year.value
        selectMonth = month.value

        // ListActivity로 전달할 date 정보
        this.selectDate = selectDate.toString()

        isCurrentDate()
    }

    private fun isCurrentDate() {
        // 현재 날짜와 선택한 날짜가 동일할 경우 isCurrentDate 변수에 true 대입
        isCurrentDate = (year.value == filter_current_year && month.value == filter_current_month)
    }

    private fun addDateToggle() {
        binding.constraintlayoutFilterDateTouchbox.setOnClickListener {
            toggleIdx = 1 - toggleIdx

            // 토글이 접힌 상태에서 버튼을 눌렀을 때 -> v 이렇게 바껴야함
            if (toggleIdx == 1) {
                binding.includeFilterNumberPicker.constraintlayoutNumberPicker.visibility = View.VISIBLE
                binding.imgbtnFilterDateArrow.rotation = 90F
            }
            // 토글이 펼쳐진 상태에서 다시 버튼을 눌렀을 때 -> > 이렇게 바껴야함
            else {
                binding.includeFilterNumberPicker.constraintlayoutNumberPicker.visibility = View.GONE
                binding.imgbtnFilterDateArrow.rotation = 0F
            }
        }
    }

    private fun initCloseButton() {
        binding.imgbtnFilterClose.setOnClickListener {
            this.dismiss()
        }
    }

    private fun setSelectedFilter(emotionIdx : Int?, depthIdx : Int?) {
        binding.apply {
            when (emotionIdx) {
                1 -> imgbtnFilterLove.isChecked = true
                2 -> imgbtnFilterHappy.isChecked = true
                3 -> imgbtnFilterConsole.isChecked = true
                4 -> imgbtnFilterAngry.isChecked = true
                5 -> imgbtnFilterSad.isChecked = true
                6 -> imgbtnFilterBored.isChecked = true
                7 -> imgbtnFilterMemory.isChecked = true
                8 -> imgbtnFilterDaily.isChecked = true
                else -> Log.d("setSelectedFilter", "emotion: nothing selected")
            }

            when (depthIdx) {
                0 -> imgbtnFilterDepth2.isChecked = true
                1 -> imgbtnFilterDepth30.isChecked = true
                2 -> imgbtnFilterDepth100.isChecked = true
                3 -> imgbtnFilterDepth300.isChecked = true
                4 -> imgbtnFilterDepth700.isChecked = true
                5 -> imgbtnFilterDepth1005.isChecked = true
                6 -> imgbtnFilterDepthUnder.isChecked = true
                else -> Log.d("setSelectedFilter", "depth: nothing selected")
            }
        }
    }

    // 감정 선택 체크박스 관련 함수
    private fun initEmotionCheckBox() {
        binding.apply {
            imgbtnFilterLove.selectEmotion()
            imgbtnFilterHappy.selectEmotion()
            imgbtnFilterConsole.selectEmotion()
            imgbtnFilterAngry.selectEmotion()
            imgbtnFilterSad.selectEmotion()
            imgbtnFilterBored.selectEmotion()
            imgbtnFilterMemory.selectEmotion()
            imgbtnFilterDaily.selectEmotion()
        }
    }

    private fun CheckBox.selectEmotion() {
        this.setOnClickListener {
            if (this.isChecked) {
                disableEmotionCheckBox()
                this.isChecked = true

                addEmotionId(this.id)
            }
            else if (!this.isChecked) {
                this.isChecked = false
                selectEmotion = null
            }
        }
    }

    private fun addEmotionId(id : Int) {
        binding.apply {
            when(id) {
                imgbtnFilterLove.id -> selectEmotion = 1
                imgbtnFilterHappy.id -> selectEmotion = 2
                imgbtnFilterConsole.id -> selectEmotion = 3
                imgbtnFilterAngry.id -> selectEmotion = 4
                imgbtnFilterSad.id -> selectEmotion = 5
                imgbtnFilterBored.id -> selectEmotion = 6
                imgbtnFilterMemory.id -> selectEmotion = 7
                imgbtnFilterDaily.id -> selectEmotion = 8
                else -> Log.d("id", "error")
            }
        }
    }

    // 모든 감정 체크박스 비활성화
    private fun disableEmotionCheckBox() {
        binding.apply {
            imgbtnFilterLove.isChecked = false
            imgbtnFilterHappy.isChecked = false
            imgbtnFilterConsole.isChecked = false
            imgbtnFilterAngry.isChecked = false
            imgbtnFilterSad.isChecked = false
            imgbtnFilterBored.isChecked = false
            imgbtnFilterMemory.isChecked = false
            imgbtnFilterDaily.isChecked = false
        }
    }

    // 깊이 선택 체크박스 관련 함수
    private fun initDepthCheckBox() {
        binding.apply {
            imgbtnFilterDepth2.selectDepth()
            imgbtnFilterDepth30.selectDepth()
            imgbtnFilterDepth100.selectDepth()
            imgbtnFilterDepth300.selectDepth()
            imgbtnFilterDepth700.selectDepth()
            imgbtnFilterDepth1005.selectDepth()
            imgbtnFilterDepthUnder.selectDepth()
        }
    }

    private fun CheckBox.selectDepth() {
        this.setOnClickListener {
            if (this.isChecked) {
                disableDepthCheckBox()
                this.isChecked = true

                addDepthId(this.id)
            }
            else if (!this.isChecked) {
                this.isChecked = false
                selectDepth = null
            }
        }
    }

    private fun addDepthId(id : Int) {
        binding.apply {
            when(id) {
                imgbtnFilterDepth2.id -> selectDepth = 0
                imgbtnFilterDepth30.id -> selectDepth = 1
                imgbtnFilterDepth100.id -> selectDepth = 2
                imgbtnFilterDepth300.id -> selectDepth = 3
                imgbtnFilterDepth700.id -> selectDepth = 4
                imgbtnFilterDepth1005.id -> selectDepth = 5
                imgbtnFilterDepthUnder.id -> selectDepth = 6
                else -> Log.d("id", "error")
            }
        }
    }

    // 모든 깊이 체크박스 비활성화
    private fun disableDepthCheckBox() {
        binding.apply {
            imgbtnFilterDepth2.isChecked = false
            imgbtnFilterDepth30.isChecked = false
            imgbtnFilterDepth100.isChecked = false
            imgbtnFilterDepth300.isChecked = false
            imgbtnFilterDepth700.isChecked = false
            imgbtnFilterDepth1005.isChecked = false
            imgbtnFilterDepthUnder.isChecked = false
        }
    }

    private fun initApplyButton() {
        binding.btnFilterApply.isEnabled = true
        binding.btnFilterApply.setOnClickListener {

            // 선택된 항목들 Activity로 전달
            val pickDate = intArrayOf(selectYear, selectMonth)

            itemClick(selectDate, pickDate, isCurrentDate, selectEmotion, selectDepth)

            // bottomSheet dismiss
            this.dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}