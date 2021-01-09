package com.example.momo_android.util

import android.view.View

/* 리사이클러뷰 아이템 클릭 인터페이스 */
interface ItemClickListener {
    fun onClickItem(view: View, position: Int)
}

/* 모달 팝업창 버튼 클릭 인터페이스 */
interface ModalClickListener {
    fun onOKClicked(content : String)
}