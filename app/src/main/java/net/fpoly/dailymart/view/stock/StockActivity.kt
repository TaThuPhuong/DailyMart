package net.fpoly.dailymart.view.stock

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityStockBinding

class StockActivity :BaseActivity<ActivityStockBinding>(ActivityStockBinding::inflate) {

    private val viewModel : StockViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupObserver() {

    }
}