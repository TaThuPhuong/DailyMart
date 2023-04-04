package net.fpoly.dailymart.view.profile

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogChangeRoleBinding
import net.fpoly.dailymart.utils.ROLE

class ChangeRoleDialog(val mContext: Context, var mRole: ROLE, val onSelected: (ROLE) -> Unit) :
    BaseBottomDialog<DialogChangeRoleBinding>(mContext, DialogChangeRoleBinding::inflate) {


    override fun initData() {
        if (mRole == ROLE.staff) {
            setSelected(binding.tvStaff)
            setUnSelected(binding.tvManage)
        } else {
            setSelected(binding.tvManage)
            setUnSelected(binding.tvStaff)
        }
        binding.imvClose.setOnClickListener { dismiss() }

        binding.tvStaff.setOnClickListener {
            setSelected(binding.tvStaff)
            setUnSelected(binding.tvManage)
            mRole = ROLE.staff
        }
        binding.tvManage.setOnClickListener {
            setSelected(binding.tvManage)
            setUnSelected(binding.tvStaff)
            mRole = ROLE.manager
        }
        binding.tvSelect.setOnClickListener {
            onSelected(mRole)
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