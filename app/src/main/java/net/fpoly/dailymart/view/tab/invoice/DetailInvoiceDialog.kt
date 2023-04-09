package net.fpoly.dailymart.view.tab.invoice

import android.content.Context
import net.fpoly.dailymart.base.BaseDialog
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.databinding.DialogDetailInvoiceBinding

class DetailInvoiceDialog(context: Context, private val viewModel: InvoiceViewModel, private val invoice: Invoice) :
    BaseDialog<DialogDetailInvoiceBinding>(context, DialogDetailInvoiceBinding::inflate) {
    override fun initData() {
        binding.viewmodel = viewModel
        binding.invoice = invoice

    }
}