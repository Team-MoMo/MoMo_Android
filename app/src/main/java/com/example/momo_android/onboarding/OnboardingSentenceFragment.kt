package com.example.momo_android.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.momo_android.R
import com.example.momo_android.databinding.FragmentOnboardingSentenceBinding
import com.example.momo_android.databinding.FragmentOnboardingStartBinding
import com.example.momo_android.ui.OnboardingActivity
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_author
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_book
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_date
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_feeling
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_publisher
import com.example.momo_android.ui.OnboardingActivity.Companion.companion_sentence
import com.example.momo_android.ui.UploadWriteActivity
import com.example.momo_android.upload.UploadSentenceAdapter
import com.example.momo_android.upload.UploadSentenceData
import com.example.momo_android.util.ItemClickListener
import kotlinx.android.synthetic.main.activity_onboarding.*
import java.util.*


class OnboardingSentenceFragment : Fragment() {
    private var _Binding: FragmentOnboardingSentenceBinding? = null
    private val Binding get() = _Binding!!
    var onboardingActivity: OnboardingActivity?=null
    private lateinit var uploadSentenceAdapter: UploadSentenceAdapter

    //Activity로 접근
    private val act by lazy {context as OnboardingActivity}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _Binding = FragmentOnboardingSentenceBinding.inflate(layoutInflater)
        return Binding.root
        Binding.tvDate.text= companion_date
        Binding.tvFeeling.text= companion_feeling
        //return inflater.inflate(R.layout.bottomsheet_custom, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //(companion_feeling)
        Log.d("feeling", companion_feeling)

        uploadSentenceAdapter = UploadSentenceAdapter(view.context)
        Binding.rvSelectSentence.adapter=uploadSentenceAdapter
        Binding.rvSelectSentence.layoutManager = LinearLayoutManager(view.context)
        uploadSentenceAdapter.data = mutableListOf(
            UploadSentenceData(
                "구병모",
                "<파과>",
                "(위즈덤하우스)",
                "점입가경, 이게 웬 심장이 콧구멍으로 쏟아질 얘긴가 싶지만 그저 지레짐작이나 얻어걸린 이야기일 가능성이 더 많으니 조각은 표정을 바꾸지 않는다."
            ),
            UploadSentenceData("author", "<book>", "(publisher)", "한줄이다"),
            UploadSentenceData(
                "박연준",
                "<인생은 이상하게 흐른다>",
                "(달)",
                "소년이여, 야망을 가져라란 말이었다. 분명 어디서 들어본듯한, 그러나 은근히 책상머리에 붙여놓아도 별 손색이 없을, 미적지근 훌륭한 격언이라는 생각이 들었다."
            )
        )
        uploadSentenceAdapter.notifyDataSetChanged()

        uploadSentenceAdapter.setItemClickListener(object: ItemClickListener {
            override fun onClickItem(view: View, position:Int){
                companion_author=uploadSentenceAdapter.data[position].author
                companion_book=uploadSentenceAdapter.data[position].book
                companion_publisher=uploadSentenceAdapter.data[position].publisher
                companion_sentence=uploadSentenceAdapter.data[position].sentence

                act.onboarding.currentItem = 3
            }
        })


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onboardingActivity=context as OnboardingActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _Binding = null
    }



    fun changeFeeling(feeling:Int){
        when(feeling){
            1->{
                Binding.tvFeeling.text="사랑"
                Binding.imgFeeling.setImageResource(R.drawable.ic_love_14_black)
            }
            2->{
                Binding.tvFeeling.text="행복"
                Binding.imgFeeling.setImageResource(R.drawable.ic_happy_14_black)
            }
            3->{
                Binding.tvFeeling.text="위로"
                Binding.imgFeeling.setImageResource(R.drawable.ic_console_14_black)
            }
            4->{
                Binding.tvFeeling.text="화남"
                Binding.imgFeeling.setImageResource(R.drawable.ic_angry_14_black)
            }
            5->{
                Binding.tvFeeling.text="슬픔"
                Binding.imgFeeling.setImageResource(R.drawable.ic_sad_14_black)
            }
            6->{
                Binding.tvFeeling.text="우울"
                Binding.imgFeeling.setImageResource(R.drawable.ic_bored_14_black)
            }
            7->{
                Binding.tvFeeling.text="추억"
                Binding.imgFeeling.setImageResource(R.drawable.ic_memory_14_black)
            }
            8->{
                Binding.tvFeeling.text="일상"
                Binding.imgFeeling.setImageResource(R.drawable.ic_daily_14_black)
            }
            100->{
                Binding.tvFeeling.text="No...."
            }
            0->{
                Binding.tvFeeling.text="0"
            }
        }
    }
}