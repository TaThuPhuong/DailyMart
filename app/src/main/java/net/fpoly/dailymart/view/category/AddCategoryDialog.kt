package net.fpoly.dailymart.view.category

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogEditCategoryBinding

class AddCategoryDialog(private val mContext: Context, private val onConfirm: () -> Unit) :
    BaseBottomDialog<DialogEditCategoryBinding>(mContext, DialogEditCategoryBinding::inflate) {

    override fun initData() {
        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvCancel.setOnClickListener { dismiss() }
        binding.tvConfirm.setOnClickListener {
            onConfirm()
            dismiss()
        }
    }
}