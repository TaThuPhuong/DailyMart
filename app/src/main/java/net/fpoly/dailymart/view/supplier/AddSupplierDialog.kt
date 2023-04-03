package net.fpoly.dailymart.view.supplier

import android.content.Context
import android.util.Log
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.DialogAddSupplierBinding

class AddSupplierDialog(private val mContext: Context, private val onAdd: (Supplier) -> Unit) :
    BaseBottomDialog<DialogAddSupplierBinding>(mContext, DialogAddSupplierBinding::inflate) {

    override fun initData() {
        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvAddNew.setOnClickListener {

//            onAdd()
            dismiss()
        }
    }
}



//binding.tvAddNew.setOnClickListener {
//            val supName = binding.edSupplierName.text.toString()
//            val supPhone= binding.edSupplierPhone.text.toString()
//            val supplier = Supplier()
//            supplier.name = supName
//            supplier.phone = supPhone
//            Log.d("aa", "initData: "+supplier)
//            onAdd(supplier)
//            dismiss()
//        }