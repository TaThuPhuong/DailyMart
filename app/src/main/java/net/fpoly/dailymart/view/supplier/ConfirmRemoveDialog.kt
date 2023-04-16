package net.fpoly.dailymart.view.supplier

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseDialog
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.DialogRemoveConfirmBinding

class ConfirmRemoveDialog(
    context: Context,
    private val supplier: Supplier,
    private val viewModel: SupplierViewModel
) :
    Dialog(context, R.style.BaseThemeDialog) {

    private lateinit var binding: DialogRemoveConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRemoveConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    fun initData() {
        binding.tvConfirm.setOnClickListener {
            viewModel.removeSupplier(supplier)
            dismiss()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }
}