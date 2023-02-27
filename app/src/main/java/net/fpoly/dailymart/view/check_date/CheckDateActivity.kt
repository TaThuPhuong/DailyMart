package net.fpoly.dailymart.view.check_date

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityCheckDateBinding

class CheckDateActivity :
    BaseActivity<ActivityCheckDateBinding>(ActivityCheckDateBinding::inflate) {

    private val viewModel: CheckDateViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupObserver() {

    }
}