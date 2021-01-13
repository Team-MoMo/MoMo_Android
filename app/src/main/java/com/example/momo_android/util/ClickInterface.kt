package com.example.momo_android.util

import android.view.View


/* 리사이클러뷰 아이템 클릭 인터페이스 */
interface ItemClickListener {
    fun onClickItem(view: View, position: Int)
}

/* 모달 팝업창 버튼 클릭 인터페이스 */
interface ModalClickListener {
    fun onOKClicked(content: String)
}

/* ScrollFragment.kt 리사이클러뷰 물방울 클릭 인터페이스 */
interface OvalClickListeners {
    fun onClickOvalItem(view: View, diaryId: Int)
}

/* DatePickerBottomSheetFragment.kt 적용 버튼 클릭 인터페이스 */
interface ScrollDatePickerListener {
    fun onClickDatePickerApplyButton(year: Int, month: Int)
}