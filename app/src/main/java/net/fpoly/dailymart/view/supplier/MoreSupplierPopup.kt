package net.fpoly.dailymart.view.supplier

import android.content.Context
import net.fpoly.dailymart.base.BaseBottomDialog
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.data.model.SupplierParam
import net.fpoly.dailymart.databinding.PopupDialogMoreSupplierBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible

class MoreSupplierPopup(
    private val context: Context,
    private val supplier: Supplier,
    private val viewModel: SupplierViewModel
) :
    BaseBottomDialog<PopupDialogMoreSupplierBinding>(
        context,
        PopupDialogMoreSupplierBinding::inflate
    ) {
    override fun initData() {
        setupDetail()
        binding.imvClose.setOnClickListener {
            dismiss()
        }

        binding.btnInfo.setOnClickListener {
            binding.title.text = "Chi tiết"
            binding.layoutInfo.visible()
            binding.layoutOption.gone()
        }

        binding.btnRemove.setOnClickListener {
            viewModel.openRemoveSupplier(context, supplier)
            dismiss()
        }

        binding.btnUpdate.setOnClickListener {
            viewModel.openEditSupplier(context, supplier)
            dismiss()
        }

        binding.btnRestore.setOnClickListener {
            val restore = SupplierParam(
                supplierName = supplier.supplierName,
                status = true,
                representativeName = supplier.representativeName,
                phoneNumber = supplier.phoneNumber,
                address = supplier.address
            )
            viewModel.editNewSupplier(supplier.id, restore)
            dismiss()
        }
    }

    private fun setupDetail() {
        if (supplier.status) {
            binding.btnRestore.gone()
        } else {
            binding.btnRemove.gone()
        }
        binding.nameSupplier.text = supplier.supplierName
        binding.phoneNumber.text = supplier.phoneNumber
        binding.representativeName.text = supplier.representativeName
        binding.address.text = supplier.address
        binding.status.text = if (supplier.status) "Đang hoạt động" else "Vô hiệu hóa"
    }
}