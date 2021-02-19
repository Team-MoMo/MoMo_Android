package com.momo.momo_android.lock.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityLockBinding
import com.momo.momo_android.util.SharedPreferenceController
import com.momo.momo_android.util.showToast


class LockOnActivity : AppCompatActivity() {

    private var _binding: ActivityLockBinding? = null
    private val binding get() = _binding!!

    private var firstPassCode = ""
    private var finalPassCode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setListeners()
        setFirstPassCodeView()
    }

    private fun setViewBinding() {
        _binding = ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setListeners() {
        binding.apply {
            activityOnClickListener.let {
                button01.setOnClickListener(it)
                button02.setOnClickListener(it)
                button03.setOnClickListener(it)
                button04.setOnClickListener(it)
                button05.setOnClickListener(it)
                button06.setOnClickListener(it)
                button07.setOnClickListener(it)
                button08.setOnClickListener(it)
                button09.setOnClickListener(it)
                button00.setOnClickListener(it)
                imageButtonClose.setOnClickListener(it)
                imageButtonDelete.setOnClickListener(it)
            }
        }
    }

    private val activityOnClickListener = View.OnClickListener {
        binding.apply {
            when (it.id) {
                button01.id -> setPassCode("1")
                button02.id -> setPassCode("2")
                button03.id -> setPassCode("3")
                button04.id -> setPassCode("4")
                button05.id -> setPassCode("5")
                button06.id -> setPassCode("6")
                button07.id -> setPassCode("7")
                button08.id -> setPassCode("8")
                button09.id -> setPassCode("9")
                button00.id -> setPassCode("0")
                imageButtonClose.id -> finish()
                imageButtonDelete.id -> deletePassCode()
            }
        }
    }

    private fun setPassCode(id: String) {
        when (firstPassCode.length) {
            0, 1, 2, 3 -> {
                firstPassCode += id
                checkFirstPassCodeLength()
            }
            4 -> {
                finalPassCode += id
                checkFinalPassCodeLength()
            }
        }
    }

    private fun checkFirstPassCodeLength() {
        updatePassCodeOvalColor(firstPassCode.length)
        when (firstPassCode.length) {
            4 -> Handler(mainLooper).postDelayed({ setFinalPassCodeView() }, 500)
        }
    }

    private fun checkFinalPassCodeLength() {
        updatePassCodeOvalColor(finalPassCode.length)
        when (finalPassCode.length) {
            4 -> Handler(mainLooper).postDelayed({ checkPassCodeValidation() }, 500)
        }
    }

    private fun checkPassCodeValidation() {
        if (finalPassCode == firstPassCode) {
            SharedPreferenceController.setPassCode(this@LockOnActivity, finalPassCode)
            finishActivityWithLockStatus()
            showToast("암호 설정이 완료되었습니다.")
        } else {
            setWrongPassCodeView()
        }
    }

    private fun finishActivityWithLockStatus() {
        val intent = Intent()
        intent.putExtra("isLocked", true)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun deletePassCode() {
        when (firstPassCode.length) {
            1, 2, 3 -> {
                firstPassCode = firstPassCode.substring(0, firstPassCode.length - 1)
                updatePassCodeOvalColor(firstPassCode.length)
            }
            else -> {
                when (finalPassCode.length) {
                    1, 2, 3 -> {
                        finalPassCode = finalPassCode.substring(0, finalPassCode.length - 1)
                        updatePassCodeOvalColor(finalPassCode.length)
                    }
                }
            }
        }
    }

    private fun setFirstPassCodeView() {
        binding.apply {
            textViewDescription.text = "새 암호를 입력해 주세요."
            textViewWrongPassCode.text = ""
        }
    }

    private fun setFinalPassCodeView() {
        binding.apply {
            textViewDescription.text = "다시 한 번 입력해 주세요."
            textViewWrongPassCode.text = ""
        }
        updatePassCodeOvalColor(finalPassCode.length)
    }

    private fun setWrongPassCodeView() {
        binding.apply {
            setTextViewBottomMargin(textViewDescription)
            textViewDescription.text = "다시 한 번 입력해 주세요."
            textViewWrongPassCode.text = "입력한 암호와 달라요!"
        }
        finalPassCode = ""
        updatePassCodeOvalColor(finalPassCode.length)
    }

    private fun setTextViewBottomMargin(view: View) {
        val deviceDensity = Resources.getSystem().displayMetrics.density
        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.bottomMargin = (56 * deviceDensity).toInt()
        view.layoutParams = layoutParams
    }

    private fun updatePassCodeOvalColor(length: Int) {
        binding.apply {
            this@LockOnActivity.let {
                when (length) {
                    0 -> {
                        imageViewPassCodeOval01.backgroundTintList = setPassCodeOvalColor(it, false)
                        imageViewPassCodeOval02.backgroundTintList = setPassCodeOvalColor(it, false)
                        imageViewPassCodeOval03.backgroundTintList = setPassCodeOvalColor(it, false)
                        imageViewPassCodeOval04.backgroundTintList = setPassCodeOvalColor(it, false)
                    }
                    1 -> {
                        imageViewPassCodeOval01.backgroundTintList = setPassCodeOvalColor(it, true)
                        imageViewPassCodeOval02.backgroundTintList = setPassCodeOvalColor(it, false)
                        imageViewPassCodeOval03.backgroundTintList = setPassCodeOvalColor(it, false)
                        imageViewPassCodeOval04.backgroundTintList = setPassCodeOvalColor(it, false)
                    }
                    2 -> {
                        imageViewPassCodeOval01.backgroundTintList = setPassCodeOvalColor(it, true)
                        imageViewPassCodeOval02.backgroundTintList = setPassCodeOvalColor(it, true)
                        imageViewPassCodeOval03.backgroundTintList = setPassCodeOvalColor(it, false)
                        imageViewPassCodeOval04.backgroundTintList = setPassCodeOvalColor(it, false)
                    }
                    3 -> {
                        imageViewPassCodeOval01.backgroundTintList = setPassCodeOvalColor(it, true)
                        imageViewPassCodeOval02.backgroundTintList = setPassCodeOvalColor(it, true)
                        imageViewPassCodeOval03.backgroundTintList = setPassCodeOvalColor(it, true)
                        imageViewPassCodeOval04.backgroundTintList = setPassCodeOvalColor(it, false)
                    }
                    4 -> {
                        imageViewPassCodeOval01.backgroundTintList = setPassCodeOvalColor(it, true)
                        imageViewPassCodeOval02.backgroundTintList = setPassCodeOvalColor(it, true)
                        imageViewPassCodeOval03.backgroundTintList = setPassCodeOvalColor(it, true)
                        imageViewPassCodeOval04.backgroundTintList = setPassCodeOvalColor(it, true)
                    }
                }
            }
        }
    }

    private fun setPassCodeOvalColor(context: Context, isFilled: Boolean): ColorStateList {
        return when (isFilled) {
            true -> ContextCompat.getColorStateList(context, R.color.blue_3)!!
            false -> ContextCompat.getColorStateList(context, R.color.blue_5)!!
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}