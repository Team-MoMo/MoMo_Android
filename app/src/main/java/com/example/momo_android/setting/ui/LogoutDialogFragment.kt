package com.example.momo_android.setting.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.momo_android.R
import com.example.momo_android.databinding.FragmentDialogLogoutBinding
import com.example.momo_android.home.ui.HomeActivity
import com.example.momo_android.login.ui.MainLoginActivity
import com.example.momo_android.util.SharedPreferenceController


class LogoutDialogFragment : DialogFragment() {

    private var _dialogLogoutViewBinding: FragmentDialogLogoutBinding? = null
    private val dialogLogoutViewBinding get() = _dialogLogoutViewBinding!!

    lateinit var praiseIndex: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dialogLogoutViewBinding = FragmentDialogLogoutBinding.inflate(layoutInflater)
        return dialogLogoutViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setDialogBackground()
    }

    private fun setListeners() {
        dialogLogoutViewBinding.apply {
            textViewConfirm.setOnClickListener(dialogDoneClickListener)
            textViewCancel.setOnClickListener(dialogDoneClickListener)
        }
    }

    private fun setDialogBackground() {
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.dialog_rectangle_radius10)
    }

    private val dialogDoneClickListener = View.OnClickListener {
        dialogLogoutViewBinding.apply {
            when (it.id) {
                textViewConfirm.id -> {
                    clearSharedPreferences()
                    setIntentToLoginActivity()
                    dialog!!.dismiss()

                }
                textViewCancel.id -> {
                    dialog!!.dismiss()
                }
            }
        }
    }

    private fun clearSharedPreferences() {
        SharedPreferenceController.clearAccessToken(requireContext())
        SharedPreferenceController.clearUserId(requireContext())
        SharedPreferenceController.clearPassword(requireContext())
    }

    private fun setIntentToLoginActivity() {
        val intent = Intent(requireContext(), MainLoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dialogLogoutViewBinding = null
    }


    class CustomDialogBuilder {
        private val dialog = LogoutDialogFragment()

        fun create(): LogoutDialogFragment {
            return dialog
        }
    }
}