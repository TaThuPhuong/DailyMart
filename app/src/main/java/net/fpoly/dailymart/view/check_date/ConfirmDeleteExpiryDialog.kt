package net.fpoly.dailymart.view.check_date

import android.annotation.SuppressLint
import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.ExpiryCheck
import net.fpoly.dailymart.databinding.DialogConfirmDeleteExpiryBinding
import net.fpoly.dailymart.extension.time_extention.date2String

class ConfirmDeleteExpiryDialog(
    private val mContext: Context,
    private val mExpiry: ExpiryCheck,
    private val onConfirm: () -> Unit,
) : BaseBottomDialog<DialogConfirmDeleteExpiryBinding>(
    mContext,
    DialogConfirmDeleteExpiryBinding::inflate
) {
    @SuppressLint("SetTextI18n")
    override fun initData() {
        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvCancel.setOnClickListener { dismiss() }
        binding.tvCancel.setOnClickListener { dismiss() }
        binding.tvConfirm.setOnClickListener {
            onConfirm()
            dismiss()
        }
        binding.tvTitle.text =
            "Bạn có chắc chắn muốn hủy ${mExpiry.quantity} \"${mExpiry.productName}\" HSD: ${mExpiry.expiryDate.date2String()}"
    }
}