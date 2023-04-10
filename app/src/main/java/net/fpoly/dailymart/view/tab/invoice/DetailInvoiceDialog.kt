package net.fpoly.dailymart.view.tab.invoice

import android.content.Context
import android.content.DialogInterface
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.BaseDialog
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.databinding.DialogDetailInvoiceBinding

class DetailInvoiceDialog(private val context: Context, private val viewModel: InvoiceViewModel, private val invoice: Invoice) :
    BaseDialog<DialogDetailInvoiceBinding>(context, DialogDetailInvoiceBinding::inflate) {

    val adapter = DetailInvoiceAdapter(viewModel)

    override fun initData() {
        binding.viewmodel = viewModel
        binding.invoice = invoice
        viewModel.isRefund.observe(context as BaseActivity<*>){
            adapter.notifyItemRangeChanged(0, adapter.currentList.size)
        }
        binding.listInvoiceDetail.adapter = adapter
    }

    override fun dismiss() {
        super.dismiss()
        viewModel.isRefund.value = false
    }
}