package net.fpoly.dailymart.view.category

import android.content.Context
import android.content.DialogInterface
import androidx.core.content.ContextCompat
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Category
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
            val categoryParam = CategoryParam(name)
            if(isEdit) {
                viewModel.editCategory(category!!.id, categoryParam)
            }else {
                viewModel.createNewCategory(categoryParam)
            }
            dismiss()
        }
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        context.hideKeyboard()
    }

    private fun isValidate() : Boolean {
        val name = binding.edName.text.toString().replace(" ", "")
        if (name.isEmpty() || name.length < 3) {
            binding.edName.error = NAME_UN_VALIDATE
            return false
        }
        return true
    }

    companion object {
        const val TITLE_EDIT = "Sửa loại hàng"
        const val NAME_UN_VALIDATE = "Tên loại hàng không hợp lệ"
    }
}