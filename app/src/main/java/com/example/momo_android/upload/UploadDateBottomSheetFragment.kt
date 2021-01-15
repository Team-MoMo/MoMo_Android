package com.example.momo_android.upload

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
import androidx.appcompat.app.AppCompatActivity
import com.example.momo_android.R
import com.example.momo_android.databinding.BottomsheetDiaryEditDateBinding
import com.example.momo_android.home.data.ResponseDiaryList
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.upload.ui.UploadFeelingActivity
import com.example.momo_android.util.SharedPreferenceController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Response
import java.util.*

class UploadDateBottomSheetFragment (val itemClick: (IntArray) -> Unit) : BottomSheetDialogFragment() {
    private var _Binding: BottomsheetDiaryEditDateBinding? = null
    private val Binding get() = _Binding!!

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
    ): View? {
        _Binding = BottomsheetDiaryEditDateBinding.inflate(layoutInflater)

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity).supportActionBar?.hide()

        Binding.tvChangeDate.text="날짜 변경"
        return Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Binding.btnDiaryDateEdit.isEnabled=true

        val year = Binding.includeYmdPicker.year
        val month = Binding.includeYmdPicker.month
        val date = Binding.includeYmdPicker.date

        // divider 색깔 투명으로 변경하는 함수
        fun NumberPicker.removeDivider() {
            val pickerFields = NumberPicker::class.java.declaredFields
            for (pf in pickerFields) {
                if (pf.name == "mSelectionDivider") {
                    pf.isAccessible = true
                    try {
                        val colorDrawable = ColorDrawable(Color.TRANSPARENT)
                        pf[this] = colorDrawable
                    } catch (e: java.lang.IllegalArgumentException) {
                        // log exception here
                    } catch (e: Resources.NotFoundException) {
                        // log exception here
                    } catch (e: IllegalAccessException) {
                        // log exception here
                    }
                    break
                }
            }
        }

        year.removeDivider()
        month.removeDivider()
        date.removeDivider()

        // 현재 날짜 가져오기
        val currentDate = Calendar.getInstance()

        // minValue = 최소 날짜 표시
        year.minValue = 2020
        month.minValue = 1
        date.minValue = 1

        // maxValue = 최대 날짜 표시
        year.maxValue = currentDate.get(Calendar.YEAR)

        // year에 따라 month maxValue 변경
        if(UploadFeelingActivity.upload_year == currentDate.get(Calendar.YEAR)) {
            month.maxValue = currentDate.get(Calendar.MONTH) + 1
        } else {
            month.maxValue = 12
        }

        // month에 따라 month, date maxValue 변경
        if(UploadFeelingActivity.upload_month == currentDate.get(Calendar.MONTH) + 1) {
            month.maxValue = currentDate.get(Calendar.MONTH) + 1
            date.maxValue = currentDate.get(Calendar.DAY_OF_MONTH)
        } else {
            setMonthMax()
        }


        year.value = UploadFeelingActivity.upload_year
        month.value = UploadFeelingActivity.upload_month
        date.value = UploadFeelingActivity.upload_date

        // 순환 안되게 막기

        year.wrapSelectorWheel = false
        month.wrapSelectorWheel = false
        date.wrapSelectorWheel = false

        // edittext 입력 방지
        year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        date.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        // year picker change listener
        year.setOnValueChangedListener { _, _, _ ->

            if(year.value == currentDate.get(Calendar.YEAR)) {
                month.maxValue = currentDate.get(Calendar.MONTH) + 1
                date.maxValue = currentDate.get(Calendar.DAY_OF_MONTH)
            } else {
                month.value = currentDate.get(Calendar.MONTH) + 1
                date.value = currentDate.get(Calendar.DAY_OF_MONTH)
                month.maxValue = 12
                setMonthMax()
            }

        }

        // month picker change listener
        month.setOnValueChangedListener { _, _, _ ->

            if(year.value == currentDate.get(Calendar.YEAR) && month.value == currentDate.get(
                    Calendar.MONTH) + 1) {
                // 현재 년도에 현재 날짜일 때
                month.maxValue = currentDate.get(Calendar.MONTH) + 1
                date.maxValue = currentDate.get(Calendar.DAY_OF_MONTH)
            } else {
                month.maxValue = 12
                setMonthMax()
            }

        }


        // 스크롤 했을 때 해당 날짜에 일기가 있는지 체크
        year.setOnScrollListener(pickerScrollListener)
        month.setOnScrollListener(pickerScrollListener)
        date.setOnScrollListener(pickerScrollListener)


        Binding.btnDiaryDateEdit.setOnClickListener {
            val pick = intArrayOf(year.value, month.value, date.value)
            itemClick(pick)

            if(Binding.btnDiaryDateEdit.isEnabled){
                dismiss()
            }
        }

        Binding.btnCloseDiaryEditDate.setOnClickListener {
            dialog?.dismiss()
        }



    }

    private val pickerScrollListener = NumberPicker.OnScrollListener { _, state ->
        if(state == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
            requestCheckDiary(
                Binding.includeYmdPicker.year.value,
                Binding.includeYmdPicker.month.value,
                Binding.includeYmdPicker.date.value)
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
                when {
                    response.code() == 200 -> {

                        if(response.body()!!.data.isNullOrEmpty()) {
                            Log.d("가능여부", "된다?")
                            Binding.btnDiaryDateEdit.isEnabled = true
                        } else {
                            Log.d("가능여부", "놉")
                            Binding.btnDiaryDateEdit.isEnabled = false
                        }

                    }
                    response.code() == 400 -> {
                        Log.d("checkDiary 400", response.message())
                    }
                    else -> {
                        Log.d("checkDiary 500", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDiaryList>, t: Throwable) {
                Log.d("checkDiary ERROR", "$t")
            }

        })
    }



    // 달 별로 일수 다른거 미리 세팅해둔 함수
    private fun setMonthMax() {
        val month = Binding.includeYmdPicker.month
        val date = Binding.includeYmdPicker.date

        when (month.value) {
            2 -> {
                date.maxValue = 29
            }
            4, 6, 9, 11 -> {
                date.maxValue = 30
            }
            1, 3, 5, 7, 8, 10, 12 -> {
                date.maxValue = 31
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity).supportActionBar?.show()
    }


}