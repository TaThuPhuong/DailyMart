package net.fpoly.dailymart.view.check_date

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityCheckDateBinding
import net.fpoly.dailymart.extension.view_extention.setMarginsStatusBar
import net.fpoly.dailymart.utils.CheckDateFilter

class CheckDateActivity :
    BaseActivity<ActivityCheckDateBinding>(ActivityCheckDateBinding::inflate) {

    private val viewModel: CheckDateViewModel by viewModels { AppViewModelFactory }

    private var mFilter = CheckDateFilter.SOON

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.layoutToolbar.setMarginsStatusBar(this)
        binding.imvBack.setOnClickListener { finish() }
        binding.imvFilter.setOnClickListener {
            FilterCheckDateDialog(this, mFilter) {
                binding.tvFilter.text = getTextFilter(it)
                mFilter = it
            }.show()
        }
    }

    override fun setupObserver() {

    }

    private fun getTextFilter(type: CheckDateFilter): String {
        return when (type) {
            CheckDateFilter.SOON -> "Sắp hết hạn"
            CheckDateFilter.SEVEN_DAY -> "Hết hạn 7 ngày tới"
            CheckDateFilter.CATEGORY -> "Loại hàng"
        }
    }
}