package com.example.momo_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityListBinding
import com.example.momo_android.list.FilterBottomSheetFragment
import com.example.momo_android.list.ListAdapter
import com.example.momo_android.list.ListData

class ListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListBinding
    private lateinit var listAdapter : ListAdapter

    private var selectEmotion = 0
    private var selectDepth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initToolbar()

        listAdapter = ListAdapter(this)
        binding.rcvList.adapter = listAdapter
        binding.rcvList.layoutManager = LinearLayoutManager(this)

        loadData()

    }

    private fun initToolbar() {
        //툴바 사용 설정
        setSupportActionBar(binding.toolbarList)
        binding.collapsingtoolbarlayoutList.title = "2020년 12월"

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
                val frag = FilterBottomSheetFragment { date: String, emotion: Int, depth: Int ->
                    binding.collapsingtoolbarlayoutList.title = date
                    item.setIcon(R.drawable.list_btn_filter_blue)

                    selectEmotion = emotion
                    selectDepth = depth
                }
                frag.show(supportFragmentManager, frag.tag)
            }
            R.id.graph -> {
                //graph 버튼 클릭 시 발생하는 이벤트 설정
            }
        }
        return super.onOptionsItemSelected(item)
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