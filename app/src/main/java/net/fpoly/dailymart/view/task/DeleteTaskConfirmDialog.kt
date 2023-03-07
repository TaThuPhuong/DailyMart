package net.fpoly.dailymart.view.task

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogDeleteTaskConfirmBinding

class DeleteTaskConfirmDialog(private val mContext: Context, private val onConfirm: () -> Unit) :
    BaseBottomDialog<DialogDeleteTaskConfirmBinding>(
        mContext,
        DialogDeleteTaskConfirmBinding::inflate
    ) {

    override fun initData() {
        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvCancel.setOnClickListener { dismiss() }
        binding.tvConfirm.setOnClickListener {
            onConfirm()
            dismiss()
        }
    }
}