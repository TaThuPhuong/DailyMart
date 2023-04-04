package net.fpoly.dailymart.view.supplier

import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivitySupplierBinding
import net.fpoly.dailymart.extension.setupSnackbar

class SupplierActivity : BaseActivity<ActivitySupplierBinding>(ActivitySupplierBinding::inflate) {

    private val viewModel: SupplierViewModel by viewModels { AppViewModelFactory }
    private lateinit var supplierAdapter: SupplierAdapter
    private lateinit var addSupplierDialog: AddSupplierDialog

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupListSupplier()
        setupSearchSupplier()
        setupBtnClear()
        setupDialogAdd()
        setupSnackbar()
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    private fun setupDialogAdd() {
        addSupplierDialog = AddSupplierDialog(this, viewModel)
        viewModel.eventShowDialogAdd.observe(this) {
            addSupplierDialog.show()
        }
    }

    override fun setupObserver() {}

    private fun setupBtnClear() {
        binding.imvClear.setOnClickListener { binding.edSearch.setText("") }
    }

    private fun setupSearchSupplier() {
        binding.edSearch.doAfterTextChanged {
            val text = binding.edSearch.text
            if (text.isNotEmpty()) {
                binding.imvClear.visibility = View.VISIBLE
                viewModel.listSupplier.value?.also { invoices ->
                    val result =
                        invoices.filter { it.id.contains(text) || it.supplierName.contains(text) }
                            .toMutableList()
                    viewModel.listSupplier.value = result
                }
            } else {
                binding.imvClear.visibility = View.GONE
                viewModel.listSupplier.value = viewModel.listSupplierRemote
            }
        }
    }

    private fun setupListSupplier() {
        supplierAdapter = SupplierAdapter(viewModel)
        binding.listSupplier.adapter = supplierAdapter
    }
}