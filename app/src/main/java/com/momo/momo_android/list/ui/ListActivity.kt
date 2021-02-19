package com.momo.momo_android.list.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityListBinding
import com.momo.momo_android.diary.ui.DiaryActivity
import com.momo.momo_android.list.*
import com.momo.momo_android.list.adapter.FilterLabelAdapter
import com.momo.momo_android.list.adapter.ListAdapter
import com.momo.momo_android.list.data.ListData
import com.momo.momo_android.list.data.ResponseFilterData
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.upload.ui.UploadFeelingActivity
import com.momo.momo_android.util.*
import kotlinx.android.synthetic.main.activity_list.*
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

        var filter_current_year = 0
        var filter_current_month = 0

        lateinit var mContext : ListActivity
        private set
    }

    private lateinit var binding : ActivityListBinding
    private lateinit var listAdapter : ListAdapter
    private lateinit var filterLabelAdapter : FilterLabelAdapter

    private lateinit var selectEmotion : String
    private lateinit var selectDepth : String

    // picker에서 선택한 날짜가 현재 날짜인 경우 true
    private var selectCurrentDate = false

    private var fromDiaryFlag = false

    private var isCurrentDate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mContext = this

        filter_emotion = null
        filter_depth = null

        setCurrentDate(intent.getIntExtra("year", 0), intent.getIntExtra("month", 0))

        initToolbar()

        initFilterLabelRecycler()

        initListRecycler()

    }

    override fun onResume() {
        super.onResume()

        loadFilteredData()
    }

    private fun initFilterLabelRecycler() {
        filterLabelAdapter = FilterLabelAdapter(this)
        binding.apply {
            rcvFilterLabel.adapter = filterLabelAdapter
            rcvFilterLabel.layoutManager = LinearLayoutManager(this@ListActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initListRecycler() {
        listAdapter = ListAdapter(this)
        binding.apply {
            rcvList.adapter = listAdapter
            rcvList.layoutManager = LinearLayoutManager(this@ListActivity)
        }
    }

    fun disableScroll() {
        binding.nestedscrollviewList.isNestedScrollingEnabled = false
    }

    fun enableScroll() {
        binding.nestedscrollviewList.isNestedScrollingEnabled = true
    }

    private fun scrollTop() {
        binding.apply {
            nestedscrollviewList.scrollTo(0, rcvFilterLabel.top)
            appbarlayoutList.setExpanded(true)
        }
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
                setFilterModalResultToList()
            }
            R.id.graph -> {
                //graph 버튼 클릭 시 발생하는 이벤트 설정
                goToReportActivity()
            }
            android.R.id.home -> {
                // back 버튼 클릭 시 발생하는 이벤트 설정 -> home 화면으로 다시 이동
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFilterModalResultToList() {
        val frag = FilterBottomSheetFragment { date: String, pickDate : IntArray, isCurrentDate: Boolean, emotion: Int?, depth: Int? ->

            filter_year = pickDate[0]
            filter_month = pickDate[1]
            filter_emotion = emotion
            filter_depth = depth
            selectCurrentDate = isCurrentDate

            // 필터 모달에서 선택한 날짜로 toolbar의 title도 변경
            binding.collapsingtoolbarlayoutList.title = date

            activeFilterButton()

            setLabelData()

            loadFilterLabelData()

            loadFilteredData()

        }
        frag.show(supportFragmentManager, frag.tag)
    }

    private fun goToReportActivity() {
        fromDiaryFlag = true

        val intent = Intent(this, ReportActivity::class.java)
        startActivity(intent)
    }

    private fun setCurrentDate(year : Int, month : Int) {
        filter_year = year
        filter_month = month
        filter_current_year = year
        filter_current_month = month
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

    private fun setLabelData() {
        if (filter_emotion != null)
            selectEmotion = getEmotionString(filter_emotion!!, this)

        if (filter_depth != null)
            selectDepth = getDepthString(filter_depth!!, this)
    }

    private fun loadFilterLabelData() {

        if (filter_emotion == null && filter_depth == null) {
            binding.rcvFilterLabel.visibility = View.GONE
        }

        else {
            binding.rcvFilterLabel.visibility = View.VISIBLE

            if (filter_emotion == null && filter_depth != null) {
                filterLabelAdapter.data = mutableListOf(selectDepth)
            }

            if (filter_emotion != null && filter_depth == null) {
                filterLabelAdapter.data = mutableListOf(selectEmotion)
            }

            if (filter_emotion != null && filter_depth != null) {
                filterLabelAdapter.data =
                    mutableListOf(selectEmotion, selectDepth)
            }
            filterLabelAdapter.notifyDataSetChanged()
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

    private fun isCurrentDate() {
        // 스크롤뷰에서 넘어온 날짜와 필터에서 선택한 년/월이 같으면 true, 아니면 false
        isCurrentDate = filter_year == filter_current_year && filter_month == filter_current_month
    }

    private fun loadListData(data: List<ListData>) {

        // 리사이클러뷰의 최상단으로 이동
        // 다이어리뷰에서 back 버튼을 눌러서 다시 리스트뷰로 왔을 땐, 최상단으로 스크롤 x
        if (!fromDiaryFlag) {
            scrollTop()   
        }
        fromDiaryFlag = false

        // 검색 결과가 없을 때 (데이터가 0개)
        if (data.isEmpty()) {

            // 처음 진입화면에서는 bottomsheet에서 선택한 년/월이 아니라 접속해 있는 현재 날짜와 같은지 비교해야 함
            isCurrentDate()

            disableScroll()

            // 현재 월에 아무 일기도 쓰지 않았을 때
            if (data.isEmpty() && isCurrentDate && filter_emotion == null && filter_depth == null) {
                visibleUploadView()

                // + 버튼 클릭 시 upload 뷰로 이동
                goToUploadActivity()
            }
            else {
                visibleFilteredNoneView()
            }
        }

        // 서버로부터 받아온 데이터가 empty가 아닐 때
        if (data.isNotEmpty()) {
            visibleRecyclerView()

            listAdapter.data = mutableListOf()

            for (i in data.indices) {
                listAdapter.data.add(
                    com.momo.momo_android.list.adapter.ListData(
                        baseContext?.getDrawable(getEmotionImg(data[i].emotionId)),
                        data[i].Emotion.name,
                        getFormedDate(data[i].wroteAt),
                        getFormedDay(data[i].wroteAt),
                        getDepthString(data[i].depth, this),
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

        initItemClickListener(data)
    }

    private fun visibleUploadView() {
        binding.apply {
            rcvList.visibility = View.GONE
            constraintlayoutListFilterdNone.visibility = View.GONE
            constraintlayoutListNone.visibility = View.VISIBLE
        }
    }

    private fun visibleFilteredNoneView() {
        binding.apply {
            rcvList.visibility = View.GONE
            constraintlayoutListNone.visibility = View.GONE
            constraintlayoutListFilterdNone.visibility = View.VISIBLE
        }
    }

    private fun visibleRecyclerView() {
        binding.apply {
            rcvList.visibility = View.VISIBLE
            constraintlayoutListFilterdNone.visibility = View.GONE
            constraintlayoutListNone.visibility = View.GONE
        }
    }

    private fun goToUploadActivity() {
        binding.imagebuttonListCreateDiary.setOnClickListener {
            val intent = Intent(this, UploadFeelingActivity::class.java)
            intent.putExtra("intentFrom", "List -> Upload")
            startActivity(intent)
        }
    }

    // DiaryActivity로 intent 보내는 부분
    private fun initItemClickListener(data: List<ListData>) {
        listAdapter.setItemClickListener(object : ItemClickListener{
            override fun onClickItem(view: View, position: Int) {
                fromDiaryFlag = true

                val intentFrom = "List -> Diary"
                val id = data[position].id

                val intent = Intent(this@ListActivity, DiaryActivity::class.java)
                intent.putExtra("intentFrom", intentFrom)
                intent.putExtra("diaryId", id)
                startActivity(intent)
            }
        })
    }

    fun loadFilteredData() {
        RequestToServer.service.getFilterdDiary(
            Authorization = SharedPreferenceController.getAccessToken(this),
            userId = SharedPreferenceController.getUserId(this),
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
                    ?.let { _ ->
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