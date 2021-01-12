package com.example.momo_android.login.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.example.momo_android.R
import com.example.momo_android.util.ModalClickListener
import com.example.momo_android.util.SharedPreferenceController
import kotlin.coroutines.coroutineContext

class ModalFindpwCount(context : Context) {
    private val dlg = Dialog(context)   //부모 액티비티의 context 가 들어감
    private lateinit var btn_yes : TextView
    private lateinit var listener: ModalClickListener

    fun start() {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.setContentView(R.layout.modal_findpw_count)     //다이얼로그에 사용할 xml 파일을 불러옴

        btn_yes = dlg.findViewById(R.id.btn_findpw_yes)
        btn_yes.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }



}
