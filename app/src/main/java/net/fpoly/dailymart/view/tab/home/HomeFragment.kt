package net.fpoly.dailymart.view.tab.home

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.HomeFragmentBinding
import net.fpoly.dailymart.extension.view_extention.setMarginsStatusBar
import net.fpoly.dailymart.view.check_date.CheckDateActivity
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity
import net.fpoly.dailymart.view.profile.ProfileActivity
import net.fpoly.dailymart.view.report.ReportActivity
import net.fpoly.dailymart.view.stock.StockActivity
import net.fpoly.dailymart.view.task.TaskActivity
import net.fpoly.dailymart.view.work_sheet.WorkSheetActivity

class HomeFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate),
    View.OnClickListener {

    private val viewModel: HomeViewModel by viewModels { AppViewModelFactory }

    override fun setOnClickListener() {
        binding.imvAvatarToolbar.setOnClickListener(this)
        binding.imvNotification.setOnClickListener(this)
        binding.layoutReport.setOnClickListener(this)
        binding.imvCheckDate.setOnClickListener(this)
        binding.imvReport.setOnClickListener(this)
        binding.imvWorkSheet.setOnClickListener(this)
        binding.imvStock.setOnClickListener(this)
        binding.imvTask.setOnClickListener(this)
        binding.imvPay.setOnClickListener(this)
    }

    override fun setupData() {
        binding.layoutToolbar.setMarginsStatusBar(mContext)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun setupObserver() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imvAvatarToolbar -> openActivity(ProfileActivity::class.java)
            binding.imvNotification -> {}
            binding.layoutReport, binding.imvReport -> {
                openActivity(ReportActivity::class.java)
            }
            binding.imvCheckDate -> {
                openActivity(CheckDateActivity::class.java)
            }
            binding.imvWorkSheet -> {
                openActivity(WorkSheetActivity::class.java)
            }
            binding.imvStock -> {
                openActivity(StockActivity::class.java)
            }
            binding.imvTask -> {
                openActivity(TaskActivity::class.java)
            }
            binding.imvPay -> {
                openActivity(AddInvoiceExportActivity::class.java)
            }
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(mContext, c))
    }
}