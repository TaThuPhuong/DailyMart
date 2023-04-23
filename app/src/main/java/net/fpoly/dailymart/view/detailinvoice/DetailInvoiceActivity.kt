package net.fpoly.dailymart.view.detailinvoice

import android.view.View
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.InvoiceType
import net.fpoly.dailymart.databinding.ActivityDetailInvoiceBinding
import net.fpoly.dailymart.extension.setupSnackbar
import net.fpoly.dailymart.view.tab.invoice.DetailInvoiceAdapter

class DetailInvoiceActivity : BaseActivity<ActivityDetailInvoiceBinding>(ActivityDetailInvoiceBinding::inflate) {

    private val viewModel : DetailInvoiceViewModel by viewModels { AppViewModelFactory }
    private lateinit var adapter : DetailInvoiceAdapter
    private lateinit var adapterRefund : DetailInvoiceAdapter

    override fun setupData() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        viewModel.getInvoice(this)

        setupBtnBack()
        setupSnackbar()
        setupListInvoiceDetail()

        viewModel.eventResetScreen.observe(this) {
            viewModel.getInvoice(this)
        }
    }

    private fun setupBtnBack() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupListInvoiceDetail() {
        adapter = DetailInvoiceAdapter(this,viewModel)
        binding.listInvoiceDetail.adapter = adapter
        adapterRefund = DetailInvoiceAdapter(this, viewModel)
        binding.listInvoiceDetailRefund.adapter = adapterRefund
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    override fun setupObserver() {
        viewModel.invoice.observe(this) {
            if (it.type == InvoiceType.IMPORT.name) {
                binding.customer.visibility = View.GONE
            }
        }
    }

    companion object {
        const val INVOICE = "INVOICE"
    }
}