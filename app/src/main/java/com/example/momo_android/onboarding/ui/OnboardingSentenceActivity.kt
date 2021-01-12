package com.example.momo_android.onboarding.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityOnboardingSentenceBinding
import com.example.momo_android.upload.UploadSentenceAdapter
import com.example.momo_android.upload.data.UploadSentenceData
import com.example.momo_android.util.ItemClickListener

class OnboardingSentenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingSentenceBinding
    private var cardview=0
    private lateinit var uploadSentenceAdapter: UploadSentenceAdapter// 버튼을 Recycler의 형태로 제작
    val datas = mutableListOf<UploadSentenceData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingSentenceBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        val feeling=intent.getIntExtra("feeling",0)
        binding.tvDate.text=intent.getStringExtra("date")
        when(feeling){
            1->{
                binding.tvFeeling.text="사랑"
                binding.imgFeeling.setImageResource(R.drawable.ic_love_14_black)
            }
            2->{
                binding.tvFeeling.text="행복"
                binding.imgFeeling.setImageResource(R.drawable.ic_happy_14_black)
            }
            3->{
                binding.tvFeeling.text="위로"
                binding.imgFeeling.setImageResource(R.drawable.ic_console_14_black)
            }
            4->{
                binding.tvFeeling.text="화남"
                binding.imgFeeling.setImageResource(R.drawable.ic_angry_14_black)
            }
            5->{
                binding.tvFeeling.text="슬픔"
                binding.imgFeeling.setImageResource(R.drawable.ic_sad_14_black)
            }
            6->{
                binding.tvFeeling.text="우울"
                binding.imgFeeling.setImageResource(R.drawable.ic_bored_14_black)
            }
            7->{
                binding.tvFeeling.text="추억"
                binding.imgFeeling.setImageResource(R.drawable.ic_memory_14_black)
            }
            8->{
                binding.tvFeeling.text="일상"
                binding.imgFeeling.setImageResource(R.drawable.ic_daily_14_black)
            }
        }


        //RecylerView 이용한 버튼
        //RecyclerView 밖에 있는 것들도 이 방식을 사용해서 불러옴. Interface를 만들어서
        uploadSentenceAdapter = UploadSentenceAdapter(this)
        uploadSentenceAdapter.setItemClickListener(object: ItemClickListener {
            override fun onClickItem(view: View, position:Int){
                val intent= Intent(this@OnboardingSentenceActivity, OnboardingWriteFirstActivity::class.java)
                intent.putExtra("author",uploadSentenceAdapter.data[position].author)
                intent.putExtra("book",uploadSentenceAdapter.data[position].book)
                intent.putExtra("publisher",uploadSentenceAdapter.data[position].publisher)
                intent.putExtra("sentence",uploadSentenceAdapter.data[position].sentence)
                intent.putExtra("feeling",feeling)
                //Toast.makeText(this@UploadSentenceActivity,uploadSentenceAdapter.data[0].author,Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        })

        binding.rvSelectSentence.adapter=uploadSentenceAdapter
        binding.rvSelectSentence.layoutManager = LinearLayoutManager(this)



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

    }


}