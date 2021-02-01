package com.example.momo_android.login.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.example.momo_android.R

class ModalFindpwCountOver(context : Context) {
    private val dlg = Dialog(context)
    private lateinit var btn_yes : TextView

    fun start() {

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setCancelable(false)
        dlg.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.setContentView(R.layout.modal_findpw_count_over)

        btn_yes = dlg.findViewById(R.id.btn_count_over_yes)
        btn_yes.setOnClickListener {
            dlg.dismiss()
        }

        dlg.show()
    }



}
