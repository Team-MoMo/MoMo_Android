package com.example.momo_android.ui

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.momo_android.R
import com.example.momo_android.adapter.ScrollGradientAdapter
import com.example.momo_android.databinding.FragmentScrollBinding


class ScrollFragment : Fragment() {

    private var _viewBinding: FragmentScrollBinding? = null
    private val viewBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentScrollBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    // UI 작업 수행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setGradientRecyclerView()
    }

    private fun setListeners() {
        viewBinding.apply {
            viewButtonContainer.setOnTouchListener(fragmentOnTouchListener)
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            imageButtonCalendar.setOnClickListener(fragmentOnClickListener)
            imageButtonHome.setOnClickListener(fragmentOnClickListener)
            imageButtonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private fun setGradientRecyclerView() {
        viewBinding.recyclerViewGradient.apply {
            adapter = ScrollGradientAdapter()
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(scrollListener)
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemPosition = getVisibleItemPosition()
            setBackgroundColor(visibleItemPosition)
            updateVerticalSeekBar(visibleItemPosition)
        }
    }

    private fun getVisibleItemPosition(): Int {
        val layoutManager = viewBinding.recyclerViewGradient.layoutManager as LinearLayoutManager
        return layoutManager.findFirstVisibleItemPosition()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateVerticalSeekBar(visibleItemPosition: Int) {
        ObjectAnimator
            .ofInt(
                viewBinding.verticalSeekBarDepth,
                "progress",
                visibleItemPosition * 80
            )
            .setDuration(1000)
            .start()
    }

    private fun setBackgroundColor(visibleItemPosition: Int) {
        viewBinding.coordinatorLayout.apply {
            when (visibleItemPosition) {
                0 -> setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.gradient_2m_start)
                )
                6 -> setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.gradient_deep_sea_end)
                )
            }
        }
    }

    private val fragmentOnTouchListener = View.OnTouchListener { v, event -> true }
    private val fragmentOnClickListener = View.OnClickListener {
        viewBinding.apply {
            when (it.id) {
                imageButtonMy.id -> {
                    Log.d("TAG", "clicked: ")
                }
                imageButtonCalendar.id -> {
                    Log.d("TAG", "clicked: ")
                }
                imageButtonHome.id -> {
                    Log.d("TAG", "clicked: ")
                }
                imageButtonUpload.id -> {
                    Log.d("TAG", "clicked: ")
                }
                imageButtonList.id -> {
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