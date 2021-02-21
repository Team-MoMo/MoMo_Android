package com.momo.momo_android.list.ui

import android.graphics.Point
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.momo.momo_android.R
import com.momo.momo_android.databinding.ActivityReportBinding
import com.momo.momo_android.list.data.Data
import com.momo.momo_android.list.data.ResponseReportData
import com.momo.momo_android.network.RequestToServer
import com.momo.momo_android.util.SharedPreferenceController
import com.momo.momo_android.util.showToast
import com.google.android.material.tabs.TabLayout
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.util.*

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding

    private var emotionMax = -1
    private var depthMax = -1
    private var maxGraphWidth = 0F
    private var maxGraphHeight = 0F
    private var graphUnit = 0F
    private var heightLimit = 0F

    private lateinit var currentDate: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        selectEmotionTab()

        setCurrentDate()

        getGraphMaxSize()

        initTabLayout()

        initBackButton()

        initBottomSheet()

        loadData()

    }

    private fun setCurrentDate() {
        currentDate = Calendar.getInstance()
        report_year = currentDate.get(Calendar.YEAR)
        report_month = currentDate.get(Calendar.MONTH) + 1

        setDateText()
    }

    private fun setDateText() {
        val dateText = StringBuilder(report_year.toString())
        dateText.append("년 ")
            .append(report_month.toString())
            .append("월")

        binding.tvReportDate.text = dateText
    }

    private fun selectEmotionTab() {
        binding.tablayoutReport.getTabAt(0)?.select()
        val tabLayout: LinearLayout = ((binding.tablayoutReport.getChildAt(0) as ViewGroup) as LinearLayout)
            .getChildAt(0) as LinearLayout
        val tabTextView = tabLayout.getChildAt(1) as TextView
        val typeFace: Typeface? = ResourcesCompat.getFont(this@ReportActivity, R.font.notosanskrmedium)
        tabTextView.typeface = typeFace

        setEmotionGraphVisible()
    }

    private fun initBottomSheet() {
        binding.imageButtonDate.setOnClickListener {
            val frag = ReportBottomSheetFragment {
                report_year = it[0]
                report_month = it[1]

                setDateText()

                loadData()
            }
            frag.show(supportFragmentManager, frag.tag)
        }
    }

    private fun initTabLayout() {
        binding.tablayoutReport.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // tab font 변경
                val tabLayout: LinearLayout = ((binding.tablayoutReport.getChildAt(0) as ViewGroup) as LinearLayout)
                    .getChildAt(tab!!.position) as LinearLayout
                val tabTextView = tabLayout.getChildAt(1) as TextView
                val typeFace: Typeface? = ResourcesCompat.getFont(this@ReportActivity, R.font.notosanskrmedium)
                tabTextView.typeface = typeFace

                if (tab.position == 0) {
                    setEmotionGraphVisible()
                }
                else if (tab.position == 1) {
                    setDepthGraphVisible()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tabLayout: LinearLayout = ((binding.tablayoutReport.getChildAt(0) as ViewGroup) as LinearLayout)
                    .getChildAt(tab!!.position) as LinearLayout
                val tabTextView = tabLayout.getChildAt(1) as TextView
                val typeFace: Typeface? = ResourcesCompat.getFont(this@ReportActivity, R.font.notosanskrlight)
                tabTextView.typeface = typeFace
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun setEmotionGraphVisible() {
        binding.apply {
            constraintlayoutEmotionGraph.visibility = View.VISIBLE
            constraintlayoutDepthGraph.visibility = View.INVISIBLE
        }
    }

    private fun setDepthGraphVisible() {
        binding.apply {
            constraintlayoutDepthGraph.visibility = View.VISIBLE
            constraintlayoutEmotionGraph.visibility = View.INVISIBLE
        }
    }

    private fun getGraphMaxSize() {
        val display : Display = windowManager.defaultDisplay
        val size = Point()
        var width = 0 //360
        var graphWidth = 0F //25
        var graphHeight = 0F //260

        display.getSize(size)
        width = size.x
        graphWidth = (width * 0.06111).toFloat()
        graphHeight = (graphWidth / 0.08461).toFloat() // 한 막대의 최대 높이

        maxGraphWidth = graphWidth
        maxGraphHeight = graphHeight

        heightLimit = (maxGraphHeight * 0.3576).toFloat()
    }

    private fun loadData() {
        RequestToServer.service.getStatistics(
            Authorization = SharedPreferenceController.getAccessToken(this),
            userId = SharedPreferenceController.getUserId(this),
            year = report_year,
            month = report_month
        ).enqueue(object : Callback<ResponseReportData> {
            override fun onResponse(
                call: Call<ResponseReportData>,
                response: Response<ResponseReportData>
            ) {
                response.takeIf { it.isSuccessful}
                    ?.body()
                    ?.let {
                        Log.d("StatActivity-server", "success : ${response.body()!!.data}, message : ${response.message()}")

                        calDiaryCount(response.body()!!.data)

                        loadEmotionData(response.body()!!.data)

                        loadDepthData(response.body()!!.data)

                    } ?: showError(response.errorBody())
            }

            override fun onFailure(call: Call<ResponseReportData>, t: Throwable) {
                Log.d("StatActivity-server", "fail : ${t.message}")
            }

        })
    }

    private fun calDiaryCount(data: Data) {
        var count = 0

        for (i in data.emotionCounts.indices) {
            count += data.emotionCounts[i].count
        }
        setDiaryCount(count)
    }

    private fun setDiaryCount(count: Int) {
        val countText = StringBuilder(count.toString())
        countText.append("개의 일기")

        binding.tvDiaryCount.text = countText
    }

    // 감정 그래프 설정
    private fun loadEmotionData(data: Data) {
        calEmotionMax(data)
        graphUnit = maxGraphHeight / emotionMax

        var num = 0
        for (i in 1..8) {
            if (num <= data.emotionCounts.size-1) {
                if (data.emotionCounts[num].id == i) {
                    setEmotionGraph(i, data.emotionCounts[num].count)
                    num += 1
                }
                else {
                    setEmotionGraph(i, 0)
                }
                continue
            }
            else {
                setEmotionGraph(i, 0)
                continue
            }
        }
    }

    private fun calEmotionMax(data: Data) {
        emotionMax = -1
        for (i in data.emotionCounts.indices) {
            if (emotionMax < data.emotionCounts[i].count) {
                emotionMax = data.emotionCounts[i].count
            }
        }
    }

    private fun setEmotionGraph(emotionId: Int, count: Int) {
        binding.apply {
            when (emotionId) {
                1 -> {
                    graphLove.setEmotionGraphLayout(count)
                    tvCountLove.text = count.toString()
                }
                2 -> {
                    graphHappy.setEmotionGraphLayout(count)
                    tvCountHappy.text = count.toString()
                }
                3 -> {
                    graphConsole.setEmotionGraphLayout(count)
                    tvCountConsole.text = count.toString()
                }
                4 -> {
                    graphAngry.setEmotionGraphLayout(count)
                    tvCountAngry.text = count.toString()
                }
                5 -> {
                    graphSad.setEmotionGraphLayout(count)
                    tvCountSad.text = count.toString()
                }
                6 -> {
                    graphBored.setEmotionGraphLayout(count)
                    tvCountBored.text = count.toString()
                }
                7 -> {
                    graphMemory.setEmotionGraphLayout(count)
                    tvCountMemory.text = count.toString()
                }
                8 -> {
                    graphDaily.setEmotionGraphLayout(count)
                    tvCountDaily.text = count.toString()
                }
                else -> {
                    Log.d("Stat-setEmotionGraph", "error")
                }
            }
        }
    }

    private fun CardView.setEmotionGraphLayout(count: Int) {
        val height = (graphUnit * count).toInt()
        this.apply {
            layoutParams.height = height
            layoutParams.width = maxGraphWidth.toInt()
            requestLayout()
        }
    }

    // 깊이 그래프 설정
    private fun loadDepthData(data: Data) {
        calDepthMax(data)
        graphUnit = maxGraphHeight / depthMax

        var num = 0
        for (i in 0..6) {
            if (num <= data.depthCounts.size-1) {
                if (data.depthCounts[num].depth == i) {
                    setDepthGraph(i, data.depthCounts[num].count)
                    num += 1
                }
                else {
                    setDepthGraph(i, 0)
                }
                continue
            }
            else {
                setDepthGraph(i, 0)
                continue
            }
        }
    }

    private fun calDepthMax(data: Data) {
        depthMax = -1
        for (i in data.depthCounts.indices) {
            if (depthMax < data.depthCounts[i].count) {
                depthMax = data.depthCounts[i].count
            }
        }
    }

    private fun setDepthGraph(depthId: Int, count: Int) {
        var height = 0
        binding.apply {
            when (depthId) {
                0 -> {
                    height = graph2.setDepthGraphLayout(count)
                    tvCount2.text = count.toString()

                    imgDepth2.setVisibleImg(height)
                }
                1 -> {
                    height = graph30.setDepthGraphLayout(count)
                    tvCount30.text = count.toString()

                    imgDepth30.setVisibleImg(height)
                }
                2 -> {
                    height = graph100.setDepthGraphLayout(count)
                    tvCount100.text = count.toString()

                    imgDepth100.setVisibleImg(height)
                }
                3 -> {
                    height = graph300.setDepthGraphLayout(count)
                    tvCount300.text = count.toString()

                    imgDepth300.setVisibleImg(height)
                }
                4 -> {
                    height = graph700.setDepthGraphLayout(count)
                    tvCount700.text = count.toString()

                    imgDepth700.setVisibleImg(height)
                }
                5 -> {
                    height = graph1005.setDepthGraphLayout(count)
                    tvCount1005.text = count.toString()

                    imgDepth1005.setVisibleImg(height)
                }
                6 -> {
                    height = graphUnder.setDepthGraphLayout(count)
                    tvCountUnder.text = count.toString()

                    imgDepthUnder.setVisibleImg(height)
                }
                else -> {
                    Log.d("Stat-setDepthGraph", "error")
                }
            }
        }
    }

    private fun CardView.setDepthGraphLayout(count: Int) : Int {
        val height = (graphUnit * count).toInt()
        this.apply {
            layoutParams.height = height
            layoutParams.width = maxGraphWidth.toInt()
            requestLayout()
        }

        return height
    }

    private fun ImageView.setVisibleImg(height: Int) {
        if (height >= heightLimit)
            this.visibility = View.VISIBLE
        else
            this.visibility = View.GONE
    }

    private fun initBackButton() {
        binding.imagebuttonSettingBack.setOnClickListener {
            finish()
        }
    }

    private fun showError(error : ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        this.showToast(ob.getString("message"))
        Log.d("StatActivity-server", ob.getString("message"))
    }

    companion object {
        var report_year = 0
        var report_month = 0
    }
}