package net.fpoly.dailymart.view.stock

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogChangeQuantityBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange

class ChangeQuantityDialog(
    private val mContext: Context,
    private var quantity: Int,
    private val onSave: (newQuantity: Int) -> Unit,
) :
    BaseBottomDialog<DialogChangeQuantityBinding>(mContext, DialogChangeQuantityBinding::inflate) {

    override fun initData() {
        binding.edQuantity.setText(quantity.toString())
        binding.edQuantity.getTextOnChange {
            if (it.isNotEmpty()) {
                quantity = try {
                    it.toInt()
                } catch (e: Exception) {
                    0
                }
            }
        }
        binding.tvSave.setOnClickListener {
            onSave(quantity)
            dismiss()
        }
        binding.imvClose.setOnClickListener { dismiss() }
    }
}