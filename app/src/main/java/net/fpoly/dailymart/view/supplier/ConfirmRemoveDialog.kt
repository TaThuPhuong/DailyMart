package net.fpoly.dailymart.view.supplier

import android.content.Context
import net.fpoly.dailymart.base.BaseDialog
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.DialogRemoveConfirmBinding

class ConfirmRemoveDialog(private val context: Context, private val supplier: Supplier, private val viewModel: SupplierViewModel) :
    BaseDialog<DialogRemoveConfirmBinding>(context, DialogRemoveConfirmBinding::inflate) {

    override fun initData() {
        binding.tvConfirm.setOnClickListener {
            viewModel.removeSupplier(supplier)
            dismiss()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }
}