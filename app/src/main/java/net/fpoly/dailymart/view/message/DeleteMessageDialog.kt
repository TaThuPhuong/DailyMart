package net.fpoly.dailymart.view.message

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogConfirmDeleteMessageBinding

class DeleteMessageDialog(private val mContext: Context, private val onConfirm: () -> Unit) :
    BaseBottomDialog<DialogConfirmDeleteMessageBinding>(
        mContext,
        DialogConfirmDeleteMessageBinding::inflate
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