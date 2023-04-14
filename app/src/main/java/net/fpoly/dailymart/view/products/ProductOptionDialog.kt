package net.fpoly.dailymart.view.products

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogOptionProductBinding
import net.fpoly.dailymart.extension.view_extention.setVisibility

class ProductOptionDialog(
    private val mContext: Context,
    private val role: Boolean,
    private val onDetail: () -> Unit,
    private val onEdit: () -> Unit,
    private val onDelete: () -> Unit,
) :
    BaseBottomDialog<DialogOptionProductBinding>(mContext, DialogOptionProductBinding::inflate) {

    override fun initData() {
        binding.btnDelete.setVisibility(role)
        binding.btnEdit.setVisibility(role)
        binding.btnDetail.setOnClickListener {
            onDetail()
            dismiss()
        }
        binding.btnEdit.setOnClickListener {
            onEdit()
            dismiss()
        }
        binding.btnDelete.setOnClickListener {
            onDelete()
            dismiss()
        }
        binding.imvClose.setOnClickListener {
            dismiss()
        }
    }
}