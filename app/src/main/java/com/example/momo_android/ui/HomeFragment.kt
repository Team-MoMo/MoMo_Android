package com.example.momo_android.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.momo_android.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentHomeBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    // UI 작업 수행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDayNightView()
    }

    private fun setDayNightView() {
//        setDayView(true)
        setNightView(false)
    }

    private fun setDayView(isDefaultView: Boolean) {
        viewBinding.apply {
            when (isDefaultView) {
                true -> {
                    setDayEmptyListeners()
                    includeHomeDayDiary.root.visibility = View.INVISIBLE
                    includeHomeDayEmpty.root.visibility = View.VISIBLE
                    includeHomeNightDiary.root.visibility = View.INVISIBLE
                    includeHomeNightEmpty.root.visibility = View.INVISIBLE

                }
                false -> {
                    setDayDiaryListeners()
                    includeHomeDayDiary.root.visibility = View.VISIBLE
                    includeHomeDayEmpty.root.visibility = View.INVISIBLE
                    includeHomeNightDiary.root.visibility = View.INVISIBLE
                    includeHomeNightEmpty.root.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setNightView(isDefaultView: Boolean) {
        viewBinding.apply {
            when (isDefaultView) {
                true -> {
                    setNightEmptyListeners()
                    includeHomeDayDiary.root.visibility = View.INVISIBLE
                    includeHomeDayEmpty.root.visibility = View.INVISIBLE
                    includeHomeNightDiary.root.visibility = View.INVISIBLE
                    includeHomeNightEmpty.root.visibility = View.VISIBLE

                }
                false -> {
                    setNightDiaryListeners()
                    includeHomeDayDiary.root.visibility = View.INVISIBLE
                    includeHomeDayEmpty.root.visibility = View.INVISIBLE
                    includeHomeNightDiary.root.visibility = View.VISIBLE
                    includeHomeNightEmpty.root.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setDayDiaryListeners() {
        viewBinding.includeHomeDayDiary.apply {
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            buttonShowFull.setOnClickListener(fragmentOnClickListener)
            imageButtonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private fun setDayEmptyListeners() {
        viewBinding.includeHomeDayEmpty.apply {
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            buttonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private fun setNightDiaryListeners() {
        viewBinding.includeHomeNightDiary.apply {
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            buttonShowFull.setOnClickListener(fragmentOnClickListener)
            imageButtonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private fun setNightEmptyListeners() {
        viewBinding.includeHomeNightEmpty.apply {
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            buttonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private val fragmentOnClickListener = View.OnClickListener {
        viewBinding.apply {
            when (it.id) {
                includeHomeDayDiary.imageButtonMy.id,
                includeHomeDayEmpty.imageButtonMy.id,
                includeHomeNightDiary.imageButtonMy.id,
                includeHomeNightEmpty.imageButtonMy.id -> {
                    Log.d("TAG", "clicked: ")
                }
                includeHomeDayDiary.buttonShowFull.id,
                includeHomeNightDiary.buttonShowFull.id -> {
                    Log.d("TAG", "clicked: ")
                }
                includeHomeDayEmpty.buttonUpload.id,
                includeHomeNightEmpty.buttonUpload.id -> {
                    Log.d("TAG", "clicked: ")
                }
                includeHomeDayDiary.imageButtonUpload.id,
                includeHomeNightDiary.imageButtonUpload.id -> {
                    Log.d("TAG", "clicked: ")
                }
                includeHomeDayDiary.imageButtonList.id,
                includeHomeDayEmpty.imageButtonList.id,
                includeHomeNightDiary.imageButtonList.id,
                includeHomeNightEmpty.imageButtonList.id -> {
                    Log.d("TAG", "clicked: ")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}