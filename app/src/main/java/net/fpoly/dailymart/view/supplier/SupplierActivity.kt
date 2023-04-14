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

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupListSupplier()
        setupSearchSupplier()
        setupBtnClear()
        setupSnackbar()
        setupBtnBack()
    }

    private fun setupBtnBack() {
        binding.imvBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    override fun setupObserver() {
    }

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
                        invoices.filter { it.id.lowercase().contains(text) || it.supplierName.lowercase().contains(text) }
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