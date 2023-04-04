package net.fpoly.dailymart.view.supplier

import android.content.Context
import android.util.Log
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import net.fpoly.dailymart.databinding.DialogAddSupplierBinding

class AddEditSupplierDialog(context: Context, private val supplier: Supplier? = null, private val viewModel: SupplierViewModel) :
    BaseBottomDialog<DialogAddSupplierBinding>(context, DialogAddSupplierBinding::inflate) {

    override fun initData() {
        if (supplier != null) {
            binding.edSupplierPhone.setText(supplier.phoneNumber)
            binding.edSupplierName.setText(supplier.supplierName)
            binding.tvAddNew.text = "Sửa"
        }

        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvAddNew.setOnClickListener {
            if (!isValidate()) return@setOnClickListener
            if (supplier != null) {
                val supplierParam = getSupplierParam()
                viewModel.editNewSupplier(supplier.id, supplierParam)
                dismiss()
            }else {
                val supplier = getSupplierParam()
                viewModel.addNewSupplier(supplier)
                dismiss()
            }
        }
    }

    private fun getSupplierParam(): SupplierParam {
        val phoneNumber = binding.edSupplierPhone.text.toString().replace(" ", "")
        val nameSupplier = binding.edSupplierName.text.toString()
        return SupplierParam(supplierName = nameSupplier, phoneNumber = phoneNumber)
    }

    private fun isValidate(): Boolean {
        val phoneNumber = binding.edSupplierPhone.text.toString().replace(" ", "")
        val nameSupplier = binding.edSupplierName.text.toString().replace(" ", "")
        val regex = Regex("(84|0[3|5|7|8|9])+([0-9]{8})\\b")
        var isValidate = true

        if (nameSupplier.length < 3) {
            binding.edSupplierName.error = "Độ dài ký tự không hợp lệ"
            isValidate = false
        }

        if (nameSupplier.toIntOrNull() != null) {
            binding.edSupplierName.error = "Tên nhà cung cấp không hợp lệ"
            isValidate = false
        }

        if (phoneNumber.length > 11 || !regex.matches(phoneNumber)) {
            binding.edSupplierPhone.error = "Số điện thoại không hợp lệ"
            isValidate = false
        }
        Log.e("TAG", "isValidate: ${phoneNumber.length}")

        return isValidate
    }
}
