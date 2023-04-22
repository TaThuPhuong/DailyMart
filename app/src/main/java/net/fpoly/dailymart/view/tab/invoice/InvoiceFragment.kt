package net.fpoly.dailymart.view.tab.invoice

import android.content.Intent
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.InvoicceFragmentBinding
import net.fpoly.dailymart.extension.setupSnackbar
import net.fpoly.dailymart.view.getinvoice.GetInvoiceActivity
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity

class InvoiceFragment : BaseFragment<InvoicceFragmentBinding>(InvoicceFragmentBinding::inflate) {

    private val viewModel: InvoiceViewModel by viewModels { AppViewModelFactory }
    private lateinit var invoiceAdapter: InvoiceAdapter

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupSnackbar()
        setupBtnBack()
        setupAdapter()
        setupEditSearch()
        setupBtnAdd()
        setupBtnGetInvoice()
    }

    private fun setupBtnGetInvoice() {
        binding.getInvoice.setOnClickListener {
            startActivity(Intent(mContext, GetInvoiceActivity::class.java))
        }
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
            val text = binding.edSearchInvoice.text.toString().lowercase()
            if (text.isNotEmpty()) {
                viewModel.rootInvoices.also { invoices ->
                    val result = invoices.filter {
                        it.numberID.lowercase().contains((text)) || it.user.name.lowercase()
                            .contains(text)
                    }.toMutableList()
                    viewModel.invoices.value = result
                }
            } else {
                viewModel.showInvoice()
            }
        }
    }

    private fun setupBtnBack() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupAdapter() {
        invoiceAdapter = InvoiceAdapter(viewModel)
        binding.listInvoices.adapter = invoiceAdapter
        binding.listInvoices.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.loadMore()
                }
            }
        })
    }


    override fun setupObserver() {
    }

}