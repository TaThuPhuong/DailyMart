package net.fpoly.dailymart.view.report

import android.graphics.Color
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.listener.BarLineChartTouchListener
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.ReportDataByDay
import net.fpoly.dailymart.data.model.ReportDataByDayInMonth
import net.fpoly.dailymart.data.model.ReportDataByMonthInYear
import net.fpoly.dailymart.data.model.ReportPrice
import net.fpoly.dailymart.databinding.ActivityReportBinding
import net.fpoly.dailymart.extension.CustomBarChartRender
import net.fpoly.dailymart.extension.CustomMarkerChartView
import net.fpoly.dailymart.extention.CheckTimeUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportActivity :
    BaseActivity<ActivityReportBinding>(ActivityReportBinding::inflate),
    View.OnClickListener {

    private var TAG = "YingMing"
    private val viewModel: ReportViewModel by viewModels { AppViewModelFactory }
    private var mYear = 0
    private var mMonth = 0

    private var mWeek = 0
    private var mDayOfMonth = 0
    private var typeChart = ""
    private val formatter = SimpleDateFormat("dd/MM/yyyy")
    private val calendarToday = Calendar.getInstance()

    private var mTypeFilter = TypeFilter.DAY
    private var mTime = Calendar.getInstance().timeInMillis

    private var mLoadingDialog: LoadingDialog? = null

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mLoadingDialog = LoadingDialog(this)
        mLoadingDialog?.showLoading()
        initCharView()
    }

    override fun setupObserver() {
        viewModel.onGetDone.observe(this) {
            if (it) mLoadingDialog?.hideLoading()
        }
    }

    override fun setOnClickListener() {
        super.setOnClickListener()
        binding.imvBack.setOnClickListener(this)
        binding.tvFilter.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.imvBack -> finish()
            binding.tvFilter -> {
                FilterDialog(this, mTypeFilter, mTime) { type, time ->
                    Log.e(TAG, "type:$type ")
                    Log.e(TAG, "date:${formatter.format(time)} ")
                    mTime = time
                    mTypeFilter = type
                    viewModel.onGetDone.postValue(false)
                    mLoadingDialog?.showLoading()
                    when (type) {
                        TypeFilter.DAY -> viewModel.getRevenueByDate(time)
                        TypeFilter.MONTH -> viewModel.getRevenueByMonth(time)
                        TypeFilter.YEAR -> viewModel.getRevenueByYear(time)
                    }
                }.show()
            }
        }
    }

    private fun initCharView() {
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
        binding.barChart.setNoDataText("Đang tải dữ liệu")
        val p = binding.barChart.getPaint(Chart.PAINT_INFO)
        p.textSize = 30f
        p.color = Color.parseColor("#FF6E6A")
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
        viewModel.listRevenueByMonth.observe(this) {
            setUpMonthlyChart(calendarToday[Calendar.MONTH], calendarToday[Calendar.YEAR], it)
        }
    }

    private fun setUpMonthlyChart(
        month: Int,
        year: Int,
        listReport: List<ReportDataByDayInMonth?>,
    ) {
        Log.d(TAG, "setUpMonthlyChart: $listReport")

        (binding.barChart.onTouchListener as BarLineChartTouchListener).stopDeceleration()
        mMonth = month
        mYear = year
        Log.d(TAG, "setUpMonthlyChart: $mMonth")
        typeChart = "MONTHLY"
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
            for (doanhThu in listReport) {
                if (formatter.format(doanhThu!!.date) == day) {
                    valueMoney += doanhThu!!.data
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
        data.barWidth = 0.3f
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        })
        data.setValueTextSize(9f)
        data.setValueTextColor(ContextCompat.getColor(this, R.color.transparent))
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

    private fun setUpYearlyChart(year: Int, listReport: List<ReportDataByMonthInYear?>) {
        Log.d(TAG, "setUpYearlyChart: $listReport")

        (binding.barChart.onTouchListener as BarLineChartTouchListener).stopDeceleration()
        mYear = year
        typeChart = "YEARLY"
        val valuesPushUp = java.util.ArrayList<BarEntry>()
        var maxMoney: Long = 0
        val monthOfYearList = arrayOfNulls<String>(12)
        val valueMonthOfYearList = LongArray(12)
        for (i in 0..11) {
            val month = CheckTimeUtils.mDecimalFormat.format(i + 1).toString()
            var valueMoney: Long = 0
            for (doanhThu in listReport) {
                if (formatter.format(doanhThu!!.month) == month) {
                    valueMoney += doanhThu!!.data
                }
            }
            monthOfYearList[i] = SimpleDateFormat("MMMM").format(
                Calendar.getInstance().apply { set(Calendar.MONTH, i) }.time
            )
            valueMonthOfYearList[i] = valueMoney
        }
        for (i in monthOfYearList.indices) {
            valuesPushUp.add(BarEntry(i.toFloat(), valueMonthOfYearList[i].toFloat()))
            if (valueMonthOfYearList[i] > maxMoney) {
                maxMoney = valueMonthOfYearList[i]
            }
        }
        if (maxMoney < 20) {
            maxMoney = 20
        }
        binding.barChart.axisLeft.axisMaximum = maxMoney + 1000000f
        val xAxis = binding.barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(monthOfYearList)
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
        binding.barChart.setVisibleXRangeMaximum(4f)
        binding.barChart.isHorizontalScrollBarEnabled = true
        binding.barChart.setTouchEnabled(true)
        binding.barChart.isDragEnabled = true
        val marker: IMarker = CustomMarkerChartView(this, R.layout.market_chart_view)
        binding.barChart.marker = marker
        binding.barChart.setDrawMarkerViews(true)
        binding.barChart.moveViewToX(0f)
        binding.barChart.invalidate()
    }
}

enum class TypeFilter {
    DAY, MONTH, YEAR
}
