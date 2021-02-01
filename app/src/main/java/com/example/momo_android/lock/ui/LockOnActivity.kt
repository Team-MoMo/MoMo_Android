package com.example.momo_android.lock.ui

import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityLockOnBinding
import com.example.momo_android.util.SharedPreferenceController


class LockOnActivity : AppCompatActivity() {

    private var _viewBinding: ActivityLockOnBinding? = null
    private val viewBinding get() = _viewBinding!!

    private var firstPassCode = ""
    private var finalPassCode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setListeners()
        setFirstPassCodeView()
    }

    private fun setViewBinding() {
        _viewBinding = ActivityLockOnBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    private fun setListeners() {
        viewBinding.apply {
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
        viewBinding.apply {
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
            4 -> Handler().postDelayed({ setFinalPassCodeView() }, 500)
        }
    }

    private fun checkFinalPassCodeLength() {
        updatePassCodeOvalColor(finalPassCode.length)
        when (finalPassCode.length) {
            4 -> Handler().postDelayed({ checkPassCodeValidation() }, 500)
        }
    }

    private fun checkPassCodeValidation() {
        if (finalPassCode == firstPassCode) {
            SharedPreferenceController.setLockStatus(this, true)
            SharedPreferenceController.setPassCode(this@LockOnActivity, finalPassCode)
            finish()
        } else {
            setWrongPassCodeView()
        }
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
        viewBinding.apply {
            textViewDescription.text = "새 암호를 입력해 주세요."
            textViewWrongPassCode.text = ""
        }
    }

    private fun setFinalPassCodeView() {
        viewBinding.apply {
            textViewDescription.text = "다시 한 번 입력해 주세요."
            textViewWrongPassCode.text = ""
        }
        updatePassCodeOvalColor(finalPassCode.length)
    }

    private fun setWrongPassCodeView() {
        viewBinding.apply {
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
        viewBinding.apply {
            when (length) {
                0 -> {
                    imageViewPassCodeOval01.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_5)
                    imageViewPassCodeOval02.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_5)
                    imageViewPassCodeOval03.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_5)
                    imageViewPassCodeOval04.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_5)
                }
                1 -> {
                    imageViewPassCodeOval01.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_3)
                    imageViewPassCodeOval02.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_5)
                    imageViewPassCodeOval03.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_5)
                    imageViewPassCodeOval04.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_5)
                }
                2 -> {
                    imageViewPassCodeOval01.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_3)
                    imageViewPassCodeOval02.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_3)
                    imageViewPassCodeOval03.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_5)
                    imageViewPassCodeOval04.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_5)
                }
                3 -> {
                    imageViewPassCodeOval01.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_3)
                    imageViewPassCodeOval02.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_3)
                    imageViewPassCodeOval03.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_3)
                    imageViewPassCodeOval04.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_5)
                }
                4 -> {
                    imageViewPassCodeOval01.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_3)
                    imageViewPassCodeOval02.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_3)
                    imageViewPassCodeOval03.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_3)
                    imageViewPassCodeOval04.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOnActivity, R.color.blue_3)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}