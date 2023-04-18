package net.fpoly.dailymart.extension.chart_view

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import net.fpoly.dailymart.R
import net.fpoly.dailymart.databinding.MarkerRectangleLayoutBinding

class RectangleMarkerView(
    private val mContext: Context,
    layoutResource: Int,
    private val isShowDot: Boolean,
    private var round: String = "#.#",
) : MarkerView(
    mContext, layoutResource
) {
    private val binding: MarkerRectangleLayoutBinding = MarkerRectangleLayoutBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var xOffsetMultiplier = 0f
    private var maxX = 0

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry, highlight: Highlight) {
        if (e.x < 2) {
            xOffsetMultiplier = 7.3f
            binding.layoutMarkerRectangleText.setBackgroundResource(R.drawable.bg_marker_rectangle_left)
            binding.imvMarkerRectangleDot.layoutParams =
                (binding.imvMarkerRectangleDot.layoutParams as LinearLayout.LayoutParams).apply {
                    gravity = Gravity.START
                }
        } else if (e.x > maxX - 2) {
            xOffsetMultiplier = 1.12f
            binding.layoutMarkerRectangleText.setBackgroundResource(R.drawable.bg_marker_rectangle_right)
            binding.imvMarkerRectangleDot.layoutParams =
                (binding.imvMarkerRectangleDot.layoutParams as LinearLayout.LayoutParams).apply {
                    gravity = Gravity.END
                }
        } else {
            xOffsetMultiplier = 2f
            binding.layoutMarkerRectangleText.setBackgroundResource(R.drawable.bg_marker_rectangle_center)
            binding.imvMarkerRectangleDot.layoutParams =
                (binding.imvMarkerRectangleDot.layoutParams as LinearLayout.LayoutParams).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
        }
        val markerData = e.data as MarkerDataModel
        binding.tvMarkerRectangleValue.text = markerData.value
        binding.tvMarkerRectangleTime.text = markerData.timer

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return if (isShowDot) {
            MPPointF(
                -(width / xOffsetMultiplier), (-height + mContext.resources
                    .getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp)).toFloat()
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
        binding.imvMarkerRectangleDot.setBackgroundResource(dotColorResource)
    }

    init {
        if (isShowDot) {
            binding.imvMarkerRectangleDot.visibility = VISIBLE
        } else {
            binding.imvMarkerRectangleDot.visibility = GONE
        }
    }
}