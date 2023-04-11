package net.fpoly.dailymart.view.report

import android.content.Context
import android.util.Log
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogFilterReportBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible
import java.util.*

class FilterDialog(
    context: Context,
    private val viewModel: ReportViewModel,
) :
    BaseBottomDialog<DialogFilterReportBinding>(context, DialogFilterReportBinding::inflate) {
    override fun initData() {
        val TAG = "FilterDialog"
        val calendar = Calendar.getInstance()
        var filterTime = ""
        var maxDayInMonths =
            getMaxDayInMonths(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
        Log.d(TAG, "initData: max day: $maxDayInMonths")

        binding.pickerDate.minValue = 1
        binding.pickerDate.maxValue = maxDayInMonths
        binding.pickerDate.value = calendar.get(Calendar.DAY_OF_MONTH)
        binding.pickerMonth.minValue = 1
        binding.pickerMonth.maxValue = 12
        binding.pickerMonth.value = calendar.get(Calendar.MONTH) + 1
        binding.pickerYear.minValue = 2020
        binding.pickerYear.maxValue = 2100
        binding.pickerYear.value = calendar.get(Calendar.YEAR)

        binding.pickerDate.wrapSelectorWheel = false
        binding.pickerMonth.wrapSelectorWheel = false
        binding.pickerYear.wrapSelectorWheel = false

        binding.pickerMonth.setOnValueChangedListener { _, _, newVal ->
            maxDayInMonths = getMaxDayInMonths(newVal, binding.pickerYear.value)
            binding.pickerDate.maxValue = maxDayInMonths
        }
        binding.pickerYear.setOnValueChangedListener { _, _, newVal ->
            maxDayInMonths = getMaxDayInMonths(binding.pickerMonth.value, newVal)
            binding.pickerDate.maxValue = maxDayInMonths
        }
        binding.tvDate.setOnClickListener {
            binding.tvDate.setBackgroundResource(R.drawable.bg_item_filter_pink_rd_20)
            binding.tvYear.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
            binding.tvMonth.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
            binding.pickerDate.visible()
            binding.pickerMonth.visible()
        }
        binding.tvMonth.setOnClickListener {
            binding.tvDate.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
            binding.tvYear.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
            binding.tvMonth.setBackgroundResource(R.drawable.bg_item_filter_pink_rd_20)
            binding.pickerDate.gone()
            binding.pickerMonth.visible()
            binding.pickerDate.value = 0
        }
        binding.tvYear.setOnClickListener {
            binding.tvDate.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
            binding.tvYear.setBackgroundResource(R.drawable.bg_item_filter_pink_rd_20)
            binding.tvMonth.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
            binding.pickerDate.gone()
            binding.pickerMonth.gone()
            binding.pickerDate.value = 0
            binding.pickerMonth.value = 0
        }
        binding.btnFilter.setOnClickListener {
            if (binding.pickerDate.value != 0 && binding.pickerMonth.value != 0 && binding.pickerYear.value != 0) {
                filterTime = "${binding.pickerDate.value} / ${binding.pickerMonth.value} / ${binding.pickerYear.value}"
                Log.d(TAG, "initData: filterTime: $filterTime")
            }
            if (binding.pickerDate.value == 0) {
                filterTime = "${binding.pickerMonth.value}"
                Log.d(TAG, "initData: filterTime: $filterTime")
            }
            if (binding.pickerDate.value == 0 && binding.pickerMonth.value == 0) {
                filterTime = "${binding.pickerYear.value}"
                Log.d(TAG, "initData: filterTime: $filterTime")
            }
        }
        binding.imvClose.setOnClickListener {
            dismiss()
        }
    }

    private fun getMaxDayInMonths(month: Int, year: Int): Int {
        if (month == 2) {
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                return 29; // năm nhuận
            } else {
                return 28; // năm không nhuận
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30; // các tháng có 30 ngày
        } else {
            return 31; // các tháng có 31 ngày
        }
    }
}
