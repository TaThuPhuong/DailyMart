package net.fpoly.dailymart.view.tab.receipt

import android.content.Intent
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.ReceiptFragmentBinding
import net.fpoly.dailymart.view.pay.PayActivity

class ReceiptFragment : BaseFragment<ReceiptFragmentBinding>(ReceiptFragmentBinding::inflate) {

    private val viewModel: ReceiptViewModel by viewModels { AppViewModelFactory }
    private lateinit var invoiceSellAdapter: InvoiceAdapter
    private lateinit var invoiceImportAdapter: InvoiceAdapter
    private lateinit var invoiceDeductionAdapter: InvoiceAdapter

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupBtnBack()
        setupBtnAdd()
        setupAdapter()
        setupEditSearch()
    }

    private fun setupEditSearch() {
        binding.edSearchInvoice.doAfterTextChanged {
            val text = binding.edSearchInvoice.text
            if (text.isNotEmpty()) {
                viewModel.invoices.value?.also { invoices ->
                    val result =
                        invoices.filter { it.userId.contains(text) || it.id.contains(text) }
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
            requireActivity().onBackPressed()
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

    private fun setupBtnAdd() {
        binding.imvAdd.setOnClickListener {
//           viewModel.addNewInvoice()
            startActivity(Intent(mContext, PayActivity::class.java))
        }
    }

    override fun setupObserver() {

    }

}