package net.fpoly.dailymart.view.check_date

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogCheckDateFilterBinding
import net.fpoly.dailymart.utils.CheckDateFilter

class FilterCheckDateDialog(
    val mContext: Context,
    private var mFilter: CheckDateFilter,
    val onSelected: (CheckDateFilter) -> Unit,
) :
    BaseBottomDialog<DialogCheckDateFilterBinding>(
        mContext,
        DialogCheckDateFilterBinding::inflate
    ) {
    override fun initData() {
        setCurrent(mFilter)
        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvOutOfDate.setOnClickListener {
            setSelected(binding.tvOutOfDate)
            setUnSelected(binding.tv7)
            setUnSelected(binding.tvCategory)
            mFilter = CheckDateFilter.SOON
        }
        binding.tv7.setOnClickListener {
            setUnSelected(binding.tvOutOfDate)
            setSelected(binding.tv7)
            setUnSelected(binding.tvCategory)
            mFilter = CheckDateFilter.SEVEN_DAY
        }
        binding.tvCategory.setOnClickListener {
            setUnSelected(binding.tvOutOfDate)
            setUnSelected(binding.tv7)
            setSelected(binding.tvCategory)
            mFilter = CheckDateFilter.CATEGORY
        }
        binding.tvSelect.setOnClickListener {
            onSelected(mFilter)
            dismiss()
        }
    }

    private fun setCurrent(filter: CheckDateFilter) {
        when (filter) {
            CheckDateFilter.SOON -> {
                setSelected(binding.tvOutOfDate)
                setUnSelected(binding.tv7)
                setUnSelected(binding.tvCategory)
            }
            CheckDateFilter.SEVEN_DAY -> {
                setUnSelected(binding.tvOutOfDate)
                setSelected(binding.tv7)
                setUnSelected(binding.tvCategory)
            }
            CheckDateFilter.CATEGORY -> {
                setUnSelected(binding.tvOutOfDate)
                setUnSelected(binding.tv7)
                setSelected(binding.tvCategory)
            }
        }
    }

    private fun setSelected(tv: TextView) {
        tv.apply {
            setTextColor(ContextCompat.getColor(mContext, R.color.pink_primary))
            background = ContextCompat.getDrawable(mContext, R.drawable.bg_bd_pink_rd_58)
        }
    }

    private fun setUnSelected(tv: TextView) {
        tv.apply {
            setTextColor(ContextCompat.getColor(mContext, R.color.gray_medium))
            background = ContextCompat.getDrawable(mContext, R.drawable.bg_gray_light_10_rd_58)
        }
    }
}