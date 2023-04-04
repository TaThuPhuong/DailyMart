package net.fpoly.dailymart.view.report

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.listener.BarLineChartTouchListener
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityReportBinding
import net.fpoly.dailymart.extention.CheckTimeUtils
import net.fpoly.dailymart.extention.CustomBarChartRender
import net.fpoly.dailymart.extention.CustomMarkerChartView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class DoanhThu(
    var soTien: Long = 0,
    var time: Long = 0,
)

class ReportActivity :
    BaseActivity<ActivityReportBinding>(ActivityReportBinding::inflate),
    View.OnClickListener {

    private var TAG = "ReportActivity"
    private val viewModel: ReportViewModel by viewModels { AppViewModelFactory }
    private var mYear = 0
    private var mMonth = 0

    private var mWeek = 0
    private var mDayOfMonth = 0
    private var typeChart = ""
    private val formatter = SimpleDateFormat("dd/MM/yyyy")

    private val listDoanhThu = ArrayList<DoanhThu>()

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        fakeData()
        initCharView(listDoanhThu)
    }

    override fun setupObserver() {
    }

    override fun setOnClickListener() {
        super.setOnClickListener()
        binding.imvBack.setOnClickListener(this)
        binding.imvReportViewLastTimeChart.setOnClickListener(this)
        binding.imvReportViewNextTimeChart.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.imvBack -> finish()
            binding.imvReportViewLastTimeChart -> {
                Log.d(TAG, "onClick: click")
                if (mMonth == 0) {
                    setUpMonthlyChart(11, mYear - 1, listDoanhThu)
                } else {
                    setUpMonthlyChart(mMonth - 1, mYear, listDoanhThu)
                }
            }
            binding.imvReportViewNextTimeChart -> {
                if (mMonth == 11) {
                    setUpMonthlyChart(0, mYear + 1, listDoanhThu)
                } else {
                    setUpMonthlyChart(mMonth + 1, mYear, listDoanhThu)
                }
            }
        }
    }

    private fun getMilliFromDate(dateFormat: String?): Long {
        var date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time
    }
    private fun fakeData() {
        listDoanhThu.add(DoanhThu(10000, getMilliFromDate("02/04/2023")))
        listDoanhThu.add(DoanhThu(20000, getMilliFromDate("01/04/2023")))
        listDoanhThu.add(DoanhThu(30000, getMilliFromDate("28/03/2023")))
        listDoanhThu.add(DoanhThu(70000, getMilliFromDate("27/03/2023")))
        listDoanhThu.add(DoanhThu(40000, getMilliFromDate("29/03/2023")))
    }

    private fun initCharView(listDoanhThu: ArrayList<DoanhThu>) {
        binding.barChart.setDrawBarShadow(false)
        binding.barChart.description.isEnabled = false
        binding.barChart.setDrawGridBackground(false)
        binding.barChart.setScaleEnabled(false)
        binding.barChart.setPinchZoom(false)
        binding.barChart.legend.setDrawInside(false)
        binding.barChart.legend.isEnabled = false
        binding.barChart.setFitBars(true)
        binding.barChart.animateXY(1000, 1000, Easing.EaseInOutExpo)
        binding.barChart.extraBottomOffset = 22f
        binding.barChart.setNoDataText("Loading Data...")
        val p = binding.barChart.getPaint(Chart.PAINT_INFO)
        p.textSize = 22f
        p.color = Color.parseColor("#7265E3")
        val xAxis = binding.barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = Color.parseColor("#B4B7CC")
        xAxis.yOffset = 20f
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawLabels(true)
        xAxis.setCenterAxisLabels(false)
        xAxis.isGranularityEnabled = true
        xAxis.axisLineColor = Color.parseColor("#D0D0D0")
        xAxis.setDrawAxisLine(false)
        val leftAxis = binding.barChart.axisLeft
        leftAxis.textColor = Color.parseColor("#B4B7CC")
        leftAxis.setLabelCount(4, false)
        leftAxis.xOffset = 16f
        leftAxis.spaceTop = 20f
        leftAxis.axisMinimum = 0f
        leftAxis.axisLineColor = Color.parseColor("#00D0D0D0")
        leftAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString() + ""
            }
        }
        val rightAxis = binding.barChart.axisRight
        rightAxis.isEnabled = false
        val calendarToday = Calendar.getInstance()
        setUpMonthlyChart(
            calendarToday[Calendar.MONTH],
            calendarToday[Calendar.YEAR],
            listDoanhThu,
        )
    }

    private fun setUpMonthlyChart(
        month: Int,
        year: Int,
        listDoanhThu: ArrayList<DoanhThu>,
    ) {
        (binding.barChart.onTouchListener as BarLineChartTouchListener).stopDeceleration()
//        tv_thong_ke.text = "Doanh thu tháng ${month + 1} năm $year"
        mMonth = month
        mYear = year
        typeChart = "MONTHLY"
        binding.txvReportViewTimeChart.text =
            CheckTimeUtils.mDecimalFormat.format(month + 1).toString() + "/" + year
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar[Calendar.MONTH] = month
        calendar[Calendar.YEAR] = year
        val numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        if (mDayOfMonth > numDaysInMonth) {
            mDayOfMonth = numDaysInMonth - 2
        }
        calendar[Calendar.DAY_OF_MONTH] = mDayOfMonth + 1
        mWeek = calendar[Calendar.WEEK_OF_YEAR]
        val valuesPushUp = java.util.ArrayList<BarEntry>()
        var maxMinute: Long = 0
        val dayOfMonthList = arrayOfNulls<String>(numDaysInMonth)
        val valueDayOfMonthList = LongArray(numDaysInMonth)
        for (i in 0 until numDaysInMonth) {
            val day = CheckTimeUtils.mDecimalFormat.format(i + 1)
                .toString() + "/" + CheckTimeUtils.mDecimalFormat.format(month + 1) + "/" + year
            var valueMoney: Long = 0
            for (doanhThu in listDoanhThu) {
                if (formatter.format(doanhThu.time) == day) {
                    valueMoney += doanhThu.soTien
                }
            }
            dayOfMonthList[i] = (i + 1).toString() + ""
            valueDayOfMonthList[i] = valueMoney
        }
        for (i in dayOfMonthList.indices.reversed()) {
            valuesPushUp.add(0, BarEntry(i.toFloat(), valueDayOfMonthList[i].toFloat()))
            if (valueDayOfMonthList[i] > maxMinute) {
                maxMinute = valueDayOfMonthList[i]
            }
        }
        if (maxMinute < 20) {
            maxMinute = 20
        }
        binding.barChart.axisLeft.axisMaximum = maxMinute + 100000f
        val xAxis = binding.barChart.xAxis
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value.toInt() >= dayOfMonthList.size) {
                    ""
                } else {
                    dayOfMonthList[value.toInt()]!!
                }
            }
        }
        val dataSet = BarDataSet(valuesPushUp, "")
        dataSet.color = Color.parseColor("#FF444C")
        dataSet.highLightAlpha = 0
        val data = BarData(dataSet)
        data.barWidth = 0.58f
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        })
        data.setValueTextSize(11f)
        data.setValueTextColor(resources.getColor(R.color.transparent))
        data.isHighlightEnabled = true
        val barChartRender = CustomBarChartRender(
            binding.barChart,
            binding.barChart.animator,
            binding.barChart.viewPortHandler,
        )
        barChartRender.setRadius(16)
        binding.barChart.renderer = barChartRender
        binding.barChart.fitScreen()
        if (binding.barChart.data != null) {
            binding.barChart.data.clearValues()
        }
        binding.barChart.notifyDataSetChanged()
        binding.barChart.clear()
        binding.barChart.data = data
        binding.barChart.setVisibleXRangeMaximum(7f)
        binding.barChart.isHorizontalScrollBarEnabled = true
        binding.barChart.setTouchEnabled(true)
        binding.barChart.isDragEnabled = true
        val marker: IMarker = CustomMarkerChartView(this, R.layout.market_chart_view)
        binding.barChart.marker = marker
        binding.barChart.setDrawMarkerViews(true)
        if (Calendar.getInstance()[Calendar.MONTH] == month) {
            binding.barChart.moveViewToX(Calendar.getInstance()[Calendar.DAY_OF_MONTH].toFloat())
        } else {
            binding.barChart.moveViewToX(0f)
        }
        binding.barChart.invalidate()
    }
}
