package com.example.momo_android.home.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.example.momo_android.home.adapter.ScrollGradientAdapter
import com.example.momo_android.databinding.FragmentScrollBinding
import com.example.momo_android.list.ui.ListActivity
import com.example.momo_android.home.ui.HomeActivity.Companion.IS_FROM_SCROLL
import com.example.momo_android.upload.ui.UploadFeelingActivity
import com.example.momo_android.util.ScrollDatePickerListener
import java.util.*


class ScrollFragment : Fragment(), ScrollDatePickerListener {

    private var _viewBinding: FragmentScrollBinding? = null
    private val viewBinding get() = _viewBinding!!

    private var visibleItemPosition: Int = 0
    private var isHomeButtonClicked: Boolean = false


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
        initQueryDate()
        setGradientRecyclerView(QUERY_YEAR, QUERY_MONTH)
        fadeOutLoadingView()
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

    private fun initQueryDate() {
        QUERY_MONTH = Calendar.getInstance().get(Calendar.YEAR)
        QUERY_MONTH = Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    private fun setGradientRecyclerView(year: Int, month: Int) {
        viewBinding.recyclerViewGradient.apply {
            adapter = ScrollGradientAdapter(year, month)
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(scrollListener)
        }
    }

    private fun fadeOutLoadingView() {
        viewBinding.viewLoading.apply {
            visibility = View.VISIBLE
            alpha = 1f
            animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        viewBinding.viewLoading.visibility = View.GONE
                    }
                })
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            checkHomeButtonStatus()
            updateVerticalSeekBar(getVisibleItemPosition())
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                SCROLL_STATE_IDLE -> fadeInSwipeImage()
                SCROLL_STATE_DRAGGING -> fadeOutSwipeImage()
            }
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

    private fun checkHomeButtonStatus() {
        if (visibleItemPosition == 0 && isHomeButtonClicked) {
            IS_FROM_SCROLL = true
            requireActivity().onBackPressed()
            isHomeButtonClicked = false
        }
    }

    private fun fadeInSwipeImage() {
        fadeInSwipeUpImage()
        fadeInSwipeDownImage()
    }

    private fun fadeInSwipeUpImage() {
        viewBinding.imageViewSwipeUp.apply {
            visibility = View.VISIBLE
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(null)
        }
    }

    private fun fadeInSwipeDownImage() {
        viewBinding.apply {
            when ((recyclerViewGradient.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()) {
                7 -> imageViewSwipeDown.visibility = ImageView.INVISIBLE
                else -> {
                    imageViewSwipeDown.visibility = View.VISIBLE
                    imageViewSwipeDown.alpha = 0f
                    imageViewSwipeDown.animate()
                        .alpha(1f)
                        .setDuration(
                            resources.getInteger(android.R.integer.config_longAnimTime).toLong()
                        )
                        .setListener(null)
                }
            }
        }
    }

    private fun fadeOutSwipeImage() {
        viewBinding.imageViewSwipeUp.apply {
            visibility = View.VISIBLE
            alpha = 1f
            animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        viewBinding.viewLoading.visibility = View.INVISIBLE
                    }
                })
        }

        viewBinding.imageViewSwipeDown.apply {
            visibility = View.VISIBLE
            alpha = 1f
            animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        viewBinding.viewLoading.visibility = View.INVISIBLE
                    }
                })
        }
    }

    private val fragmentOnTouchListener = View.OnTouchListener { _, _ -> true }
    private val fragmentOnClickListener = View.OnClickListener {
        viewBinding.apply {
            when (it.id) {
                imageButtonMy.id -> Log.d("TAG", "clicked: ")
                imageButtonCalendar.id -> setIntentToDatePicker()
                imageButtonHome.id -> scrollToTop()
                imageButtonUpload.id -> setIntentToUploadActivity()
                imageButtonList.id -> setIntentToListActivity()
            }
        }
    }

    private fun setIntentToDatePicker() {
        DatePickerBottomSheetFragment(this).show(requireFragmentManager(), tag)
    }

    override fun onClickDatePickerApplyButton(year: Int, month: Int) {
        fadeOutLoadingView()
        setGradientRecyclerView(year, month)
    }

    private fun scrollToTop() {
        viewBinding.recyclerViewGradient.smoothScrollToPosition(0)
        isHomeButtonClicked = true
    }

    private fun setIntentToUploadActivity() {
        val intent = Intent(requireContext(), UploadFeelingActivity::class.java)
        startActivity(intent)
    }

    private fun setIntentToListActivity() {
        val intent = Intent(requireContext(), ListActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        var QUERY_YEAR = Calendar.getInstance().get(Calendar.YEAR)
        var QUERY_MONTH = Calendar.getInstance().get(Calendar.MONTH) + 1
    }
}