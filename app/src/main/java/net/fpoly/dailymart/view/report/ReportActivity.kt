package net.fpoly.dailymart.view.report

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityReportBinding

class ReportActivity : BaseActivity<ActivityReportBinding>(ActivityReportBinding::inflate) {

    private val viewModel: ReportViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupObserver() {

    }
}