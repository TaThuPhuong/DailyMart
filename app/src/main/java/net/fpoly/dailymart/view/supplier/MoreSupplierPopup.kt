package net.fpoly.dailymart.view.supplier

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.PopupDialogMoreSupplierBinding

class MoreSupplierPopup(
    private val context: Context,
    private val supplier: Supplier,
    private val viewModel: SupplierViewModel
) :
    BaseBottomDialog<PopupDialogMoreSupplierBinding>(
        context,
        PopupDialogMoreSupplierBinding::inflate
    ) {
    override fun initData() {
        binding.btnRemove.setOnClickListener {
            viewModel.openRemoveSupplier(context, supplier)
            dismiss()
        }

        binding.btnUpdate.setOnClickListener {
            viewModel.openEditSupplier(context, supplier)
            dismiss()
        }
    }
}