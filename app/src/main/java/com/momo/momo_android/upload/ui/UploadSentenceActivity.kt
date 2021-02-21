package com.momo.momo_android.upload.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.momo.momo_android.R
import com.momo.momo_android.upload.adapter.UploadSentenceAdapter
import com.momo.momo_android.databinding.ActivityUploadSentenceBinding
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.upload.data.Data
import com.momo.momo_android.upload.data.ResponseSentenceData
import com.momo.momo_android.upload.data.UploadSentenceData
import com.momo.momo_android.util.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadSentenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadSentenceBinding
    private lateinit var uploadSentenceAdapter: UploadSentenceAdapter// 버튼을 Recycler의 형태로 제작

    private var sentence1 = 0
    private var sentence2 = 0
    private var sentence3 = 0

    private var wroteAt = ""
    private var emotionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadSentenceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        activity = this

        binding.apply {
            tvDate.text = intent.getStringExtra("date").toString()
            //< 뒤로가기버튼: 홈화면으로
            imgBack.setOnClickListener { finish() }
            //X 버튼: Upload들어오기 전 화면으로
            imgClose.setOnClickListener {
                UploadFeelingActivity.activity?.finish()
                finish()
            }
        }
        wroteAt = intent.getStringExtra("wroteAt").toString()
        emotionId = intent.getIntExtra("emotionId", 0)

        // 서버로부터 문장 3개 받아오기
        loadSentenceData(emotionId)

        //감정, 감정이미지 설정
        showFeeling(emotionId)

        //RecylerView 이용한 물방울 버튼 설정
        setRecyclerAdapter()
    }

    //감정,감정이미지 설정
    private fun showFeeling(feeling: Int) {
        binding.apply {
            tvFeeling.text = getEmotionString(feeling, this@UploadSentenceActivity)
            imgFeeling.setImageResource(getEmotionBlack(feeling))
        }
    }

    private fun setRecyclerAdapter() {
        binding.apply {
            uploadSentenceAdapter = UploadSentenceAdapter(this@UploadSentenceActivity)
            uploadSentenceAdapter.setItemClickListener(object : ItemClickListener {
                override fun onClickItem(view: View, position: Int) {

                    var sentenceId = 0

                    when (position) {
                        0 -> sentenceId = sentence1
                        1 -> sentenceId = sentence2
                        2 -> sentenceId = sentence3
                    }

                    val intent =
                        Intent(this@UploadSentenceActivity, UploadWriteActivity::class.java)
                    intent.putExtra("date", tvDate.text.toString())
                    intent.putExtra("author", uploadSentenceAdapter.data[position].author)
                    intent.putExtra("book", uploadSentenceAdapter.data[position].book)
                    intent.putExtra("publisher", uploadSentenceAdapter.data[position].publisher)
                    intent.putExtra("sentence", uploadSentenceAdapter.data[position].sentence)
                    intent.putExtra("sentenceId", sentenceId)
                    intent.putExtra("emotionId", emotionId)
                    intent.putExtra("wroteAt", wroteAt)
                    startActivity(intent)
                    overridePendingTransition(
                        R.anim.horizontal_left_in,
                        R.anim.horizontal_right_out
                    )
                }
            })
            rvSelectSentence.adapter = uploadSentenceAdapter
            rvSelectSentence.layoutManager = LinearLayoutManager(this@UploadSentenceActivity)
        }
    }

    //추천3문장 서버에서 받아와 설정
    private fun loadSentenceData(emotionId: Int) {
        RequestToServer.service.getSentence(
            Authorization = SharedPreferenceController.getAccessToken(this),
            emotionId = emotionId,
            userId = SharedPreferenceController.getUserId(this)
        ).enqueue(object : Callback<ResponseSentenceData> {
            override fun onResponse(
                call: Call<ResponseSentenceData>,
                response: Response<ResponseSentenceData>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        Log.d(
                            "UploadSentence-server",
                            "success : ${response.body()!!.data}, message : ${response.message()}"
                        )

                        // recyclerview에 문장 등록
                        setSentence(response.body()!!.data)

                        sentence1 = it.data[0].id
                        sentence2 = it.data[1].id
                        sentence3 = it.data[2].id

                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseSentenceData>, t: Throwable) {
                Log.d("UploadSentence-server", "fail : ${t.message}")
            }

        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
        Log.d("UploadSentence-server", ob.getString("message"))
    }

    private fun setSentence(data: List<Data>) {
        uploadSentenceAdapter.data = mutableListOf()
        for (i in data.indices) {
            uploadSentenceAdapter.data.add(
                UploadSentenceData(
                    data[i].writer,
                    "<" + data[i].bookName + ">",
                    "(" + data[i].publisher + ")",
                    data[i].contents
                )
            )
        }
        uploadSentenceAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.horizontal_right_in, R.anim.horizontal_left_out)
    }

    companion object {
        var activity: Activity? = null
    }
}