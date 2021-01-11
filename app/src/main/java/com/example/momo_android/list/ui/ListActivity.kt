package com.example.momo_android.list.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityListBinding
import com.example.momo_android.list.*
import java.lang.StringBuilder
import java.util.*

class ListActivity : AppCompatActivity() {

    companion object {

        var filter_year = 0
        var filter_month = 0
        var filter_emotion = 0
        var filter_depth = 0

        // picker에서 선택한 날짜가 현재 날짜인 경우 true
        var selectCurrentDate = false

        lateinit var mContext : Context
    }

    private lateinit var binding : ActivityListBinding
    private lateinit var listAdapter : ListAdapter
    private lateinit var filterLabelAdapter : FilterLabelAdapter

    private lateinit var currentDate : Calendar

    private lateinit var selectEmotion : String
    private lateinit var selectDepth : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mContext = this

        setCurrentDate()

        initToolbar()

        filterLabelAdapter = FilterLabelAdapter(this)
        binding.rcvFilterLabel.adapter = filterLabelAdapter
        binding.rcvFilterLabel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        listAdapter = ListAdapter(this)
        binding.rcvList.adapter = listAdapter
        binding.rcvList.layoutManager = LinearLayoutManager(this)

        loadListData()

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
                    selectCurrentDate = isCurrentDate

                    Log.d("filter0", filter_emotion.toString())
                    Log.d("filter0", filter_depth.toString())

                    // 필터 모달에서 선택한 날짜로 toolbar의 title도 변경
                    binding.collapsingtoolbarlayoutList.title = date

                    activeFilterButton()

                    setLabelData()

