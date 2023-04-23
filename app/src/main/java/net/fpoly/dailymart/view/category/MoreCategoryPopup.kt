package net.fpoly.dailymart.view.category

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.CategoryParam
import net.fpoly.dailymart.databinding.PopupDialogMoreSupplierBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible

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
        setupCategory()
        binding.imvClose.setOnClickListener {
            dismiss()
        }

        binding.btnInfo.setOnClickListener {
            binding.layoutInfoCategory.visible()
            binding.layoutOption.gone()
        }

        binding.btnRemove.setOnClickListener {
            viewModel.clickRemove(context, category)
            dismiss()
        }

        binding.btnUpdate.setOnClickListener {
            viewModel.clickEdit(context, category)
            dismiss()
        }

        binding.btnRestore.setOnClickListener {
            val params = CategoryParam(category.name, true)
            viewModel.editCategory(category.id, params)
            dismiss()
        }
    }

    private fun setupCategory() {
        if (category.status) binding.btnRestore.gone() else binding.btnRemove.gone()
        binding.idCategory.text = category.id
        binding.nameCategory.text = category.name
        binding.statusCategory.text = if (category.status) "Đang hoạt động" else "Vô hiệu hóa"
    }
}