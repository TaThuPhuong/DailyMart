package net.fpoly.dailymart.view.payment

import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.param.InvoiceParam
import net.fpoly.dailymart.databinding.ActivityPaymentBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.utils.convertTotalInvoiceNumber

class PaymentActivity : BaseActivity<ActivityPaymentBinding>(ActivityPaymentBinding::inflate) {

    private val viewModel: PaymentViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getInvoice(this)

        setupCheckBox()
        setupInvoice()
        setupEdAmount()
        setupBtnCreateQR()
    }

    private fun setupBtnCreateQR() {
        binding.btnCreateQR.setOnClickListener {
            val amount = binding.edMoneyPay.text.toString()
            Glide.with(this)
                .load(createQrQuickLink(amount, "37510000400851"))
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
        } else {
            binding.layoutBanking.gone()
            binding.edAmount.visible()
        }
    }

    override fun setupObserver() {

    }

    companion object {
        private fun createQrQuickLink(amount: String, stk: String, bank: String = "BIDV"): String {
            return "https://img.vietqr.io/image/${bank}-${stk}-compact.png?amount=${amount}"
        }

    }
}