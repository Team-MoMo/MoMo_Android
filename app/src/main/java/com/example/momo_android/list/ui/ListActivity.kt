package com.example.momo_android.list.ui

import android.content.Context
import android.content.Intent
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
import com.example.momo_android.list.data.ListData
import com.example.momo_android.list.data.ResponseFilterData
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.ui.UploadFeelingActivity
import com.example.momo_android.util.showToast
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class ListActivity : AppCompatActivity() {

    companion object {

        var filter_year = 0
        var filter_month = 0
        var filter_emotion : Int? = null
        var filter_depth : Int? = null

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

        filter_emotion = null
        filter_depth = null

        setCurrentDate()

        initToolbar()

        filterLabelAdapter = FilterLabelAdapter(this)
        binding.rcvFilterLabel.adapter = filterLabelAdapter
        binding.rcvFilterLabel.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        listAdapter = ListAdapter(this)
        binding.rcvList.adapter = listAdapter
        binding.rcvList.layoutManager = LinearLayoutManager(this)

        loadFilteredData()

    }

    fun disableScroll() {
        binding.nestedscrollviewList.isNestedScrollingEnabled = false
    }

    fun enableScroll() {
        binding.nestedscrollviewList.isNestedScrollingEnabled = true
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
                val frag = FilterBottomSheetFragment { date: String, pickDate : IntArray, isCurrentDate: Boolean, emotion: Int?, depth: Int? ->

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

                    loadFilteredData()

                }
                frag.show(supportFragmentManager, frag.tag)
            }
            R.id.graph -> {
                //graph 버튼 클릭 시 발생하는 이벤트 설정
                this.showToast("아직 준비 중인 기능입니다.")
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
        if (selectCurrentDate && filter_emotion == null && filter_depth == null) {
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
            0 -> selectDepth = "2m"
            1 -> selectDepth = "30m"
            2 -> selectDepth = "100m"
            3 -> selectDepth = "300m"
            4 -> selectDepth = "700m"
            5 -> selectDepth = "1,005m"
            6 -> selectDepth = "심해"
            else -> selectDepth = "선택안함" //Log.d("setLabelData", "depth: nothing selected") // filter_depth == 0
        }
        Log.d("filter2", filter_depth.toString())
        Log.d("filter3", selectDepth)
    }

    private fun loadFilterLabelData() {

        Log.d("filter3", filter_emotion.toString())
        Log.d("filter3", filter_depth.toString())

        if (filter_emotion == null && filter_depth == null) {
            binding.rcvFilterLabel.visibility = View.GONE
            Log.d("filter4", "0, 0")
        }

        else {
            binding.rcvFilterLabel.visibility = View.VISIBLE

            if (filter_emotion == null && filter_depth != null) {
                filterLabelAdapter.data = mutableListOf(FilterLabelData(selectDepth))
                Log.d("filter4", "0, 1")
            }

            if (filter_emotion != null && filter_depth == null) {
                filterLabelAdapter.data = mutableListOf(FilterLabelData(selectEmotion))
                Log.d("filter4", "1, 0")
            }

            if (filter_emotion != null && filter_depth != null) {
                filterLabelAdapter.data =
                    mutableListOf(FilterLabelData(selectEmotion), FilterLabelData(selectDepth))
                Log.d("filter4", "1, 1")
            }
            filterLabelAdapter.notifyDataSetChanged()
        }
    }

    private fun getDepthString(depth : Int) : String {
        return when (depth) {
            0 -> "2m"
            1 -> "30m"
            2 -> "100m"
            3 -> "300m"
            4 -> "700m"
            5 -> "1,005m"
            6 -> "심해"
            else -> "error"
        }
    }

    private fun getFormedDate(wroteAt: String) : String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREA).parse(wroteAt)
        return SimpleDateFormat("MM.dd", Locale.KOREA).format(dateFormat!!)
    }

    private fun getFormedDay(wroteAt: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss.sss'Z'", Locale.KOREA).parse(wroteAt)
        return SimpleDateFormat("E", Locale.KOREA).format(dateFormat!!)
    }

    private fun getEmotionImg(emotionIdx: Int) : Int {
        return when (emotionIdx) {
            1 -> R.drawable.ic_love_blue
            2 -> R.drawable.ic_happy_blue
            3 -> R.drawable.ic_console_blue
            4 -> R.drawable.ic_angry_blue
            5 -> R.drawable.ic_sad_blue
            6 -> R.drawable.ic_bored_blue
            7 -> R.drawable.ic_memory_blue
            8 -> R.drawable.ic_daily_blue
            else -> R.drawable.ic_happy_blue
        }
    }

    private fun loadListData(data: List<ListData>) {

        // 검색 결과가 없을 때 (데이터가 0개)
        if (data.isEmpty()) {
            binding.rcvList.visibility = View.GONE
            binding.constraintlayoutListNone.visibility = View.GONE
            binding.constraintlayoutListFilterdNone.visibility = View.VISIBLE
        }

        // 현재 월에 아무 일기도 쓰지 않았을 때
        if (data.isEmpty() && selectCurrentDate && filter_emotion == null && filter_depth == null) {
            binding.rcvList.visibility = View.GONE
            binding.constraintlayoutListFilterdNone.visibility = View.GONE
            binding.constraintlayoutListNone.visibility = View.VISIBLE

            // + 버튼 클릭 시 upload 뷰로 이동
            binding.imagebuttonListCreateDiary.setOnClickListener {
                val intent = Intent(this, UploadFeelingActivity::class.java)
                startActivity(intent)
            }
        }

        // 서버로부터 받아온 데이터가 empty가 아닐 때
        if (data.isNotEmpty()) {
            binding.rcvList.visibility = View.VISIBLE
            binding.constraintlayoutListFilterdNone.visibility = View.GONE
            binding.constraintlayoutListNone.visibility = View.GONE

            listAdapter.data = mutableListOf()

            for (i in 0..data.size-1) {
                listAdapter.data.add(
                    ListData(
                        baseContext?.getDrawable(getEmotionImg(data[i].emotionId)),
                        data[i].Emotion.name,
                        getFormedDate(data[i].wroteAt),
                        getFormedDay(data[i].wroteAt),
                        getDepthString(data[i].depth),
                        data[i].Sentence.contents,
                        data[i].Sentence.writer,
                        "<" + data[i].Sentence.bookName + ">",
                        "(" + data[i].Sentence.publisher + ")",
                        data[i].contents
                    )
                )
            }
        }
        listAdapter.notifyDataSetChanged()
    }

    fun loadFilteredData() {
        RequestToServer.service.getFilterdDiary(
            Authorization = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTYxMDI4NTcxOCwiZXhwIjoxNjE4MDYxNzE4LCJpc3MiOiJtb21vIn0.BudOmb4xI78sbtgw81wWY8nfBD2A6Wn4vS4bvlzSZYc",
            userId = 2,
            year = filter_year,
            month = filter_month,
            order = "filter",
            emotionId = filter_emotion,
            depth = filter_depth
        ).enqueue(object : Callback<ResponseFilterData> {
            override fun onResponse(
                call: Call<ResponseFilterData>,
                response: Response<ResponseFilterData>
            ) {
                response.takeIf { it.isSuccessful}
                    ?.body()
                    ?.let { it ->
                        Log.d("ListActivity-server", "success : ${response.body()!!.data}, message : ${response.message()}")

                        loadListData(response.body()!!.data)

                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseFilterData>, t: Throwable) {
                Log.d("ListActivity-server", "fail : ${t.message}")
            }

        })
    }

    private fun showError(error : ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
        Log.d("ListActivity-server", ob.getString("message"))
    }

}