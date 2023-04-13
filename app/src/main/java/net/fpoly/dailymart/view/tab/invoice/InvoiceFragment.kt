package net.fpoly.dailymart.view.tab.invoice

import android.content.Intent
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.InvoicceFragmentBinding
import net.fpoly.dailymart.extension.setupSnackbar
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity

class InvoiceFragment : BaseFragment<InvoicceFragmentBinding>(InvoicceFragmentBinding::inflate) {

    private val viewModel: InvoiceViewModel by viewModels { AppViewModelFactory }
    private lateinit var invoiceSellAdapter: InvoiceAdapter
    private lateinit var invoiceImportAdapter: InvoiceAdapter

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupSnackbar()
        setupBtnBack()
        setupAdapter()
        setupEditSearch()
        setupBtnAdd()
    }

    private fun setupBtnAdd() {
        binding.imvAdd.setOnClickListener {
            startActivity(Intent(mContext, AddInvoiceExportActivity::class.java))
        }
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    private fun setupEditSearch() {
        binding.edSearchInvoice.doAfterTextChanged {
            val text = binding.edSearchInvoice.text
            if (text.isNotEmpty()) {
                viewModel.invoicesResult.also { invoices ->
                    val result =
                        invoices.filter { it.id.contains(text) || it.user.name.contains(text) }
                            .toMutableList()
                    viewModel.invoices.value = result
                }
            } else {
                viewModel.invoices.value = viewModel.invoicesResult
            }
        }
    }

    private fun setupBtnBack() {
        binding.imvBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupAdapter() {
        invoiceSellAdapter = InvoiceAdapter(viewModel)
        invoiceImportAdapter = InvoiceAdapter(viewModel)

        binding.rvSell.adapter = invoiceSellAdapter
        binding.rvImport.adapter = invoiceImportAdapter
    }


    override fun setupObserver() {
    }

}