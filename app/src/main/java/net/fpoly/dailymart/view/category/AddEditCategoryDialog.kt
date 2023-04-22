package net.fpoly.dailymart.view.category

import android.content.Context
import android.content.DialogInterface
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.CategoryAddParam
import net.fpoly.dailymart.data.model.CategoryParam
import net.fpoly.dailymart.databinding.DialogEditCategoryBinding
import net.fpoly.dailymart.extension.view_extention.hideKeyboard

class AddEditCategoryDialog(
    context: Context,
    private val viewModel: CategoryViewModel,
    private val category: Category?
) : BaseBottomDialog<DialogEditCategoryBinding>(context, DialogEditCategoryBinding::inflate) {

    private val isEdit = category != null
    override fun initData() {
        if (isEdit) {
            binding.title.text = TITLE_EDIT
            binding.edName.setText(category?.name)
        }
        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvCancel.setOnClickListener { dismiss() }
        binding.tvAddNew.setOnClickListener {
            if (!isValidate()) return@setOnClickListener
            val name = binding.edName.text.toString()
            val categoryParam = CategoryParam(name, category?.status ?: true)
            if (isEdit) {
                viewModel.editCategory(category!!.id, categoryParam)
            } else {

                viewModel.createNewCategory(CategoryAddParam(name))
            }
            dismiss()
        }
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        context.hideKeyboard()
    }

    private fun isValidate(): Boolean {
        val name = binding.edName.text.toString().replace(" ", "")
        if (name.isEmpty() || name.length < 3) {
            binding.edName.error = NAME_UN_VALIDATE
            return false
        }
        if (Regex("[0-9]").containsMatchIn(name)) {
            binding.edName.error = "Chuỗi không được chứa ký tự là số"
            return false
        }
        return true
    }

    companion object {
        const val TITLE_EDIT = "Sửa ngành hàng"
        const val NAME_UN_VALIDATE = "Tên ngành hàng không hợp lệ"
    }
}