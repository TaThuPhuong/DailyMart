package net.fpoly.dailymart.view.category

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.databinding.PopupDialogMoreSupplierBinding

class MoreCategoryPopup(
    private val context: Context,
    private val category: Category,
    private val viewModel: CategoryViewModel
) :
    BaseBottomDialog<PopupDialogMoreSupplierBinding>(
        context,
        PopupDialogMoreSupplierBinding::inflate
    ) {
    override fun initData() {
        binding.btnRemove.setOnClickListener {
            viewModel.clickRemove(context, category)
            dismiss()
        }

        binding.btnUpdate.setOnClickListener {
            viewModel.clickEdit(context, category)
            dismiss()
        }
    }
}