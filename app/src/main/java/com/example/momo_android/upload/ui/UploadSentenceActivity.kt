package com.example.momo_android.upload.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.momo_android.R
import com.example.momo_android.upload.adapter.UploadSentenceAdapter
import com.example.momo_android.databinding.ActivityUploadSentenceBinding
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.upload.data.Data
import com.example.momo_android.upload.data.ResponseSentenceData
import com.example.momo_android.upload.data.UploadSentenceData
import com.example.momo_android.util.ItemClickListener
import com.example.momo_android.util.SharedPreferenceController
import com.example.momo_android.util.showToast
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadSentenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadSentenceBinding
    private var cardview = 0
    private lateinit var uploadSentenceAdapter: UploadSentenceAdapter// 버튼을 Recycler의 형태로 제작

    private var sentence1 = 0
    private var sentence2 = 0
    private var sentence3 = 0

    companion object {
        var activity : Activity? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadSentenceBinding.inflate(layoutInflater) // 2
        val view = binding.root // 3
        setContentView(view)

        activity = this

        val date=intent.getStringExtra("date")
        binding.tvDate.text=date.toString()
        val wroteAt=intent.getStringExtra("wroteAt")

        val feeling = intent.getIntExtra("feeling", 0)
        when (feeling) {
            1 -> {
                binding.tvFeeling.text = "사랑"
                binding.imgFeeling.setImageResource(R.drawable.ic_love_14_black)
            }
            2 -> {
                binding.tvFeeling.text = "행복"
                binding.imgFeeling.setImageResource(R.drawable.ic_happy_14_black)
            }
            3 -> {
                binding.tvFeeling.text = "위로"
                binding.imgFeeling.setImageResource(R.drawable.ic_console_14_black)
            }
            4 -> {
                binding.tvFeeling.text = "화남"
                binding.imgFeeling.setImageResource(R.drawable.ic_angry_14_black)
            }
            5 -> {
                binding.tvFeeling.text = "슬픔"
                binding.imgFeeling.setImageResource(R.drawable.ic_sad_14_black)
            }
            6 -> {
                binding.tvFeeling.text = "우울"
                binding.imgFeeling.setImageResource(R.drawable.ic_bored_14_black)
            }
            7 -> {
                binding.tvFeeling.text = "추억"
                binding.imgFeeling.setImageResource(R.drawable.ic_memory_14_black)
            }
            8 -> {
                binding.tvFeeling.text = "일상"
                binding.imgFeeling.setImageResource(R.drawable.ic_daily_14_black)
            }
        }

        // 서버로부터 문장 3개 받아오기
        loadSentenceData(feeling)

        //< 뒤로가기버튼
        binding.imgBack.setOnClickListener {
            //홈화면
            finish()
        }

        //X 버튼
        binding.imgClose.setOnClickListener {
            //Upload 들어오기 전화면 보여주기
            UploadFeelingActivity.activity?.finish()
            finish()
        }

        //RecylerView 이용한 버튼
        //RecyclerView 밖에 있는 것들도 이 방식을 사용해서 불러옴. Interface를 만들어서
        uploadSentenceAdapter = UploadSentenceAdapter(this)
        uploadSentenceAdapter.setItemClickListener(object : ItemClickListener {
            override fun onClickItem(view: View, position: Int) {

                var sentenceId = 0

                when (position) {
                    0 -> sentenceId = sentence1
                    1 -> sentenceId = sentence2
                    2 -> sentenceId = sentence3
                }

                val intent = Intent(this@UploadSentenceActivity, UploadWriteActivity::class.java)
                intent.putExtra("feeling", feeling)
                intent.putExtra("date", binding.tvDate.text.toString())
                intent.putExtra("author", uploadSentenceAdapter.data[position].author)
                intent.putExtra("book", uploadSentenceAdapter.data[position].book)
                intent.putExtra("publisher", uploadSentenceAdapter.data[position].publisher)
                intent.putExtra("sentence", uploadSentenceAdapter.data[position].sentence)
                intent.putExtra("sentenceId", sentenceId)
                intent.putExtra("emotionId", feeling)
                intent.putExtra("wroteAt",wroteAt)
                //Toast.makeText(this@UploadSentenceActivity,uploadSentenceAdapter.data[0].author,Toast.LENGTH_SHORT).show()
                startActivity(intent)
                overridePendingTransition(R.anim.horizontal_left_in, R.anim.horizontal_right_out)
            }
        })

        binding.rvSelectSentence.adapter = uploadSentenceAdapter
        binding.rvSelectSentence.layoutManager = LinearLayoutManager(this)

    }

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

}