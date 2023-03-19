package net.fpoly.dailymart.view.supplier

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogAddSupplierBinding

class AddSupplierDialog(val mContext: Context, val onAdd: () -> Unit) :
    BaseBottomDialog<DialogAddSupplierBinding>(mContext, DialogAddSupplierBinding::inflate) {

    override fun initData() {
        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvAddNew.setOnClickListener {

            dismiss()
        }
    }
}