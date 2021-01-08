package com.example.momo_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityListBinding
import com.example.momo_android.list.FilterBottomSheetFragment
import com.example.momo_android.list.ListAdapter
import com.example.momo_android.list.ListData
import java.lang.StringBuilder
import java.util.*

class ListActivity : AppCompatActivity() {

    companion object {
        var filter_year = 0
        var filter_month = 0
        var filter_emotion = 0
        var filter_depth = 0
    }

    private lateinit var binding : ActivityListBinding
    private lateinit var listAdapter : ListAdapter

    private var selectEmotion = 0
    private var selectDepth = 0

    private lateinit var currentDate : Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setCurrentDate()

        initToolbar()

        listAdapter = ListAdapter(this)
        binding.rcvList.adapter = listAdapter
        binding.rcvList.layoutManager = LinearLayoutManager(this)

        loadData()

    }

    private fun initToolbar() {
        //툴바 사용 설정
        setSupportActionBar(binding.toolbarList)

        //툴바 title에 현재 날짜 지정
        val currentDate = StringBuilder(filter_year.toString())
        currentDate.append("년 ")
            .append(filter_month.toString())
            .append("월")
        binding.collapsingtoolbarlayoutList.title = currentDate.toString()

        //toolbar 왼쪽 버튼 사용
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.btn_back_black)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.filter -> {
                //filter 버튼 클릭 시 발생하는 이벤트 설정
                val frag = FilterBottomSheetFragment { date: String, pickDate : IntArray, isCurrentDate: Boolean, emotion: Int, depth: Int ->

                    filter_year = pickDate[0]
                    filter_month = pickDate[1]
                    filter_emotion = emotion
                    filter_depth = depth

                    // 필터 모달에서 선택한 날짜로 toolbar의 title도 변경
                    binding.collapsingtoolbarlayoutList.title = date

                    if (isCurrentDate && emotion == 0 && depth == 0) {
                        // 필터 모달에서 선택한 날짜가 현재 날짜와 같고(변경 x), 감정/깊이 항목에서 어떤 것도 선택하지 않았을 때
                        item.setIcon(R.drawable.list_btn_filter)
                    }
                    else {
                        // 필터의 세 가지 항목 중 어떤 것이라도 선택 혹은 변경했을 때
                        item.setIcon(R.drawable.list_btn_filter_blue)
                    }
                }
                frag.show(supportFragmentManager, frag.tag)
            }
            R.id.graph -> {
                //graph 버튼 클릭 시 발생하는 이벤트 설정
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setCurrentDate() {
        currentDate = Calendar.getInstance()
        filter_year = currentDate.get(Calendar.YEAR)
        filter_month = currentDate.get(Calendar.MONTH) + 1
    }

    private fun loadData() {
        listAdapter.data = mutableListOf(
            ListData(
                baseContext?.getDrawable(R.drawable.ic_happy_blue),
                "행복",
                "12.01",
                "화",
                "1,005m",
                "나는 사랑받고 있다고 느꼈다. 사랑에는 언제나 한 방울의 연민이 포함되기 때문이다.",
                "박연준",
                "<인생은 이상하게 흐른다>",
                "(달)",
                "모모모모 크리스마스가 끝이 났다. 크리스마스가 끝이 났다. 모모 크리스마스가 끝이 났다. 모모 크리스마스가 끝이 났다. 모모모모 크리스마스가 끝이 났다. 모모 크리스마스가 끝이 났다. 모모모모 크리스마스가 끝이 났다. 모모 크리스마스가 끝이 났다. 모모모모모모 크리스마스가 끝이 났다.모모모모 크리스마스가 끝이 났다."
            ),
            ListData(
                baseContext?.getDrawable(R.drawable.ic_happy_blue),
                "행복",
                "12.01",
                "화",
                "1,005m",
                "나는 사랑받고 있다고 느꼈다. 사랑에는 언제나 한 방울의 연민이 포함되기 때문이다.",
                "박연준",
                "<인생은 이상하게 흐른다>",
                "(달)",
                "오늘 새벽엔 눈이 내렸다. 창문을 열어 창문을 열어 흰"
            ),
            ListData(
                baseContext?.getDrawable(R.drawable.ic_happy_blue),
                "행복",
                "12.01",
                "화",
                "1,005m",
                "나는 사랑받고 있다고 느꼈다. 사랑에는 언제나 한 방울의 연민이 포함되기 때문이다.",
                "박연준",
                "<인생은 이상하게 흐른다>",
                "(달)",
                "오늘 새벽엔 눈이 내렸다. 창문을 열어 창문을 열어 흰 눈"
            ),ListData(
                baseContext?.getDrawable(R.drawable.ic_happy_blue),
                "행복",
                "12.01",
                "화",
                "1,005m",
                "나는 사랑받고 있다고 느꼈다. 사랑에는 언제나 한 방울의 연민이 포함되기 때문이다.",
                "박연준",
                "<인생은 이상하게 흐른다>",
                "(달)",
                "모모모모 크리스마스가 끝이 났다."
            ),
            ListData(
                baseContext?.getDrawable(R.drawable.ic_happy_blue),
                "행복",
                "12.01",
                "화",
                "1,005m",
                "나는 사랑받고 있다고 느꼈다. 사랑에는 언제나 한 방울의 연민이 포함되기 때문이다.",
                "박연준",
                "<인생은 이상하게 흐른다>",
                "(달)",
                "오늘 새벽엔 눈이 내렸다. 창문을 열어 창문을 열어"
            )
        )
        listAdapter.notifyDataSetChanged()
    }

}