package com.example.momo_android.onboarding

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.example.momo_android.R
import com.example.momo_android.databinding.FragmentOnboardingFeelingBinding
import com.example.momo_android.databinding.FragmentOnboardingStartBinding
import com.example.momo_android.ui.OnboardingActivity
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_date
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_feeling
import com.example.momo_android.ui.UploadSentenceActivity
import kotlinx.android.synthetic.main.activity_onboarding.*
import java.util.*

class OnboardingFeelingFragment : Fragment() {
    private var _Binding: FragmentOnboardingFeelingBinding? = null
    private val Binding get() = _Binding!!
    var onboardingActivity: OnboardingActivity?=null


    //Activity로 접근
    private val act by lazy {context as OnboardingActivity}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _Binding = FragmentOnboardingFeelingBinding.inflate(layoutInflater)
        return Binding.root
        //return inflater.inflate(R.layout.bottomsheet_custom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        companion_date=timeGenerator()
        Binding.tvDate.text= companion_date

        Binding.btnLove.setOnClickListener {
            companion_feeling="love"
            Log.d("feeling_click", companion_feeling)
            val onboarding = act.findViewById<ViewPager>(R.id.onboarding)
            onboarding.currentItem = 2
        }
        Binding.btnHappy.setOnClickListener {
            companion_feeling="happy"
            val onboarding = act.findViewById<ViewPager>(R.id.onboarding)
            onboarding.currentItem = 2
        }
        Binding.btnConsole.click()
        Binding.btnAngry.click()
        Binding.btnSad.click()
        Binding.btnBored.click()
        Binding.btnMemory.click()
        Binding.btnDaily.click()


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onboardingActivity=context as OnboardingActivity
    }


    fun ConstraintLayout.click(){
        var feeling=0
        this.setOnClickListener {
            when(this){
                Binding.btnLove->{feeling=1}
                Binding.btnHappy->{feeling=2}
                Binding.btnConsole->{feeling=3}
                Binding.btnAngry->{feeling=4}
                Binding.btnSad->{feeling=5}
                Binding.btnBored->{feeling=6}
                Binding.btnMemory->{feeling=7}
                Binding.btnDaily->{feeling=8}
            }
            //companion_feeling=feeling
            val onboarding = act.findViewById<ViewPager>(R.id.onboarding)
            onboarding.currentItem = 1
        }
    }

    /*
    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }

     */

    fun timeGenerator() :String{
        // 현재 날짜 가져오기
        val currentDate = Calendar.getInstance()
        val year=currentDate.get(Calendar.YEAR).toString()
        val month=(currentDate.get(Calendar.MONTH)+1).toString()
        val day=currentDate.get(Calendar.DATE).toString()
        val week=currentDate.get(Calendar.DAY_OF_WEEK)

        var strDay=""
        var strMonth=""
        if (month.toInt() < 10) {
            strMonth="0$month"
        }else{strMonth=month}

        if (day.toInt() < 10) {
            strDay="0$day"
        }else{strDay=day}

        var strWeek=""

        when(week){
            1->strWeek="일요일"
            2->strWeek="월요일"
            3->strWeek="화요일"
            4->strWeek="수요일"
            5->strWeek="목요일"
            6->strWeek="금요일"
            7->strWeek="토요일"
        }

        return year+"."+strMonth+"."+strDay+"."+strWeek
    }
}