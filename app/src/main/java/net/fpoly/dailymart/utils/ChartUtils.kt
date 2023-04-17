package net.fpoly.dailymart.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
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
        chart.setNoDataText("No data")
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

    fun setConfigXAxis(context: Context, xAxis: XAxis, size: Int, isMonth: Boolean = false) {

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = size
        xAxis.setDrawLabels(true)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)

        xAxis.textColor = ContextCompat.getColor(context, R.color.gray_light)
        xAxis.textSize = 9f
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true

        if (isMonth) {
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return convertNumber(value.toInt() + 1)
                }
            }
        } else {
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return convertNumber(value.toInt() + 1)
                }
            }
        }
    }

    fun setData(chart: BarChart, data: BarData, todayIndex: Int, barWidth: Float = 0.75f) {
        data.barWidth = barWidth
        if (chart.data != null) {
            chart.data.clearValues()
        }
        chart.notifyDataSetChanged()
        chart.clear()
        chart.data = data
        if (todayIndex != -1) {
            chart.highlightValue(todayIndex.toFloat(), 0, false)
        }
        chart.animateY(500, Easing.Linear)
        chart.setVisibleXRangeMaximum(12f)
        chart.moveViewToX(todayIndex.toFloat())
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

    private fun convertNumber(value: Int): String {
        return if (value < 10) {
            "0$value"
        } else value.toString()
    }
}