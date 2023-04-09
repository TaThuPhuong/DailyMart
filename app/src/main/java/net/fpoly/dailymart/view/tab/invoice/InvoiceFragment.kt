package net.fpoly.dailymart.view.tab.invoice

import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.InvoicceFragmentBinding
import net.fpoly.dailymart.extension.setupSnackbar

class InvoiceFragment : BaseFragment<InvoicceFragmentBinding>(InvoicceFragmentBinding::inflate) {

    private val viewModel: InvoiceViewModel by viewModels { AppViewModelFactory }
    private lateinit var invoiceSellAdapter: InvoiceAdapter
    private lateinit var invoiceImportAdapter: InvoiceAdapter
    private lateinit var invoiceDeductionAdapter: InvoiceAdapter

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupSnackbar()
        setupBtnBack()
        setupAdapter()
        setupEditSearch()
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
                        invoices.filter { it.id.contains(text) || it.user.name.contains(text)}
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
        invoiceDeductionAdapter = InvoiceAdapter(viewModel)

        binding.rvSell.adapter = invoiceSellAdapter
        binding.rvImport.adapter = invoiceImportAdapter
        binding.rvDeduction.adapter = invoiceDeductionAdapter
    }

//            startActivity(Intent(mContext, PayActivity::class.java))

    override fun setupObserver() {

    }

}