                    loadFilterLabelData()

                }
                frag.show(supportFragmentManager, frag.tag)
            }
            R.id.graph -> {
                //graph 버튼 클릭 시 발생하는 이벤트 설정
            }
            android.R.id.home -> {
                // back 버튼 클릭 시 발생하는 이벤트 설정 -> home 화면으로 다시 이동
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setCurrentDate() {
        currentDate = Calendar.getInstance()
        filter_year = currentDate.get(Calendar.YEAR)
        filter_month = currentDate.get(Calendar.MONTH) + 1
    }

    fun activeFilterButton() {
        if (selectCurrentDate && filter_emotion == 0 && filter_depth == 0) {
            // 필터 모달에서 선택한 날짜가 현재 날짜와 같고(변경 x), 감정/깊이 항목에서 어떤 것도 선택하지 않았을 때
            val filterIcon = binding.toolbarList.menu.findItem(R.id.filter)
            filterIcon.setIcon(R.drawable.list_btn_filter)
        }
        else {
            // 필터의 세 가지 항목 중 어떤 것이라도 선택 혹은 변경했을 때
            val filterIcon = binding.toolbarList.menu.findItem(R.id.filter)
            filterIcon.setIcon(R.drawable.list_btn_filter_blue)
        }
    }

    /*
    * 화면이 바뀌는 시점(선택한 필터에 따라 리스트가 업데이트 되는 시점)은 bottomsheet에서 적용버튼을 눌러 필터를 적용할 때 or 필터 라벨을 삭제할 때 뿐임
    *
    * ** 새로 일기를 작성하겠냐는 뷰는 리스트 뷰에 처음 들어갔을 때만 초기화면으로 뜸 (현재 년/월에 작성한 일기가 없을 경우) => 그 뒤로 날짜를 바꾸거나, 감정/깊이를 선택했을 때 그에 해당되는 일기가 없으면 검색된 결과가 없습니다가 뜸
    *   => 툴바의 필터 버튼과 동일하게 작동하는 것
    *
    * 1. 현재 월과 일치 && 현재 월에 일기를 하나도 쓰지 않은 경우 -> 새로 일기를 작성하겠냐는 뷰가 뜸 (감정/깊이 필터는 적용하지 않은 상태, 날짜만 선택함 => 감정/깊이 선택시에는 검색된 결과가 없습니다가 뜸)
    * 2. 현재 월과 일치 && 현재 월에 쓴 일기가 하나라도 있는 경우
    *   2-1) 이번 달에 쓴 일기들이 리스트됨 (날짜만 선택한 경우, 감정/깊이 선택 x)
    *   2-2) 이번 달에 쓴 일기의 감정/깊이 중 하나가 선택한 필터 항목에 걸릴 경우 -> 선택한 필터에 해당되는 이번달에 쓴 일기들이 리스트됨 (감정/깊이까지 선택한 경우)
    *   2-3) 이번 달에 쓴 일기의 감정/깊이 중 어떤 것도 선택한 필터 항목에 걸리지 않는 경우 -> 검색된 결과가 없습니다 뷰가 뜸 (감정/깊이까지 선택한 경우)
    *
    * 1. 오늘 날짜가 아님 && 필터 적용 시 검색된 결과가 없는 경우 -> 검색된 결과가 없습니다 뷰가 뜸
    * 2. 오늘 날짜가 아님 && 필터 적용 시 검색된 결과가 있는 경우 -> 해당되는 일기들이 리스트됨 */

    private fun getCurrentMonthDiary() {

    }

    private fun setLabelData() {
        when (filter_emotion) {
            1 -> selectEmotion = "사랑"
            2 -> selectEmotion = "행복"
            3 -> selectEmotion = "위로"
            4 -> selectEmotion = "화남"
            5 -> selectEmotion = "슬픔"
            6 -> selectEmotion = "우울"
            7 -> selectEmotion = "추억"
            8 -> selectEmotion = "일상"
            else -> selectEmotion = "선택안함" //Log.d("setLabelData", "emotion: nothing selected") // filter_emotion == 0
        }
        Log.d("filter1", filter_emotion.toString())
        Log.d("filter1", selectEmotion)

        when (filter_depth) {
            1 -> selectDepth = "2m"
            2 -> selectDepth = "30m"
            3 -> selectDepth = "100m"
            4 -> selectDepth = "300m"
            5 -> selectDepth = "700m"
            6 -> selectDepth = "1,005m"
            7 -> selectDepth = "심해"
            else -> selectDepth = "선택안함" //Log.d("setLabelData", "depth: nothing selected") // filter_depth == 0
        }
        Log.d("filter2", filter_depth.toString())
        Log.d("filter3", selectDepth)
    }

    private fun loadFilterLabelData() {

        Log.d("filter3", filter_emotion.toString())
        Log.d("filter3", filter_depth.toString())

        if (filter_emotion == 0 && filter_depth == 0) {
            binding.rcvFilterLabel.visibility = View.GONE
            Log.d("filter4", "0, 0")
        }

        else {
            binding.rcvFilterLabel.visibility = View.VISIBLE

            if (filter_emotion == 0 && filter_depth != 0) {
                filterLabelAdapter.data = mutableListOf(FilterLabelData(selectDepth))
                Log.d("filter4", "0, 1")
            }

            if (filter_emotion != 0 && filter_depth == 0) {
                filterLabelAdapter.data = mutableListOf(FilterLabelData(selectEmotion))
                Log.d("filter4", "1, 0")
            }

            if (filter_emotion != 0 && filter_depth != 0) {
                filterLabelAdapter.data =
                    mutableListOf(FilterLabelData(selectEmotion), FilterLabelData(selectDepth))
                Log.d("filter4", "1, 1")
            }
            filterLabelAdapter.notifyDataSetChanged()
        }
    }

    private fun loadListData() {
        listAdapter.data = mutableListOf(
            ListData(
                baseContext?.getDrawable(R.drawable.ic_happy_blue),
                "행복",
                "12.01",
                "화",
                "1,005m",
                "나는 사랑받고 있다고 느꼈다. 사랑에는 언제나 한 방울의 연민이 포함되기 때문이다.",
                "박연준",
                "<인생은 이상하게 흐른다인생은 이상하게>",
                "(달달달)",
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