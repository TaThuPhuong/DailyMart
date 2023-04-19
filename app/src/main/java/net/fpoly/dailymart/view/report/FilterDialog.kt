package net.fpoly.dailymart.view.report

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogFilterReportBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible
import java.util.*

class FilterDialog(
    private val mContext: Context,
    private var mTypeFilter: TypeFilter,
    private var mTime: Long,
    private val onChose: (type: TypeFilter, time: Long) -> Unit,
) :
    BaseBottomDialog<DialogFilterReportBinding>(mContext, DialogFilterReportBinding::inflate) {

    private val calendar = Calendar.getInstance()
    private var maxDayInMonths = 0

    override fun initData() {
        calendar.timeInMillis = mTime
        initNumPicker()
        binding.tvDate.setOnClickListener {
            onSelectDay()
        }
        binding.tvMonth.setOnClickListener {
            onSelectMonth()
        }
        binding.tvYear.setOnClickListener {
            onSelectYear()
        }
        binding.btnFilter.setOnClickListener {
            calendar.set(
                binding.pickerYear.value,
                binding.pickerMonth.value - 1,
                binding.pickerDate.value
            )
            onChose(mTypeFilter, calendar.timeInMillis)
            dismiss()
        }
        binding.imvClose.setOnClickListener {
            dismiss()
        }
    }

    private fun getMaxDayInMonths(month: Int, year: Int): Int {
        return if (month == 2) {
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        } else if (month == 4 || month == 6 || month == 9 || month == 11) 30 else 31
    }

    private fun initNumPicker() {
        maxDayInMonths =
            getMaxDayInMonths(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))
        binding.pickerDate.minValue = 1
        binding.pickerDate.maxValue = maxDayInMonths
        binding.pickerDate.value = calendar.get(Calendar.DAY_OF_MONTH)
        binding.pickerMonth.minValue = 1
        binding.pickerMonth.maxValue = 12
        binding.pickerMonth.value = calendar.get(Calendar.MONTH) + 1
        binding.pickerYear.minValue = 2020
        binding.pickerYear.maxValue = 2100
        binding.pickerYear.value = calendar.get(Calendar.YEAR)

        binding.pickerDate.wrapSelectorWheel = true
        binding.pickerMonth.wrapSelectorWheel = true
        binding.pickerYear.wrapSelectorWheel = true

        setFilter(mTypeFilter)

        binding.pickerMonth.setOnValueChangedListener { _, _, newVal ->
            maxDayInMonths = getMaxDayInMonths(newVal, binding.pickerYear.value)
            binding.pickerDate.maxValue = maxDayInMonths
        }
        binding.pickerYear.setOnValueChangedListener { _, _, newVal ->
            maxDayInMonths = getMaxDayInMonths(binding.pickerMonth.value, newVal)
            binding.pickerDate.maxValue = maxDayInMonths
        }
    }

    private fun setFilter(type: TypeFilter) {
        when (type) {
            TypeFilter.DAY -> onSelectDay()
            TypeFilter.MONTH -> onSelectMonth()
            TypeFilter.YEAR -> onSelectYear()
        }
    }

    private fun onSelectDay() {
        mTypeFilter = TypeFilter.DAY
        binding.tvDate.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        binding.tvMonth.setTextColor(ContextCompat.getColor(mContext, R.color.gray_medium))
        binding.tvYear.setTextColor(ContextCompat.getColor(mContext, R.color.gray_medium))
        binding.tvDate.setBackgroundResource(R.drawable.bg_item_filter_pink_rd_20)
        binding.tvYear.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
        binding.tvMonth.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
        binding.pickerDate.visible()
        binding.pickerMonth.visible()
    }

    private fun onSelectMonth() {
        mTypeFilter = TypeFilter.MONTH
        binding.tvDate.setTextColor(ContextCompat.getColor(mContext, R.color.gray_medium))
        binding.tvMonth.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        binding.tvYear.setTextColor(ContextCompat.getColor(mContext, R.color.gray_medium))
        binding.tvDate.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
        binding.tvYear.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
        binding.tvMonth.setBackgroundResource(R.drawable.bg_item_filter_pink_rd_20)
        binding.pickerDate.gone()
        binding.pickerMonth.visible()
        binding.pickerDate.value = 1
    }

    private fun onSelectYear() {
        mTypeFilter = TypeFilter.YEAR
        binding.tvDate.setTextColor(ContextCompat.getColor(mContext, R.color.gray_medium))
        binding.tvMonth.setTextColor(ContextCompat.getColor(mContext, R.color.gray_medium))
        binding.tvYear.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        binding.tvDate.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
        binding.tvYear.setBackgroundResource(R.drawable.bg_item_filter_pink_rd_20)
        binding.tvMonth.setBackgroundResource(R.drawable.bg_item_filter_gray_rd_20)
        binding.pickerDate.gone()
        binding.pickerMonth.gone()
        binding.pickerDate.value = 1
        binding.pickerMonth.value = 1
    }
}
