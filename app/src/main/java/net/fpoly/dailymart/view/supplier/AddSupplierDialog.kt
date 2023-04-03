package net.fpoly.dailymart.view.supplier

import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import net.fpoly.dailymart.databinding.DialogAddSupplierBinding

class AddSupplierDialog(private val activity: SupplierActivity, private val viewModel: SupplierViewModel) :
    BaseBottomDialog<DialogAddSupplierBinding>(activity, DialogAddSupplierBinding::inflate) {

    init {
        viewModel.eventShowDialogAdd.observe(activity){
            if(it && !isShowing) show()
        }
    }
    override fun initData() {

        binding.imvClose.setOnClickListener { dismiss() }
        binding.tvAddNew.setOnClickListener {
            if (!isValidate()) return@setOnClickListener
            val supplier = getSupplierParam()
            viewModel.addNewSupplier(supplier)
            dismiss()
        }
    }

    private fun getSupplierParam(): SupplierParam {
        val phoneNumber = binding.edSupplierPhone.text.toString().replace(" ", "")
        val nameSupplier = binding.edSupplierName.text.toString()
        return SupplierParam(supplierName = nameSupplier, phoneNumber = phoneNumber)
    }

    private fun isValidate() : Boolean {
        val phoneNumber = binding.edSupplierPhone.text.toString().replace(" ", "")
        val nameSupplier = binding.edSupplierName.text.toString().replace(" ", "")
        val regex = Regex("^0\\d{9,10}\$")
        var isValidate = true

        if(nameSupplier.length <= 6){
            binding.edSupplierName.error = "Độ dài ký tự không hợp lệ"
            isValidate = false
        }

        if (phoneNumber.length < 10 && !regex.matches(phoneNumber)) {
            binding.edSupplierPhone.error = "Số điện thoại không hợp lệ"
            isValidate = false
        }

        return isValidate
    }
}
