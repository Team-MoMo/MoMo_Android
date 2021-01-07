package com.example.momo_android.list

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.NumberPicker
import com.example.momo_android.R
import com.example.momo_android.databinding.BottomsheetListFilterBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.include_ym_picker.*
import java.lang.StringBuilder
import java.util.*

class FilterBottomSheetFragment(val itemClick: (String, Int, Int) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetListFilterBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentDate : Calendar

    private var toggleIdx = 0

    // 모든 flag 값이 다 0이어야만 적용 버튼 비활성화
    private var dateFlag = false
    private var emotionFlag = false
    private var depthFlag = false

    // ListActivity로 보낼 필터 정보
    private lateinit var selectDate : String
    private var selectEmotion = 0
    private var selectDepth = 0

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

        val year = binding.includeFilterNumberPicker.year
        val month = binding.includeFilterNumberPicker.month

        // 현재 날짜 가져오기
        currentDate = Calendar.getInstance()

        // minValue = 최소 날짜 표시
        year.minValue = 2020
        month.minValue = 1

        // maxValue = 최대 날짜 표시
        year.maxValue = 2021

        // year에 따라 month maxValue 변경
        if(year.value == currentDate.get(Calendar.YEAR)) {
            month.maxValue = currentDate.get(Calendar.MONTH) + 1
        } else {
            month.maxValue = 12
        }

        // month에 따라 month, date maxValue 변경
        if(month.value == currentDate.get(Calendar.MONTH) + 1)
            month.maxValue = currentDate.get(Calendar.MONTH) + 1

        // value = 가운데 표시될 날짜
        year.value = currentDate.get(Calendar.YEAR)
        month.value = currentDate.get(Calendar.MONTH) + 1
        printDate()

        // 순환 안되게 막기
        year.wrapSelectorWheel = false
        month.wrapSelectorWheel = false

        // edittext 입력 방지
        year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        // year picker change listener
        year.setOnValueChangedListener { _, _, _ ->

            if(year.value == currentDate.get(Calendar.YEAR)) {
                month.maxValue = currentDate.get(Calendar.MONTH) + 1
            } else {
                month.value = currentDate.get(Calendar.MONTH) + 1
                month.maxValue = 12
            }
            printDate()
        }

        // month picker change listener
        month.setOnValueChangedListener { _, _, _ ->

            if(year.value == currentDate.get(Calendar.YEAR) && month.value == currentDate.get(
                    Calendar.MONTH) + 1) {
                month.maxValue = currentDate.get(Calendar.MONTH) + 1
                // 적용 버튼 비활성화
                binding.btnFilterApply.isEnabled = false
            } else {
                month.maxValue = 12
            }
            printDate()
        }
    }

    // selected date print 함수
    private fun printDate() {
        val selectDate = StringBuilder(year.value.toString())
        selectDate.append("년 ")
                .append(month.value.toString())
                .append("월")

        binding.tvFilterSelectedDate.text = selectDate

        // ListActivity로 전달할 date 정보
        this.selectDate = selectDate.toString()

        checkDateChanged()
    }

    private fun checkDateChanged() {
        dateFlag = !(year.value == currentDate.get(Calendar.YEAR) && month.value == currentDate.get(
                Calendar.MONTH) + 1)
        activeApplyButton()

        if (year.value == currentDate.get(Calendar.YEAR) && month.value == currentDate.get(
                        Calendar.MONTH) + 1) {
            dateFlag = false
            disableApplyButton()
        } else {
            dateFlag = true
            activeApplyButton()
        }
    }

    private fun addDateToggle() {
        binding.constraintlayoutFilterDateTouchbox.setOnClickListener {
            toggleIdx = 1 - toggleIdx

            // 토글이 접힌 상태에서 버튼을 눌렀을 때 -> v 이렇게 바껴야함
            if (toggleIdx == 1) {
                binding.includeFilterNumberPicker.constraintlayoutNumberPicker.visibility = View.VISIBLE
                binding.imgbtnFilterDateArrow.rotation = 0F
            }
            // 토글이 펼쳐진 상태에서 다시 버튼을 눌렀을 때 -> > 이렇게 바껴야함
            else {
                binding.includeFilterNumberPicker.constraintlayoutNumberPicker.visibility = View.GONE
                binding.imgbtnFilterDateArrow.rotation = 270F
            }
        }
    }

    private fun initCloseButton() {
        binding.imgbtnFilterClose.setOnClickListener {
            this.dismiss()
        }
    }

    // 감정 선택 체크박스 관련 함수
    private fun initEmotionCheckBox() {
        binding.imgbtnFilterLove.selectEmotion()
        binding.imgbtnFilterHappy.selectEmotion()
        binding.imgbtnFilterConsole.selectEmotion()
        binding.imgbtnFilterAngry.selectEmotion()
        binding.imgbtnFilterSad.selectEmotion()
        binding.imgbtnFilterBored.selectEmotion()
        binding.imgbtnFilterMemory.selectEmotion()
        binding.imgbtnFilterDaily.selectEmotion()
    }

    private fun CheckBox.selectEmotion() {
        // 이미 체크된 항목 다시 선택 취소 시 적용 버튼 비활성화
        this.setOnClickListener {
            if (this.isChecked) {
                disableEmotionCheckBox()
                this.isChecked = true
                emotionFlag = true

                addEmotionId(this.id)
                activeApplyButton()
            }
            else if (!this.isChecked) {
                this.isChecked = false
                emotionFlag = false
                selectEmotion = 0

                disableApplyButton()
            }
        }
    }

    private fun addEmotionId(id : Int) {
        when(id) {
            binding.imgbtnFilterLove.id -> selectEmotion = 1
            binding.imgbtnFilterHappy.id -> selectEmotion = 2
            binding.imgbtnFilterConsole.id -> selectEmotion = 3
            binding.imgbtnFilterAngry.id -> selectEmotion = 4
            binding.imgbtnFilterSad.id -> selectEmotion = 5
            binding.imgbtnFilterBored.id -> selectEmotion = 6
            binding.imgbtnFilterMemory.id -> selectEmotion = 7
            binding.imgbtnFilterDaily.id -> selectEmotion = 8
            else -> Log.d("id", "error")
        }
    }

    // 모든 감정 체크박스 비활성화
    private fun disableEmotionCheckBox() {
        binding.imgbtnFilterLove.isChecked = false
        binding.imgbtnFilterHappy.isChecked = false
        binding.imgbtnFilterConsole.isChecked = false
        binding.imgbtnFilterAngry.isChecked = false
        binding.imgbtnFilterSad.isChecked = false
        binding.imgbtnFilterBored.isChecked = false
        binding.imgbtnFilterMemory.isChecked = false
        binding.imgbtnFilterDaily.isChecked = false
    }

    // 깊이 선택 체크박스 관련 함수
    private fun initDepthCheckBox() {
        binding.imgbtnFilterDepth2.selectDepth()
        binding.imgbtnFilterDepth30.selectDepth()
        binding.imgbtnFilterDepth100.selectDepth()
        binding.imgbtnFilterDepth300.selectDepth()
        binding.imgbtnFilterDepth700.selectDepth()
        binding.imgbtnFilterDepth1005.selectDepth()
        binding.imgbtnFilterDepthUnder.selectDepth()
    }

    private fun CheckBox.selectDepth() {
        // 이미 체크된 항목 다시 선택 취소 시 적용 버튼 비활성화
        this.setOnClickListener {
            if (this.isChecked) {
                disableDepthCheckBox()
                this.isChecked = true
                depthFlag = true

                addDepthId(this.id)
                activeApplyButton()
            }
            else if (!this.isChecked) {
                this.isChecked = false
                depthFlag = false
                selectDepth = 0

                disableApplyButton()
            }
        }
    }

    private fun addDepthId(id : Int) {
        when(id) {
            binding.imgbtnFilterDepth2.id -> selectDepth = 1
            binding.imgbtnFilterDepth30.id -> selectDepth = 2
            binding.imgbtnFilterDepth100.id -> selectDepth = 3
            binding.imgbtnFilterDepth300.id -> selectDepth = 4
            binding.imgbtnFilterDepth700.id -> selectDepth = 5
            binding.imgbtnFilterDepth1005.id -> selectDepth = 6
            binding.imgbtnFilterDepthUnder.id -> selectDepth = 7
            else -> Log.d("id", "error")
        }
    }

    // 모든 깊이 체크박스 비활성화
    private fun disableDepthCheckBox() {
        binding.imgbtnFilterDepth2.isChecked = false
        binding.imgbtnFilterDepth30.isChecked = false
        binding.imgbtnFilterDepth100.isChecked = false
        binding.imgbtnFilterDepth300.isChecked = false
        binding.imgbtnFilterDepth700.isChecked = false
        binding.imgbtnFilterDepth1005.isChecked = false
        binding.imgbtnFilterDepthUnder.isChecked = false
    }

    private fun activeApplyButton() {
        // 세 flag 값 중 하나라도 true이면
        if (dateFlag || emotionFlag || depthFlag) {
            binding.btnFilterApply.isEnabled = true
            binding.btnFilterApply.setOnClickListener {
                // 선택된 항목들 Activity로 전달
                Log.d("applybutton-date", selectDate)
                Log.d("applybutton-emotion", selectEmotion.toString())
                Log.d("applybutton-depth", selectDepth.toString())

                itemClick(selectDate, selectEmotion, selectDepth)
                // 툴바의 필터 버튼 색상 변경 (필터 적용 시) -> activity에서 해야함

                // bottomSheet dismiss
                this.dismiss()
            }
        }
    }

    private fun disableApplyButton() {
        if (!dateFlag && !emotionFlag && !depthFlag)
            binding.btnFilterApply.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}