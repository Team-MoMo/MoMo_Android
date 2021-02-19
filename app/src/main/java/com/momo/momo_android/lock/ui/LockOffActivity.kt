package com.momo.momo_android.lock.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityLockOnBinding
import com.momo.momo_android.util.SharedPreferenceController


class LockOffActivity : AppCompatActivity() {

    private var _binding: ActivityLockOnBinding? = null
    private val binding get() = _binding!!

    private var inputPassCode = ""
    private var currentPassCode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setListeners()
        setCurrentPassCodeView()
    }

    private fun setViewBinding() {
        _binding = ActivityLockOnBinding.inflate(layoutInflater)
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
        when (inputPassCode.length) {
            0, 1, 2, 3 -> {
                inputPassCode += id
                checkInputPassCodeLength()
            }
        }
    }

    private fun checkInputPassCodeLength() {
        updatePassCodeOvalColor(inputPassCode.length)
        when (inputPassCode.length) {
            4 -> Handler().postDelayed({ checkPassCodeValidation() }, 500)
        }
    }

    private fun checkPassCodeValidation() {
        currentPassCode = SharedPreferenceController.getPassCode(this).toString()
        if (inputPassCode == currentPassCode) {
            finishActivityWithLockStatus()
        } else {
            setWrongPassCodeView()
        }
    }

    private fun finishActivityWithLockStatus() {
        val intent = Intent()
        intent.putExtra("isLocked", false)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun deletePassCode() {
        when (inputPassCode.length) {
            1, 2, 3 -> {
                inputPassCode = inputPassCode.substring(0, inputPassCode.length - 1)
                updatePassCodeOvalColor(inputPassCode.length)
            }
        }
    }

    private fun setCurrentPassCodeView() {
        binding.apply {
            textViewDescription.text = "현재 암호를 입력해 주세요."
            textViewWrongPassCode.text = ""
        }
    }

    private fun setWrongPassCodeView() {
        binding.apply {
            setTextViewBottomMargin(textViewDescription)
            textViewWrongPassCode.text = "현재 암호와 달라요!"
        }
        inputPassCode = ""
        updatePassCodeOvalColor(inputPassCode.length)
    }

    private fun setTextViewBottomMargin(view: View) {
        val deviceDensity = Resources.getSystem().displayMetrics.density
        val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.bottomMargin = (56 * deviceDensity).toInt()
        view.layoutParams = layoutParams
    }

    private fun updatePassCodeOvalColor(length: Int) {
        binding.apply {
            when (length) {
                0 -> {
                    imageViewPassCodeOval01.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_5)
                    imageViewPassCodeOval02.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_5)
                    imageViewPassCodeOval03.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_5)
                    imageViewPassCodeOval04.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_5)
                }
                1 -> {
                    imageViewPassCodeOval01.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_3)
                    imageViewPassCodeOval02.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_5)
                    imageViewPassCodeOval03.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_5)
                    imageViewPassCodeOval04.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_5)
                }
                2 -> {
                    imageViewPassCodeOval01.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_3)
                    imageViewPassCodeOval02.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_3)
                    imageViewPassCodeOval03.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_5)
                    imageViewPassCodeOval04.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_5)
                }
                3 -> {
                    imageViewPassCodeOval01.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_3)
                    imageViewPassCodeOval02.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_3)
                    imageViewPassCodeOval03.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_3)
                    imageViewPassCodeOval04.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_5)
                }
                4 -> {
                    imageViewPassCodeOval01.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_3)
                    imageViewPassCodeOval02.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_3)
                    imageViewPassCodeOval03.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_3)
                    imageViewPassCodeOval04.backgroundTintList =
                        ContextCompat.getColorStateList(this@LockOffActivity, R.color.blue_3)
                }
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}