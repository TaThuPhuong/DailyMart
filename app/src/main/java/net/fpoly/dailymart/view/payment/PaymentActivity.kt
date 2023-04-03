package net.fpoly.dailymart.view.payment

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityPaymentBinding
import net.fpoly.dailymart.extension.view_extention.setMarginsStatusBar

class PaymentActivity : BaseActivity<ActivityPaymentBinding>(ActivityPaymentBinding::inflate) {

    private val viewModel: PaymentViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.layoutToolbar.setMarginsStatusBar(this)
    }

    override fun setupObserver() {

    }
}