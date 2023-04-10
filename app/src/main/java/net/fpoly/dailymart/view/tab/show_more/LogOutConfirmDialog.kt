package net.fpoly.dailymart.view.tab.show_more

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogLogOutConfirmBinding

class LogOutConfirmDialog(private val mContext: Context, val onConfirm: () -> Unit) :
    BaseBottomDialog<DialogLogOutConfirmBinding>(mContext, DialogLogOutConfirmBinding::inflate) {

    override fun initData() {
        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvCancel.setOnClickListener { dismiss() }
        binding.tvConfirm.setOnClickListener {
            onConfirm()
            dismiss()
        }
    }
}