package net.fpoly.dailymart.view.task

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogFinishTaskConfirmBinding
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class FinishTaskConfirmDialog(
    private val mContext: Context,
    private val onConfirm: (comment: String?, time: Long) -> Unit,
) :
    BaseBottomDialog<DialogFinishTaskConfirmBinding>(
        mContext,
        DialogFinishTaskConfirmBinding::inflate
    ) {

    private val mUser = SharedPref.getUser(mContext)

    override fun initData() {
        binding.edComment.setVisibility(mUser!!.role != ROLE.staff)
        binding.imvClose.setOnClickListener { dismiss() }
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnConfirm.setOnClickListener {
            var time = 0L
            if (mUser.role == ROLE.staff) {
                time = System.currentTimeMillis()
            }
            onConfirm(binding.edComment.text.toString(), time)
            dismiss()
        }
    }
}