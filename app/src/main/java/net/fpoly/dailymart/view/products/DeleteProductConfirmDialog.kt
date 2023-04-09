package net.fpoly.dailymart.view.products

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogDeleteProductConfirmBinding
import net.fpoly.dailymart.databinding.DialogDeleteTaskConfirmBinding

class DeleteProductConfirmDialog(private val mContext: Context, private val onConfirm: () -> Unit) :
    BaseBottomDialog<DialogDeleteProductConfirmBinding>(
        mContext,
        DialogDeleteProductConfirmBinding::inflate
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