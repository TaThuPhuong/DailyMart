package net.fpoly.dailymart.view.work_sheet

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityWorkSheetBinding

class WorkSheetActivity :
    BaseActivity<ActivityWorkSheetBinding>(ActivityWorkSheetBinding::inflate) {

    private val viewModel: WorkSheetViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupObserver() {

    }
}