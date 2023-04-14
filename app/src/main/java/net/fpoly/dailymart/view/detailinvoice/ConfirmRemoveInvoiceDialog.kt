package net.fpoly.dailymart.view.detailinvoice

import android.content.Context
import net.fpoly.dailymart.base.BaseDialog
import net.fpoly.dailymart.databinding.DialogRemoveInvoiceConfirmBinding

class ConfirmRemoveInvoiceDialog(private val context: Context, private val viewModel: DetailInvoiceViewModel) :
    BaseDialog<DialogRemoveInvoiceConfirmBinding>(context, DialogRemoveInvoiceConfirmBinding::inflate) {

    override fun initData() {
        binding.tvConfirm.setOnClickListener {
            viewModel.removeInvoice(context)
            dismiss()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }
}