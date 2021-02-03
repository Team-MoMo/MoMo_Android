package com.example.momo_android.list.ui

import android.graphics.Point
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.momo_android.R
import com.example.momo_android.databinding.ActivityReportBinding
import com.example.momo_android.list.data.Data
import com.example.momo_android.list.data.ResponseReportData
import com.example.momo_android.network.RequestToServer
import com.example.momo_android.util.SharedPreferenceController
import com.example.momo_android.util.showToast
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

    companion object {
        var report_year = 0
        var report_month = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        selectEmotionTab()

        setCurrentDate()

        getGraphMaxHeight()

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
        binding.constraintlayoutEmotionGraph.visibility = View.VISIBLE
        binding.constraintlayoutDepthGraph.visibility = View.INVISIBLE
    }

    private fun setDepthGraphVisible() {
        binding.constraintlayoutDepthGraph.visibility = View.VISIBLE
        binding.constraintlayoutEmotionGraph.visibility = View.INVISIBLE
    }

    private fun getGraphMaxHeight() {
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

        Log.d("maxGraphHeight", maxGraphHeight.toString())
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
                    ?.let { it ->
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
                    Log.d("[statis]", "$i : ${data.emotionCounts[num].count}")
                    setEmotionGraph(i, data.emotionCounts[num].count)
                    num += 1
                }
                else {
                    Log.d("[statis]", "$i : 0")
                    setEmotionGraph(i, 0)
                }
                continue
            }
            else {
                Log.d("[statis]", "$i : 0")
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
        var height = 0
        when (emotionId) {
            1 -> {
                height = (graphUnit * count).toInt()
                binding.graphLove.layoutParams.height = height
                binding.graphLove.layoutParams.width = maxGraphWidth.toInt()
                binding.graphLove.requestLayout()
                binding.tvCountLove.text = count.toString()
            }
            2 -> {
                height = (graphUnit * count).toInt()
                binding.graphHappy.layoutParams.height = height
                binding.graphHappy.layoutParams.width = maxGraphWidth.toInt()
                binding.graphHappy.requestLayout()
                binding.tvCountHappy.text = count.toString()
            }
            3 -> {
                height = (graphUnit * count).toInt()
                binding.graphConsole.layoutParams.height = height
                binding.graphConsole.layoutParams.width = maxGraphWidth.toInt()
                binding.graphConsole.requestLayout()
                binding.tvCountConsole.text = count.toString()
            }
            4 -> {
                height = (graphUnit * count).toInt()
                binding.graphAngry.layoutParams.height = height
                binding.graphAngry.layoutParams.width = maxGraphWidth.toInt()
                binding.graphAngry.requestLayout()
                binding.tvCountAngry.text = count.toString()
            }
            5 -> {
                height = (graphUnit * count).toInt()
                binding.graphSad.layoutParams.height = height
                binding.graphSad.layoutParams.width = maxGraphWidth.toInt()
                binding.graphSad.requestLayout()
                binding.tvCountSad.text = count.toString()
            }
            6 -> {
                height = (graphUnit * count).toInt()
                binding.graphBored.layoutParams.height = height
                binding.graphBored.layoutParams.width = maxGraphWidth.toInt()
                binding.graphBored.requestLayout()
                binding.tvCountBored.text = count.toString()
            }
            7 -> {
                height = (graphUnit * count).toInt()
                binding.graphMemory.layoutParams.height = height
                binding.graphMemory.layoutParams.width = maxGraphWidth.toInt()
                binding.graphMemory.requestLayout()
                binding.tvCountMemory.text = count.toString()
            }
            8 -> {
                height = (graphUnit * count).toInt()
                binding.graphDaily.layoutParams.height = height
                binding.graphDaily.layoutParams.width = maxGraphWidth.toInt()
                binding.graphDaily.requestLayout()
                binding.tvCountDaily.text = count.toString()
            }
            else -> {
                Log.d("Stat-setEmotionGraph", "error")
            }
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
        when (depthId) {
            0 -> {
                height = (graphUnit * count).toInt()
                binding.graph2.layoutParams.height = height
                binding.graph2.layoutParams.width = maxGraphWidth.toInt()
                binding.graph2.requestLayout()
                binding.tvCount2.text = count.toString()

                if (height >= heightLimit)
                    binding.imgDepth2.visibility = View.VISIBLE
                else
                    binding.imgDepth2.visibility = View.GONE
            }
            1 -> {
                height = (graphUnit * count).toInt()
                binding.graph30.layoutParams.height = height
                binding.graph30.layoutParams.width = maxGraphWidth.toInt()
                binding.graph30.requestLayout()
                binding.tvCount30.text = count.toString()
                binding.imgDepth30.visibility = View.GONE

                if (height >= heightLimit)
                    binding.imgDepth30.visibility = View.VISIBLE
                else
                    binding.imgDepth30.visibility = View.GONE
            }
            2 -> {
                height = (graphUnit * count).toInt()
                binding.graph100.layoutParams.height = height
                binding.graph100.layoutParams.width = maxGraphWidth.toInt()
                binding.graph100.requestLayout()
                binding.tvCount100.text = count.toString()
                binding.imgDepth100.visibility = View.GONE

                if (height >= heightLimit)
                    binding.imgDepth100.visibility = View.VISIBLE
                else
                    binding.imgDepth100.visibility = View.GONE
            }
            3 -> {
                height = (graphUnit * count).toInt()
                binding.graph300.layoutParams.height = height
                binding.graph300.layoutParams.width = maxGraphWidth.toInt()
                binding.graph300.requestLayout()
                binding.tvCount300.text = count.toString()
                binding.imgDepth300.visibility = View.GONE

                if (height >= heightLimit)
                    binding.imgDepth300.visibility = View.VISIBLE
                else
                    binding.imgDepth300.visibility = View.GONE
            }
            4 -> {
                height = (graphUnit * count).toInt()
                binding.graph700.layoutParams.height = height
                binding.graph700.layoutParams.width = maxGraphWidth.toInt()
                binding.graph700.requestLayout()
                binding.tvCount700.text = count.toString()
                binding.imgDepth700.visibility = View.GONE

                if (height >= heightLimit)
                    binding.imgDepth700.visibility = View.VISIBLE
                else
                    binding.imgDepth700.visibility = View.GONE
            }
            5 -> {
                height = (graphUnit * count).toInt()
                binding.graph1005.layoutParams.height = height
                binding.graph1005.layoutParams.width = maxGraphWidth.toInt()
                binding.graph1005.requestLayout()
                binding.tvCount1005.text = count.toString()
                binding.imgDepth1005.visibility = View.GONE

                if (height >= heightLimit)
                    binding.imgDepth1005.visibility = View.VISIBLE
                else
                    binding.imgDepth1005.visibility = View.GONE
            }
            6 -> {
                height = (graphUnit * count).toInt()
                binding.graphUnder.layoutParams.height = height
                binding.graphUnder.layoutParams.width = maxGraphWidth.toInt()
                binding.graphUnder.requestLayout()
                binding.tvCountUnder.text = count.toString()
                binding.imgDepthUnder.visibility = View.GONE

                if (height >= heightLimit)
                    binding.imgDepthUnder.visibility = View.VISIBLE
                else
                    binding.imgDepthUnder.visibility = View.GONE
            }
            else -> {
                Log.d("Stat-setDepthGraph", "error")
            }
        }
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
}