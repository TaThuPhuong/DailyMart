package net.fpoly.dailymart.view.profile

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogChangeDisableBinding

class ChangeDisableDialog(
    val mContext: Context,
    var mStatus: Boolean,
    val onSelected: (Boolean) -> Unit,
) : BaseBottomDialog<DialogChangeDisableBinding>(mContext, DialogChangeDisableBinding::inflate) {


    override fun initData() {
        if (mStatus) {
            setSelected(binding.tvAble)
            setUnSelected(binding.tvDisable)
        } else {
            setSelected(binding.tvDisable)
            setUnSelected(binding.tvAble)
        }
        binding.imvClose.setOnClickListener { dismiss() }

        binding.tvAble.setOnClickListener {
            setSelected(binding.tvAble)
            setUnSelected(binding.tvDisable)
            mStatus = true
        }
        binding.tvDisable.setOnClickListener {
            setSelected(binding.tvDisable)
            setUnSelected(binding.tvAble)
            mStatus = false
        }
        binding.tvSelect.setOnClickListener {
            onSelected(mStatus)
            dismiss()
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