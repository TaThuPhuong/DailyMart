package net.fpoly.dailymart.view.task

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogOptionTaskBinding

class OptionTaskDialog(
    private val mContext: Context,
    private val onEdit: () -> Unit,
    private val onDelete: () -> Unit,
) :
    BaseBottomDialog<DialogOptionTaskBinding>(mContext, DialogOptionTaskBinding::inflate) {

    override fun initData() {
        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvEdit.setOnClickListener {
            onEdit()
            dismiss()
        }
        binding.tvDelete.setOnClickListener {
            onDelete()
            dismiss()
        }
    }
}