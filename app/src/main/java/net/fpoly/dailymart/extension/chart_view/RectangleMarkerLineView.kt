package net.fpoly.dailymart.extension.chart_view

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import net.fpoly.dailymart.R
import net.fpoly.dailymart.utils.round

@SuppressLint("ViewConstructor")
class RectangleMarkerLineView(
    private val mContext: Context,
    layoutResource: Int,
    private val isShowDot: Boolean,
    private var round: String = "#,#",
) : MarkerView(
    mContext, layoutResource
) {
    private val lnlMarkerRectangleTextLayout: LinearLayout =
        findViewById(R.id.layout_marker_rectangle_text)
    private val txvMakerRectangleValue: TextView = findViewById(R.id.tv_marker_rectangle_value)
    private val imvMarkerRectangleDot: ImageView = findViewById(R.id.imv_marker_rectangle_dot)
    private var xOffsetMultiplier = 0f
    private var maxX = 0

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry, highlight: Highlight) {
        if (e.x < 2) {
            xOffsetMultiplier = 7.3f
            lnlMarkerRectangleTextLayout.setBackgroundResource(R.drawable.bg_marker_rectangle_left)
            val layoutParams = imvMarkerRectangleDot.layoutParams as LinearLayout.LayoutParams
            layoutParams.gravity = Gravity.START
            imvMarkerRectangleDot.layoutParams = layoutParams
        } else if (e.x > maxX - 2) {
            xOffsetMultiplier = 1.12f
            lnlMarkerRectangleTextLayout.setBackgroundResource(R.drawable.bg_marker_rectangle_right)
            val layoutParams = imvMarkerRectangleDot.layoutParams as LinearLayout.LayoutParams
            layoutParams.gravity = Gravity.END
            imvMarkerRectangleDot.layoutParams = layoutParams
        } else {
            xOffsetMultiplier = 2f
            lnlMarkerRectangleTextLayout.setBackgroundResource(R.drawable.bg_marker_rectangle_center)
            val layoutParams = imvMarkerRectangleDot.layoutParams as LinearLayout.LayoutParams
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL
            imvMarkerRectangleDot.layoutParams = layoutParams
        }
        val markerData = e.data as MarkerDataModel
        txvMakerRectangleValue.text = e.y.round(round) + " " + markerData.unit
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return if (isShowDot) {
            MPPointF(
                -(width / xOffsetMultiplier), (-height + 8f)
            )
        } else {
            MPPointF(-(width / xOffsetMultiplier), -height - 10f)
        }
    }

    fun setRound(round: String) {
        this.round = round
    }

    fun setMaxX(maxX: Int) {
        this.maxX = maxX
    }

    fun setDotColor(dotColorResource: Int) {
        imvMarkerRectangleDot.setBackgroundResource(dotColorResource)
    }

    init {
        if (isShowDot) {
            imvMarkerRectangleDot.visibility = VISIBLE
        } else {
            imvMarkerRectangleDot.visibility = GONE
        }
    }
}