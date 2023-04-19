package net.fpoly.dailymart.view.report

import android.graphics.Typeface
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.ReportDayData
import net.fpoly.dailymart.data.model.ReportMonthData
import net.fpoly.dailymart.databinding.ActivityReportBinding
import net.fpoly.dailymart.extension.chart_view.MarkerDataModel
import net.fpoly.dailymart.extension.chart_view.RectangleMarkerView
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.utils.ChartUtils
import net.fpoly.dailymart.utils.round
import net.fpoly.dailymart.utils.toMoney
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max

class ReportActivity :
    BaseActivity<ActivityReportBinding>(ActivityReportBinding::inflate),
    View.OnClickListener {

    private var TAG = "YingMing"
    private val viewModel: ReportViewModel by viewModels { AppViewModelFactory }
    private val calendarToday = Calendar.getInstance()
    private var mTypeFilter = TypeFilter.MONTH
    private var mTime = Calendar.getInstance().timeInMillis

    private var mLoadingDialog: LoadingDialog? = null
    private var mListMonthData: List<ReportDayData> = ArrayList()
    private var mListYearData: List<ReportMonthData> = ArrayList()

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mLoadingDialog = LoadingDialog(this)
        mLoadingDialog?.showLoading()
    }

    override fun setupObserver() {
        viewModel.onGetDone.observe(this) {
            if (it) mLoadingDialog?.hideLoading()
        }
        viewModel.listRevenueByMonth.observe(this) {
            ChartUtils.resetLineChart(binding.lcHonHop)
            if (it.isNotEmpty()) {
                mListMonthData = it
                configBarChart(
                    binding.bcExport,
                    it.size,
                    it.size,
                    TypeChart.EXPORT,
                    TypeFilter.MONTH
                )
                configBarChart(
                    binding.bcImport,
                    it.size,
                    it.size,
                    TypeChart.IMPORT,
                    TypeFilter.MONTH
                )
                configLineChart(binding.lcHonHop, TypeFilter.MONTH, it.size)
            }
        }
        viewModel.listRevenueByYear.observe(this) {
            ChartUtils.resetLineChart(binding.lcHonHop)
            if (it.isNotEmpty()) {
                mListYearData = it
                configBarChart(
                    binding.bcExport,
                    it.size,
                    it.size,
                    TypeChart.EXPORT,
                    TypeFilter.YEAR
                )
                configBarChart(
                    binding.bcImport,
                    it.size,
                    it.size,
                    TypeChart.IMPORT,
                    TypeFilter.YEAR
                )
                configLineChart(binding.lcHonHop, TypeFilter.YEAR, it.size)
            }
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
                    setVisibleChart(type != TypeFilter.DAY, type)
                    mTime = time
                    mTypeFilter = type
                    viewModel.onGetDone.postValue(false)
                    mLoadingDialog?.showLoading()
                    when (type) {
                        TypeFilter.DAY -> {
                            viewModel.getRevenueByDate(time)
                        }
                        TypeFilter.MONTH -> viewModel.getRevenueByMonth(time)
                        TypeFilter.YEAR -> viewModel.getRevenueByYear(time)
                    }
                }.show()
            }
        }
    }

    private fun setVisibleChart(b: Boolean, typeFilter: TypeFilter) {
        when (typeFilter) {
            TypeFilter.DAY -> {}
            TypeFilter.MONTH -> {
                binding.tvChartExportBtUnit.text = "Ngày"
                binding.tvImportBtUnit.text = "Ngày"
                binding.tvLcBtUnit.text = "Ngày"
            }
            TypeFilter.YEAR -> {
                binding.tvChartExportBtUnit.text = "Tháng"
                binding.tvImportBtUnit.text = "Tháng"
                binding.tvLcBtUnit.text = "Tháng"
            }
        }
        binding.tvChartExport.setVisibility(b)
        binding.tvChartImport.setVisibility(b)
        binding.tvHonHop.setVisibility(b)
        binding.layoutChartExport.setVisibility(b)
        binding.layoutBarchartImport.setVisibility(b)
        binding.layoutLineChart.setVisibility(b)
    }

    private fun configBarChart(
        barchart: BarChart,
        maxY: Int,
        maxX: Int,
        typeChart: TypeChart,
        typeFilter: TypeFilter
    ) {
        ChartUtils.setConfigChart(this, barchart)
        ChartUtils.setConfigXAxis(this, barchart.xAxis, maxY)
        ChartUtils.setConfigYAxis(this, barchart.axisLeft)
        (barchart.marker as RectangleMarkerView).setMaxX(maxX - 1)

        val maxMoney = if (typeChart == TypeChart.EXPORT) {
            abs(mListMonthData.maxOf { it.data.tienBan })
        } else {
            abs(mListMonthData.maxOf { it.data.tienNhap })
        }
        barchart.extraRightOffset = 10f
        barchart.xAxis.axisMaximum = maxX.toFloat()
        barchart.xAxis.axisMinimum = 1f
        barchart.xAxis.labelCount = maxX
        barchart.axisLeft.axisMaximum = maxMoney * 1.2f
        barchart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value > maxX) {
                    ""
                } else {
                    value.round()
                }
            }
        }
        val data = when (typeFilter) {
            TypeFilter.DAY -> return
            TypeFilter.MONTH -> getDataEntriesMonth(mListMonthData, typeChart)
            TypeFilter.YEAR -> getDataEntriesYear(mListYearData, typeChart)
        }
        if (typeFilter == TypeFilter.MONTH) {
            data.barWidth = 0.75f
            barchart.setVisibleXRangeMaximum(12f)
            if (barchart.data != null) {
                barchart.data.clearValues()
            }
            barchart.notifyDataSetChanged()
            barchart.clear()
            barchart.data = data
            barchart.animateY(500, Easing.Linear)
            barchart.moveViewToX((maxX / 2).toFloat())
        } else {
            data.barWidth = 0.3f
            barchart.setVisibleXRangeMaximum(12f)
            if (barchart.data != null) {
                barchart.data.clearValues()
            }
            barchart.notifyDataSetChanged()
            barchart.clear()
            barchart.data = data
            barchart.animateY(500, Easing.Linear)
            for (i in mListYearData.indices) {
                if (mListYearData[i].data.tienBan != 0L || mListYearData[i].data.tienNhap != 0L) {
                    barchart.moveViewToX(i.toFloat())
                    break
                }
            }
        }
    }

    private fun getDataEntriesMonth(list: List<ReportDayData>, type: TypeChart): BarData {
        val barData: MutableList<BarEntry> = ArrayList()
        for (i in list.indices) {
            when (type) {
                TypeChart.IMPORT -> {
                    val entry = BarEntry((i + 1).toFloat(), list[i].data.tienNhap.toFloat())
                    entry.data =
                        MarkerDataModel(list[i].data.tienNhap.toMoney(), list[i].date.date2String())
                    barData.add(entry)
                }
                TypeChart.EXPORT -> {
                    val entry = BarEntry((i + 1).toFloat(), list[i].data.tienBan.toFloat())
                    entry.data =
                        MarkerDataModel(list[i].data.tienBan.toMoney(), list[i].date.date2String())
                    barData.add(entry)
                }
            }
        }
        val barDataSet = ChartUtils.setConfigBarChartDataSet(
            this,
            barData,
            "",
            R.color.pink_primary,
            R.color.pink_light,
            R.color.red_FF444C
        )
        return BarData(barDataSet)
    }

    private fun getDataEntriesYear(list: List<ReportMonthData>, type: TypeChart): BarData {
        val barData: MutableList<BarEntry> = ArrayList()
        for (i in list.indices) {
            when (type) {
                TypeChart.IMPORT -> {
                    val entry = BarEntry((i + 1).toFloat(), list[i].data.tienNhap.toFloat())
                    entry.data =
                        MarkerDataModel(list[i].data.tienNhap.toMoney(), "Tháng ${list[i].month}")
                    barData.add(entry)
                }
                TypeChart.EXPORT -> {
                    val entry = BarEntry((i + 1).toFloat(), list[i].data.tienBan.toFloat())
                    entry.data =
                        MarkerDataModel(list[i].data.tienBan.toMoney(), "Tháng ${list[i].month}")
                    barData.add(entry)
                }
            }
        }
        val barDataSet = ChartUtils.setConfigBarChartDataSet(
            this,
            barData,
            "",
            R.color.pink_primary,
            R.color.pink_light,
            R.color.red_FF444C
        )
        return BarData(barDataSet)
    }

    private fun configLineChart(lineChart: LineChart, type: TypeFilter, maxLabel: Int) {
        ChartUtils.setConfigLineChart(this, R.drawable.bg_marker_circle_blue, lineChart)
        ChartUtils.setConfigXAxiLineChart(this, lineChart.xAxis, maxLabel)
        ChartUtils.setConfigYAxisLineChart(this, lineChart.axisLeft)
        val maxMoney = if (type == TypeFilter.MONTH) {
            max(mListMonthData.maxOf { it.data.tienBan }, mListMonthData.maxOf { it.data.tienBan })
        } else {
            max(mListYearData.maxOf { it.data.tienBan }, mListYearData.maxOf { it.data.tienBan })
        }
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.axisMaximum = maxMoney * 1.2f
        lineChart.xAxis.axisMaximum = maxLabel - 1f
        lineChart.xAxis.labelCount = maxLabel - 1
        val data = when (type) {
            TypeFilter.DAY -> return
            TypeFilter.MONTH -> getLineDataMonth()
            TypeFilter.YEAR -> getLineDataYear()
        }
        lineChart.setMaxVisibleValueCount(12)
        lineChart.setVisibleXRangeMaximum(10f)
        lineChart.data = data
    }

    private fun getLineDataMonth(): LineData {
        val entryDataImport: MutableList<Entry> = ArrayList()
        val entryDataExport: MutableList<Entry> = ArrayList()
        val listLineData: List<ILineDataSet>
        for (i in mListMonthData.indices) {
            val entryImport = Entry((i + 1).toFloat(), mListMonthData[i].data.tienNhap.toFloat())
            entryImport.data = MarkerDataModel(
                "Nhập " + mListMonthData[i].data.tienNhap.toMoney(),
                mListMonthData[i].date.date2String()
            )
            val entryExport = BarEntry((i + 1).toFloat(), mListMonthData[i].data.tienBan.toFloat())
            entryExport.data =
                MarkerDataModel(
                    "Bán " + mListMonthData[i].data.tienBan.toMoney(),
                    mListMonthData[i].date.date2String()
                )
            entryDataImport.add(entryImport)
            entryDataExport.add(entryExport)
        }
        val lineDataSetImport = ChartUtils.setConfigLineDataSet(
            this,
            entryDataImport,
            R.color.red_FF444C,
            R.color.black,
            R.color.red_FF444C
        )
        val lineDataSetExport =
            ChartUtils.setConfigLineDataSet(
                this,
                entryDataExport,
                R.color.blue_04,
                R.color.start_text,
                R.color.blue_04
            )
        listLineData = ArrayList()
        listLineData.add(lineDataSetImport)
        listLineData.add(lineDataSetExport)
        return LineData(listLineData)
    }

    private fun getLineDataYear(): LineData {
        val entryDataImport: MutableList<Entry> = ArrayList()
        val entryDataExport: MutableList<Entry> = ArrayList()
        val listLineData: List<ILineDataSet>
        for (i in mListYearData.indices) {
            val entryImport = Entry((i + 1).toFloat(), mListYearData[i].data.tienNhap.toFloat())
            entryImport.data = MarkerDataModel(
                "Nhập " + mListYearData[i].data.tienNhap.toMoney(),
                "Tháng ${mListYearData[i].month}"
            )
            val entryExport = BarEntry((i + 1).toFloat(), mListYearData[i].data.tienBan.toFloat())
            entryExport.data =
                MarkerDataModel(
                    "Bán " + mListYearData[i].data.tienBan.toMoney(),
                    "Tháng ${mListYearData[i].month}"
                )
            entryDataImport.add(entryImport)
            entryDataExport.add(entryExport)
        }
        val lineDataSetImport = ChartUtils.setConfigLineDataSet(
            this,
            entryDataImport,
            R.color.red_FF444C,
            R.color.black,
            R.color.red_FF444C
        )
        val lineDataSetExport =
            ChartUtils.setConfigLineDataSet(
                this,
                entryDataExport,
                R.color.blue_04,
                R.color.start_text,
                R.color.blue_04
            )

        listLineData = ArrayList()
        listLineData.add(lineDataSetImport)
        listLineData.add(lineDataSetExport)
        return LineData(listLineData)
    }

}

enum class TypeFilter {
    DAY, MONTH, YEAR
}

enum class TypeChart {
    IMPORT, EXPORT
}
