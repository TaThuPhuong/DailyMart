package net.fpoly.dailymart.view.category

import android.content.Context
import net.fpoly.dailymart.base.BaseDialog
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.DialogRemoveCategoryConfirmBinding
import net.fpoly.dailymart.databinding.DialogRemoveConfirmBinding

class ConfirmRemoveCategoryDialog(
    private val context: Context,
    private val category: Category,
    private val viewModel: CategoryViewModel
) :
    BaseDialog<DialogRemoveCategoryConfirmBinding>(
        context,
        DialogRemoveCategoryConfirmBinding::inflate
    ) {

    override fun initData() {
        binding.tvConfirm.setOnClickListener {
            viewModel.removeCategory(category.id)
            dismiss()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }
}