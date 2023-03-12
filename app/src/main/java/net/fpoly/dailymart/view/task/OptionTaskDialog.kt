package net.fpoly.dailymart.view.task

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogOptionTaskBinding
import net.fpoly.dailymart.extention.view_extention.setVisibility
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class OptionTaskDialog(
    private val mContext: Context,
    private val finished: Boolean,
    private val onShowDetail: () -> Unit,
    private val onFinish: () -> Unit,
    private val onEdit: () -> Unit,
    private val onDelete: () -> Unit,
) :
    BaseBottomDialog<DialogOptionTaskBinding>(mContext, DialogOptionTaskBinding::inflate) {

    private val mUser = SharedPref.getUser(mContext)
    override fun initData() {
        binding.btnEdit.setVisibility(mUser.role != ROLE.STAFF)
        binding.btnDelete.setVisibility(mUser.role != ROLE.STAFF)
        binding.btnFinish.setVisibility(!finished)
        binding.imvClose.setOnClickListener { dismiss() }
        binding.btnDetail.setOnClickListener {
            onShowDetail()
            dismiss()
        }
        binding.btnFinish.setOnClickListener {
            onFinish()
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
    }
}