package net.fpoly.dailymart.view.tab.invoice

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
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
        setupAdapter()
        setupEditSearch()
        setupBtnAdd()
        setupBtnGetInvoice()
        setupRefresh()
    }

    private fun setupRefresh() {
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.pink_primary
            )
        )
        binding.refreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getInvoices(clear = true, loading = false)
                binding.refreshLayout.isRefreshing = false
            }
        }
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
                val filter = viewModel.rootInvoices.filter {
                    it.numberID.contains(
                        text,
                        true
                    ) || it.user.name.contains(text, true)
                }
                viewModel.listInvoice = filter.toMutableList()
                viewModel.showInvoice()
            } else {
                viewModel.listInvoice = viewModel.rootInvoices
                viewModel.showInvoice()
            }
        }
    }

    private fun setupAdapter() {
        invoiceAdapter = InvoiceAdapter(viewModel)
        binding.listInvoices.adapter = invoiceAdapter
        binding.listInvoices.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    viewModel.loadMore()
                }
            }
        })
    }


    override fun setupObserver() {
    }

}