package com.example.momo_android.diary.ui

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.NumberPicker
import com.example.momo_android.R
import com.example.momo_android.databinding.BottomsheetDiaryEditDateBinding
import com.example.momo_android.diary.data.RequestEditDiaryData
import com.example.momo_android.diary.data.ResponseDiaryData
import com.example.momo_android.diary.ui.DiaryActivity.Companion.diary_date
import com.example.momo_android.diary.ui.DiaryActivity.Companion.diary_month
import com.example.momo_android.diary.ui.DiaryActivity.Companion.diary_year
import com.example.momo_android.list.data.ResponseFilterData
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.util.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback

class EditDateBottomSheetFragment(val itemClick: (IntArray) -> Unit) : BottomSheetDialogFragment() {

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
        return Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        if(diary_year == currentDate.get(Calendar.YEAR)) {
            month.maxValue = currentDate.get(Calendar.MONTH) + 1
        } else {
            month.maxValue = 12
        }

        // month에 따라 month, date maxValue 변경
        if(diary_month == currentDate.get(Calendar.MONTH) + 1) {
            month.maxValue = currentDate.get(Calendar.MONTH) + 1
            date.maxValue = currentDate.get(Calendar.DAY_OF_MONTH)
        } else {
            setMonthMax()
        }


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

        date.setOnValueChangedListener { _, _, _ ->
            requestCheckDiary(year.value, month.value, date.value)
        }






        Binding.btnDiaryDateEdit.setOnClickListener {
            val pick = intArrayOf(year.value, month.value, date.value)
            itemClick(pick)

            // 날짜수정 통신
            requestEditDiary()
        }

        Binding.btnCloseDiaryEditDate.setOnClickListener {
            dialog?.dismiss()
        }



    }

    // 해당 날짜에 쓰인 일기가 있는지 확인
    private fun requestCheckDiary(year: Int, month: Int, date: Int) {

        RequestToServer.service.getFilterdDiary(
            Authorization = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTYxMDI4NTcxOCwiZXhwIjoxNjE4MDYxNzE4LCJpc3MiOiJtb21vIn0.BudOmb4xI78sbtgw81wWY8nfBD2A6Wn4vS4bvlzSZYc",
            userId = DiaryActivity.responseData[0].userId,
            year = year,
            month = month,
            order = "date",
            emotionId = 1,
            depth = 1
        ).enqueue(object : retrofit2.Callback<ResponseFilterData> {
            override fun onResponse(
                call: Call<ResponseFilterData>,
                response: Response<ResponseFilterData>
            ) {

                // 코드 400으로 뜸
                when {
                    response.code() == 200 -> {
                        val checkDate = "$year$month$date"
                        for(i in 0..response.body()!!.data.size) {
                            val matchDate = getFormedDate(response.body()!!.data[i].wroteAt)
                            if(checkDate == matchDate) {
                                Log.d("가능여부", "놉")
                                Binding.btnDiaryDateEdit.isEnabled = false
                                break
                            } else {
                                Log.d("가능여부", "된다")
                                Binding.btnDiaryDateEdit.isEnabled = true
                            }

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

            override fun onFailure(call: Call<ResponseFilterData>, t: Throwable) {
                Log.d("checkDiary ERROR", "$t")
            }

        })
    }

    private fun requestEditDiary() {

        RequestToServer.service.editDiary(
            Authorization = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTYxMDI4NTcxOCwiZXhwIjoxNjE4MDYxNzE4LCJpc3MiOiJtb21vIn0.BudOmb4xI78sbtgw81wWY8nfBD2A6Wn4vS4bvlzSZYc",
            params = DiaryActivity.responseData[0].id,
            RequestEditDiaryData(
                depth = DiaryActivity.responseData[0].depth,
                contents = DiaryActivity.responseData[0].contents,
                userId = DiaryActivity.responseData[0].userId,
                sentenceId = DiaryActivity.responseData[0].sentenceId,
                emotionId = DiaryActivity.responseData[0].emotionId,
                wroteAt = "$diary_year-$diary_month-$diary_date"
            )
        ).enqueue(object : retrofit2.Callback<ResponseDiaryData> {
            override fun onResponse(
                call: Call<ResponseDiaryData>,
                response: Response<ResponseDiaryData>
            ) {
                when {
                    response.code() == 200 -> {
                        Log.d("날짜 수정 성공", response.body().toString())
                        context!!.showToast("날짜가 수정되었습니다.")
                        dialog?.dismiss()
                    }
                    response.code() == 400 -> {
                        Log.d("editDiary 400", response.message())
                    }
                    else -> {
                        Log.d("editDiary 500", response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDiaryData>, t: Throwable) {
                Log.d("editDiary ERROR", "$t")
            }

        })
    }

    private fun getFormedDate(wroteAt: String) : String {
        val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREAN).parse(wroteAt)
        val diary_day = SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(dateformat)
        return diary_day
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




}