package com.example.momo_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.momo_android.*
import com.example.momo_android.databinding.ActivityOnboardingBinding
import com.example.momo_android.onboarding.*
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.fragment_onboarding_start.*

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter

    companion object{
        var companion_date=""
        var companion_feeling=""
        var companion_sentence=""
        var companion_author=""
        var companion_book=""
        var companion_publisher=""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        //Adapter로 뷰페이저
        onboardingAdapter= OnboardingAdapter(supportFragmentManager)
        onboardingAdapter.fragments= listOf(
            OnboardingStartFragment(),
            OnboardingFeelingFragment(),
            OnboardingSentenceFragment(),
            OnboardingWriteFragment(),
            OnboardingDepthFragment()

        )
        binding.onboarding.adapter=onboardingAdapter

        binding.onboarding.setSwipePagingEnabled(false)

    }

    /* 뷰페이저를 사용하지 않고 Fragment->Fragment라면 Fragment에서 Activity로 접근해 이런 방식으로 진행하면 좋을듯함.
    fun goNext(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.constraintlayout, fragment).commit()
    }*/



    /*
    fun startFragment(){
        val startFragment: OnboardingStartFragment = OnboardingStartFragment()

        supportFragmentManager.beginTransaction().add(R.id.onboarding, startFragment).commit()
    }
    fun feelingFragment(){
        val feelingFragment: OnboardingFeelingFragment = OnboardingFeelingFragment()
        val transaction_feeling=supportFragmentManager .beginTransaction()
        transaction_feeling.add(R.id.onboarding,feelingFragment)
        //transaction.addToBackStack()//뒤로가기 버튼을 누르면 액티비티종료 말고 프래그먼트 내 뒤로가기 가능
        transaction_feeling.commit()
    }

    fun sentenceFragment(){
        val sentenceFragment: OnboardingSentenceFragment = OnboardingSentenceFragment()
        val transaction=supportFragmentManager .beginTransaction()
        transaction.add(R.id.onboarding,sentenceFragment)
        //transaction.addToBackStack()//뒤로가기 버튼을 누르면 액티비티종료 말고 프래그먼트 내 뒤로가기 가능
        transaction.commit()
    }
    fun writeFragment(){
        val writeFragment: OnboardingWriteFragment = OnboardingWriteFragment()
        val transaction=supportFragmentManager .beginTransaction()
        transaction.add(R.id.onboarding,writeFragment)
        //transaction.addToBackStack()//뒤로가기 버튼을 누르면 액티비티종료 말고 프래그먼트 내 뒤로가기 가능
        transaction.commit()
    }
    fun depthFragment(){
        val depthFragment: OnboardingDepthFragment = OnboardingDepthFragment()
        val transaction=supportFragmentManager .beginTransaction()
        transaction.add(R.id.onboarding,depthFragment)
        //transaction.addToBackStack()//뒤로가기 버튼을 누르면 액티비티종료 말고 프래그먼트 내 뒤로가기 가능
        transaction.commit()
    }

    fun goBack(){
        onBackPressed()
    }
     */
}