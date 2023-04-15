package net.fpoly.dailymart.view.payment

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.BankInfo
import net.fpoly.dailymart.data.model.param.InvoiceParam
import net.fpoly.dailymart.databinding.ActivityPaymentBinding
import net.fpoly.dailymart.extension.setupSnackbar
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.utils.convertTotalInvoiceNumber
import net.fpoly.dailymart.view.main.MainActivity

class PaymentActivity : BaseActivity<ActivityPaymentBinding>(ActivityPaymentBinding::inflate) {

    private val viewModel: PaymentViewModel by viewModels { AppViewModelFactory }
    private lateinit var bankInfo: BankInfo
    private lateinit var adapter: PaymentAdapter

    override fun setupData() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        bankInfo = SharedPref.getBankInfo(this)
        viewModel.getInvoice(this)

        setupSnackbar()
        setupBtnBack()
        setupCheckBox()
        setupInvoice()
        setupEdAmount()
        setupBtnCreateQR()
        setupListInvoice()
        setupChoseCustomer()
        setupBtnCreateInvoice()
        setupBtnCreateSend()
    }

    private fun setupBtnCreateSend() {
        binding.sendInvoice.setOnClickListener {
            viewModel.sendInvoice(this)
        }
    }

    private fun setupBtnCreateInvoice() {
        binding.btnPayment.setOnClickListener {
            viewModel.createInvoice()
        }
        viewModel.eventCreateInvoiceSuccess.observe(this) {
            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                it.putExtra(MainActivity.MAIN_EVENT, NEW_INVOICE_CREATE)
                startActivity(it)
            }
        }
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    private fun setupChoseCustomer() {
        binding.pickCustomer.setOnClickListener {
            CustomerPopup(this, viewModel).show()
        }
        viewModel.customerSelected.observe(this) {
            binding.pickCustomer.setText(it.name)
        }
    }

    private fun setupBtnBack() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupListInvoice() {
        adapter = PaymentAdapter()
        binding.productList.adapter = adapter
    }

    private fun setupBtnCreateQR() {
        binding.btnCreateQR.setOnClickListener {
            val amount = binding.edMoneyPay.text.toString()
            Glide.with(this)
                .load(
                    getQuickQRCode(
                        bankInfo.bankId,
                        bankInfo.accountNumber,
                        amount,
                        bankInfo.accountName
                    )
                )
                .into(binding.imageQr)
        }
    }

    private fun setupEdAmount() {
        binding.edAmount.doAfterTextChanged {
            val text = binding.edAmount.text.toString().toIntOrNull() ?: 0
            val invoiceTotal = viewModel.invoice.value?.totalBill ?: 0
            val repaired = text - invoiceTotal
            binding.amountReturn.text =
                getString(
                    R.string.cover_vnd,
                    convertTotalInvoiceNumber(if (repaired < 0) 0 else repaired)
                )
            binding.amountPaid.text =
                getString(R.string.cover_vnd, convertTotalInvoiceNumber(text.toLong()))
        }
    }

    private fun setupInvoice() {
        viewModel.invoice.observe(this) {
            setupInvoiceData(it)
        }
    }

    private fun setupInvoiceData(invoice: InvoiceParam) {
        binding.totalInvoice.text =
            getString(R.string.cover_vnd, convertTotalInvoiceNumber(invoice.totalBill))
        binding.amountPaid.text =
            getString(R.string.cover_vnd, convertTotalInvoiceNumber(0))
        binding.amountReturn.text =
            getString(R.string.cover_vnd, convertTotalInvoiceNumber(0))
        adapter.submitList(invoice.products)
    }

    private fun setupCheckBox() {
        checkChange(binding.checkBoxPayment.isChecked)
        binding.checkBoxPayment.setOnCheckedChangeListener { _, isChecked ->
            checkChange(isChecked)
        }
    }

    private fun checkChange(isChecked: Boolean) {
        if (isChecked) {
            binding.layoutBanking.visible()
            binding.edAmount.gone()
            val invoiceTotal = viewModel.invoice.value?.totalBill ?: 0

            binding.amountPaid.text =
                getString(R.string.cover_vnd, convertTotalInvoiceNumber(invoiceTotal))
            binding.amountReturn.text =
                getString(R.string.cover_vnd, convertTotalInvoiceNumber(0))
        } else {
            binding.layoutBanking.gone()
            binding.edAmount.visible()
        }
    }

    override fun setupObserver() {
    }

    companion object {
        const val NEW_INVOICE_CREATE = "NEW_INVOICE_CREATE"

        private fun getQuickQRCode(
            bankID: String,
            accountNumber: String,
            amount: String,
            name: String,
        ): String {
            return "https://img.vietqr.io/image/$bankID-$accountNumber-3X8QDX1.jpg?amount=$amount&accountName=$name"
        }
    }
}