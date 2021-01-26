package com.example.momo_android.setting.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.example.momo_android.R
import com.example.momo_android.util.ModalClickListener


class ModalWithdrawal(context : Context){

private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btn_agree : TextView
    private lateinit var btn_cancel : TextView
    private lateinit var listener: ModalClickListener

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.setContentView(R.layout.modal_withdrawal)     //다이얼로그에 사용할 xml 파일을 불러옴

        btn_cancel = dlg.findViewById(R.id.btn_cancel)
        btn_cancel.setOnClickListener {
            dlg.dismiss()
        }

        btn_agree = dlg.findViewById(R.id.btn_agree)
        btn_agree.setOnClickListener {
            listener.onOKClicked("확인")
        }

        dlg.show()
    }

    fun setOnClickListener(listener: (String) -> Unit) {
        this.listener = object:
            ModalClickListener {
            override fun onOKClicked(content: String) {
                listener(content)
            }
        }
    }

}