package net.fpoly.dailymart.view.category

import android.content.Context
import android.widget.Toast
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.databinding.DialogEditCategoryBinding

class EditCategoryDialog(private val mContext: Context, private val onConfirm: (String) -> Unit) :
    BaseBottomDialog<DialogEditCategoryBinding>(mContext, DialogEditCategoryBinding::inflate) {

    override fun initData() {
        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvCancel.setOnClickListener { dismiss() }
        binding.tvConfirm.setOnClickListener {
                val edtCategory = binding.edCategoryName.text.toString()
                if (edtCategory.isEmpty()){
                    Toast.makeText(context, "Dữ liệu không được để trống", Toast.LENGTH_SHORT).show()
                }else{
                    onConfirm(edtCategory)
                    Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
        }
    }
}