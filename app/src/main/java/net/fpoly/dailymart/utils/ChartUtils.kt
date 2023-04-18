package net.fpoly.dailymart.utils

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import net.fpoly.dailymart.R
import net.fpoly.dailymart.extension.chart_view.RectangleMarkerView
import net.fpoly.dailymart.extension.chart_view.RoundedBarChartRenderer

object ChartUtils {
    fun setConfigChart(context: Context, chart: BarChart, isRadius: Boolean = true) {
        chart.axisRight.isEnabled = false
        chart.setDrawBarShadow(false)
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setScaleEnabled(false)
        chart.setPinchZoom(false)
        chart.legend.setDrawInside(false)
        chart.legend.isEnabled = false
        chart.isHorizontalScrollBarEnabled = true
        chart.setNoDataText("Không có dữ liệu")
        chart.setDrawMarkers(true)
        val marker: IMarker = RectangleMarkerView(context, R.layout.marker_rectangle_layout, false)
        chart.marker = marker
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                chart.highlightValue(h)
            }

            override fun onNothingSelected() {}
        })
        if (isRadius) {
            val renderer = RoundedBarChartRenderer(chart, chart.animator, chart.viewPortHandler)
            renderer.setRadius(16)
            chart.renderer = renderer
        }

        setConfigYAxis(context, chart.axisLeft)
    }

    fun setConfigYAxis(context: Context, leftAxis: YAxis) {
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true
        leftAxis.gridColor = ContextCompat.getColor(context, R.color.blue_primary)
        leftAxis.gridLineWidth = 0.5f
        leftAxis.textSize = 9f
        leftAxis.textColor = ContextCompat.getColor(context, R.color.gray_light)
    }

    fun setConfigXAxis(context: Context, xAxis: XAxis, size: Int) {
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = size
        xAxis.setDrawLabels(true)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)

        xAxis.textColor = ContextCompat.getColor(context, R.color.gray_light)
        xAxis.textSize = 9f
        xAxis.axisMinimum = 1f
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

//        xAxis.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return convertNumber(value.toInt())
//            }
//        }
    }

    fun resetBarChart(chart: BarChart) {
        chart.clear()
        chart.barData?.clearValues()
        chart.data = null
        chart.data?.notifyDataChanged()
        chart.notifyDataSetChanged()
    }

    fun setConfigBarChartDataSet(
        context: Context, data: MutableList<BarEntry>, unit: String, color1: Int,
        color2: Int, highlightColor: Int,
    ): BarDataSet {
        val barDataSet = BarDataSet(data, unit)
        barDataSet.setGradientColor(
            ContextCompat.getColor(context, color1),
            ContextCompat.getColor(context, color2)
        )
        barDataSet.setDrawValues(false)
        barDataSet.isHighlightEnabled = true
        barDataSet.highLightColor = ContextCompat.getColor(context, highlightColor)
        barDataSet.highLightAlpha = 2

        return barDataSet
    }

    fun setConfigLineChart(context: Context, color: Int, chart: LineChart) {
        chart.invalidate()
        chart.moveViewToX(10f)
        chart.animateX(1000, Easing.EaseInSine)
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setScaleEnabled(false)
        chart.setPinchZoom(false)
        chart.legend.setDrawInside(false)
        chart.legend.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.isHorizontalScrollBarEnabled = true
        chart.setNoDataText("Không có dữ liệu")
        chart.setDrawMarkers(true)
        val reportMarker = RectangleMarkerView(context, R.layout.marker_rectangle_layout, true)
        reportMarker.setDotColor(color)
        chart.marker = reportMarker
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                chart.highlightValue(h)
            }

            override fun onNothingSelected() {}
        })
    }

    fun setConfigXAxiLineChart(context: Context, xAxis: XAxis, max: Int) {
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawLabels(true)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.isGranularityEnabled = true
        xAxis.textColor = ContextCompat.getColor(context, R.color.gray_medium)
        xAxis.textSize = 9f
        xAxis.axisMinimum = 1f
//        xAxis.labelCount = max + 1

        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }
    }

    fun setConfigYAxisLineChart(context: Context, leftAxis: YAxis) {
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true
        leftAxis.gridColor = ContextCompat.getColor(context, R.color.blue_primary)
        leftAxis.gridLineWidth = 0.5f
        leftAxis.setDrawLabels(true)
        leftAxis.textSize = 9f
        leftAxis.textColor = ContextCompat.getColor(context, R.color.gray_medium)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if (value < 0) {
                    ""
                } else {
                    value.round()
                }
            }
        }
    }

    fun setConfigLineDataSet(context: Context, data: MutableList<Entry>): LineDataSet {
        val lineDataSet = LineDataSet(data, "")

        lineDataSet.lineWidth = 4f
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(true)
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.circleRadius = 2f
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        lineDataSet.setDrawVerticalHighlightIndicator(false)
        lineDataSet.isHighlightEnabled = true
        lineDataSet.mode = LineDataSet.Mode.LINEAR
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillDrawable =
            ContextCompat.getDrawable(context, R.drawable.bg_line_chart_blue2)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        return lineDataSet
    }

    fun resetLineChart(chart: LineChart) {
        chart.clear()
        chart.lineData?.clearValues()
        chart.data = null
        chart.data?.notifyDataChanged()
        chart.notifyDataSetChanged()
    }

    private fun convertNumber(value: Int): String {
        return if (value < 10) {
            "0$value"
        } else value.toString()
    }
}