package com.momo.momo_android.onboarding.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityOnboardingSentenceBinding
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.onboarding.ui.OnboardingFeelingActivity.Companion.ONBOARDING_FEELING
import com.momo.momo_android.upload.adapter.UploadSentenceAdapter
import com.momo.momo_android.upload.data.UploadSentenceData
import com.momo.momo_android.util.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OnboardingSentenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingSentenceBinding
    private lateinit var uploadSentenceAdapter: UploadSentenceAdapter// 버튼을 Recycler의 형태로 제작
    private var feeling = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingSentenceBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        //날짜 설정
        setDateData()
        //감정, 감정이미지 설정
        showFeeling(ONBOARDING_FEELING)
        //서버에서 3문장 받아오기
        getOnboardingSentence(ONBOARDING_FEELING)
        //RecylerView 이용한 물방울 버튼 설정
        setRecyclerAdapter()
    }

    //날짜 설정_intent 넘김 받은 값으로
    private fun setDateData() {
        binding.apply {
            tvDate.text= intent.getStringExtra("date")
        }
    }

    //감정,감정이미지 설정
    private fun showFeeling(feeling: Int) {
        binding.apply {
            tvFeeling.text= getEmotionString(feeling,this@OnboardingSentenceActivity)
            imgFeeling.setImageResource(getEmotionImage(feeling))
        }
    }

    //3개 문장 설정_ 서버연결
    private fun getOnboardingSentence(emotionId: Int) {
        RequestToServer.service.getOnboarding(
            Authorization = SharedPreferenceController.getAccessToken(this),
            emotionId = emotionId
        ).enqueue(object : Callback<ResponseOnboardingData> {
            override fun onResponse(
                call: Call<ResponseOnboardingData>,
                response: Response<ResponseOnboardingData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        Log.d(
                            "OnboardingSentence",
                            "success : ${response.body()!!.data}, message : ${response.message()}"
                        )

                        // recyclerview에 문장 등록
                        uploadSentenceAdapter.data = mutableListOf()

                        val sentence1 = it.data.sentence_01
                        val sentence2 = it.data.sentence_02
                        val sentence3 = it.data.sentence_03

                        uploadSentenceAdapter.data.add(
                            UploadSentenceData(
                                sentence1.writer, "<" + sentence1.bookName + ">",
                                "(" + sentence1.publisher + ")", sentence1.contents
                            )
                        )

                        uploadSentenceAdapter.data.add(
                            UploadSentenceData(
                                sentence2.writer, "<" + sentence2.bookName + ">",
                                "(" + sentence2.publisher + ")", sentence2.contents
                            )
                        )

                        uploadSentenceAdapter.data.add(
                            UploadSentenceData(
                                sentence3.writer, "<" + sentence3.bookName + ">",
                                "(" + sentence3.publisher + ")", sentence3.contents
                            )
                        )
                        uploadSentenceAdapter.notifyDataSetChanged()


                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseOnboardingData>, t: Throwable) {
                Log.d("OnboardingSentence", "fail : ${t.message}")
            }

        })
    }
    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
        Log.d("OnboardingSentence", ob.getString("message"))
    }


    //Recycler View를 이용한 버튼을 생성하기 위해 Adapter을 연결
    fun setRecyclerAdapter(){
        //RecyclerView 밖에 있는 것들도 이 방식을 사용해서 불러옴. Interface를 만들어서
        binding.apply {
            uploadSentenceAdapter = UploadSentenceAdapter(this@OnboardingSentenceActivity)
            uploadSentenceAdapter.setItemClickListener(object : ItemClickListener {
                override fun onClickItem(view: View, position: Int) {

                    //버튼이 클릭되면 다음 페이지로 넘어감
                    val intent = Intent(
                        this@OnboardingSentenceActivity,
                        OnboardingWriteActivity::class.java
                    )
                    //intent 넘김
                   intent.putExtra("author", uploadSentenceAdapter.data[position].author)
                    intent.putExtra("book", uploadSentenceAdapter.data[position].book)
                    intent.putExtra("publisher", uploadSentenceAdapter.data[position].publisher)
                    intent.putExtra("sentence", uploadSentenceAdapter.data[position].sentence)
                    intent.putExtra("feeling", feeling)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }
            })

            rvSelectSentence.adapter = uploadSentenceAdapter
            rvSelectSentence.layoutManager = LinearLayoutManager(this@OnboardingSentenceActivity)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
    }

}
