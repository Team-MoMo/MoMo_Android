package com.momo.momo_android.home.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.momo.momo_android.R
import com.momo.momo_android.home.adapter.ScrollGradientAdapter
import com.momo.momo_android.databinding.FragmentScrollBinding
import com.momo.momo_android.list.ui.ListActivity
import com.momo.momo_android.home.ui.HomeFragment.Companion.DIARY_STATUS
import com.momo.momo_android.setting.ui.SettingActivity
import com.momo.momo_android.upload.ui.UploadFeelingActivity
import com.momo.momo_android.util.ScrollDatePickerListener
import com.momo.momo_android.util.getCurrentDate


class ScrollFragment : Fragment(), ScrollDatePickerListener {

    private var _binding: FragmentScrollBinding? = null
    private val binding get() = _binding!!
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private var isHomeButtonClicked: Boolean = false
    private var selectedYear = QUERY_YEAR
    private var selectedMonth = QUERY_MONTH


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScrollBinding.inflate(layoutInflater)
        return binding.root
    }

    // UI 작업 수행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initQueryDate()
        setGradientRecyclerView(QUERY_YEAR, QUERY_MONTH)
        fadeOutLoadingView()
        setOnBackPressedCallBack()
    }

    override fun onResume() {
        super.onResume()
        updateEditedData()
        if(!onBackPressedCallback.isEnabled) {
            onBackPressedCallback.isEnabled = true
        }
    }

    override fun onPause() {
        super.onPause()
        onBackPressedCallback.isEnabled = false
    }

    private fun setListeners() {
        binding.apply {
            viewButtonContainer.setOnTouchListener(fragmentOnTouchListener)
            imageButtonMy.setOnClickListener(fragmentOnClickListener)
            imageButtonCalendar.setOnClickListener(fragmentOnClickListener)
            imageButtonHome.setOnClickListener(fragmentOnClickListener)
            imageButtonUpload.setOnClickListener(fragmentOnClickListener)
            imageButtonList.setOnClickListener(fragmentOnClickListener)
        }
    }

    private fun initQueryDate() {
        QUERY_YEAR = getCurrentDate()[0].toInt()
        QUERY_MONTH = getCurrentDate()[1].toInt()
    }

    private fun setGradientRecyclerView(year: Int, month: Int) {
        binding.recyclerViewGradient.apply {
            adapter = ScrollGradientAdapter(year, month)
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(scrollListener)
        }
    }

    private fun fadeOutLoadingView() {
        binding.viewLoading.apply {
            visibility = View.VISIBLE
            alpha = 1f
            animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.viewLoading.visibility = View.GONE
                    }
                })
        }
    }

    private fun setLoadingViewBackground() {
        binding.viewLoading.apply {
            when(EDITED_DEPTH + 1) {
                2 -> setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gradient_30m_start))
                3 -> setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gradient_100m_start))
                4 -> setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gradient_300m_start))
                5 -> setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gradient_700m_start))
                6 -> setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gradient_1005m_start))
                7 -> setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gradient_deep_sea_start))
                else -> setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gradient_2m_start))
            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val visibleItemPosition = getVisibleItemPosition()
            checkHomeButtonStatus(visibleItemPosition)
            updateVerticalSeekBar(visibleItemPosition)
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
        val layoutManager = binding.recyclerViewGradient.layoutManager as LinearLayoutManager
        return layoutManager.findFirstVisibleItemPosition()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateVerticalSeekBar(visibleItemPosition: Int) {
        when (visibleItemPosition) {
            0 -> {}
            else -> {
                ObjectAnimator
                    .ofInt(
                        binding.verticalSeekBarDepth,
                        "progress",
                        (visibleItemPosition - 1) * 80
                    )
                    .setDuration(1000)
                    .start()
            }
        }
    }

    private fun checkHomeButtonStatus(visibleItemPosition: Int) {
        if (visibleItemPosition == 0 && isHomeButtonClicked) {
            (activity as HomeActivity).replaceToHomeFragment()
            isHomeButtonClicked = false
        }
    }

    private fun fadeInSwipeImage() {
        fadeInSwipeUpImage()
        fadeInSwipeDownImage()
    }

    private fun fadeInSwipeUpImage() {
        binding.imageViewSwipeUp.apply {
            visibility = View.VISIBLE
            alpha = 0f
            animate()
                .alpha(1f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(null)
        }
    }

    private fun fadeInSwipeDownImage() {
        binding.apply {
            when ((recyclerViewGradient.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()) {
                8 -> imageViewSwipeDown.visibility = ImageView.INVISIBLE
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
        binding.imageViewSwipeUp.apply {
            visibility = View.VISIBLE
            alpha = 1f
            animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.viewLoading.visibility = View.INVISIBLE
                    }
                })
        }

        binding.imageViewSwipeDown.apply {
            visibility = View.VISIBLE
            alpha = 1f
            animate()
                .alpha(0f)
                .setDuration(resources.getInteger(android.R.integer.config_longAnimTime).toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.viewLoading.visibility = View.INVISIBLE
                    }
                })
        }
    }

    private fun setOnBackPressedCallBack() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { scrollToTop() }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun updateEditedData() {
        if(IS_EDITED) {
            setLoadingViewBackground()
            binding.recyclerViewGradient.adapter!!.notifyDataSetChanged()
            binding.recyclerViewGradient.scrollToPosition(EDITED_DEPTH + 1)
            fadeOutLoadingView()
            IS_EDITED = false
        }
    }

    private val fragmentOnTouchListener = View.OnTouchListener { _, _ -> true }
    private val fragmentOnClickListener = View.OnClickListener {
        binding.apply {
            when (it.id) {
                imageButtonMy.id -> setIntentToSettingActivity()
                imageButtonCalendar.id -> setIntentToDatePicker()
                imageButtonHome.id -> scrollToTop()
                imageButtonUpload.id -> setIntentToUploadActivity()
                imageButtonList.id -> setIntentToListActivity()
            }
        }
    }

    private fun setIntentToSettingActivity() {
        val intent = Intent(requireContext(), SettingActivity::class.java)
        startActivity(intent)
    }

    private fun setIntentToDatePicker() {
        DatePickerBottomSheetFragment(this).show(requireFragmentManager(), tag)
    }

    override fun onClickDatePickerApplyButton(year: Int, month: Int) {
        selectedYear = year
        selectedMonth = month
        fadeOutLoadingView()
        setGradientRecyclerView(selectedYear, selectedMonth)
    }

    private fun scrollToTop() {
        if (getVisibleItemPosition() == 0) { (activity as HomeActivity).replaceToHomeFragment() }
        binding.recyclerViewGradient.smoothScrollToPosition(0)
        isHomeButtonClicked = true
    }

    private fun setIntentToUploadActivity() {
        val intent = Intent(requireContext(), UploadFeelingActivity::class.java)
        intent.putExtra("diaryStatus", DIARY_STATUS)
        startActivity(intent)
    }

    private fun setIntentToListActivity() {
        val intent = Intent(requireContext(), ListActivity::class.java)
        intent.putExtra("year", selectedYear)
        intent.putExtra("month", selectedMonth)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onBackPressedCallback.remove()
    }

    companion object {
        var QUERY_YEAR = getCurrentDate()[0].toInt()
        var QUERY_MONTH = getCurrentDate()[1].toInt()
        var IS_EDITED = false
        var EDITED_DEPTH = 0
    }
}