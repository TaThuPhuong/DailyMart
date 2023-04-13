package net.fpoly.dailymart.view.detailinvoice

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityDetailInvoiceBinding
import net.fpoly.dailymart.extension.setupSnackbar
import net.fpoly.dailymart.view.tab.invoice.DetailInvoiceAdapter

class DetailInvoiceActivity : BaseActivity<ActivityDetailInvoiceBinding>(ActivityDetailInvoiceBinding::inflate) {

    private val viewModel : DetailInvoiceViewModel by viewModels { AppViewModelFactory }
    private lateinit var adapter : DetailInvoiceAdapter

    override fun setupData() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        viewModel.getInvoice(this)

        setupSnackbar()
        setupListInvoiceDetail()
    }

    private fun setupListInvoiceDetail() {
        adapter = DetailInvoiceAdapter(viewModel)
        binding.listInvoiceDetail.adapter = adapter
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    override fun setupObserver() {

    }

    companion object {
        const val INVOICE = "INVOICE"
    }

}