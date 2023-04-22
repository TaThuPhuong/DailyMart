package net.fpoly.dailymart.view.supplier

import android.content.Context
import android.content.DialogInterface
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import net.fpoly.dailymart.databinding.DialogAddSupplierBinding
import net.fpoly.dailymart.extension.view_extention.hideKeyboard

class AddEditSupplierDialog(
    context: Context,
    private val supplier: Supplier? = null,
    private val viewModel: SupplierViewModel
) :
    BaseBottomDialog<DialogAddSupplierBinding>(context, DialogAddSupplierBinding::inflate) {

    override fun initData() {
        if (supplier != null) {
            binding.edSupplierPhone.setText(supplier.phoneNumber)
            binding.edSupplierName.setText(supplier.supplierName)
            binding.edRepresentativeName.setText(supplier.representativeName)
            binding.edAddress.setText(supplier.address)
            binding.tvAddNew.text = "Sửa"
        }

        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvAddNew.setOnClickListener {
            if (!isValidate()) return@setOnClickListener
            val supplierParam = getSupplierParam()
            if (supplier != null) {
                viewModel.editNewSupplier(supplier.id, supplierParam)
                dismiss()
            } else {
                viewModel.addNewSupplier(supplierParam)
                dismiss()
            }
        }
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        context.hideKeyboard()
    }

    private fun getSupplierParam(): SupplierParam {
        val phoneNumber = binding.edSupplierPhone.text.toString().replace(" ", "")
        val nameSupplier = binding.edSupplierName.text.toString()
        val representativeName = binding.edRepresentativeName.text.toString()
        val address = binding.edAddress.text.toString()
        return SupplierParam(
            supplierName = nameSupplier,
            phoneNumber = phoneNumber,
            representativeName = representativeName,
            address = address,
            status = supplier?.status ?: true
        )
    }

    private fun isValidate(): Boolean {
        val phoneNumber = binding.edSupplierPhone.text.toString().replace(" ", "")
        val nameSupplier = binding.edSupplierName.text.toString().replace(" ", "")
        val representativeName = binding.edRepresentativeName.text.toString().replace(" ", "")
        val address = binding.edAddress.text.toString().replace(" ", "")

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

        if (representativeName.length < 3) {
            binding.edRepresentativeName.error = "Tên người đại diện không hợp lệ"
            isValidate = false
        }

        if (address.length < 3) {
            binding.edAddress.error = "Địa chỉ không hợp lệ"
            isValidate = false
        }

        return isValidate
    }
}
