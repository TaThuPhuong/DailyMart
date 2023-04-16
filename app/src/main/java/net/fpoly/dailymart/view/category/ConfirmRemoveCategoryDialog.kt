package net.fpoly.dailymart.view.category

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseDialog
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.DialogRemoveCategoryConfirmBinding
import net.fpoly.dailymart.databinding.DialogRemoveConfirmBinding

class ConfirmRemoveCategoryDialog(
    context: Context,
    private val category: Category,
    private val viewModel: CategoryViewModel
) : Dialog(context, R.style.BaseThemeDialog) {

    private lateinit var binding: DialogRemoveCategoryConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRemoveCategoryConfirmBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initData()

    }

    fun initData() {
        binding.tvConfirm.setOnClickListener {
            viewModel.removeCategory(category.id)
            dismiss()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }
}