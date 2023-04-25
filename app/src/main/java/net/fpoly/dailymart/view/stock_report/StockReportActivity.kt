package net.fpoly.dailymart.view.stock_report

import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.ReportCheckStock
import net.fpoly.dailymart.data.model.stringToData
import net.fpoly.dailymart.databinding.ActivityStockReportBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.isShowing
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.firbase.database.ReportStockDao

class StockReportActivity :
    BaseActivity<ActivityStockReportBinding>(ActivityStockReportBinding::inflate) {


    private var mListReportStock: List<ReportCheckStock> = ArrayList()
    private lateinit var mReportStockAdapter: ReportStockAdapter

    override fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setupData() {
        initRecycleView()
        ReportStockDao.getReport {
            mListReportStock = it
            mReportStockAdapter.setData(it)
        }
    }

    override fun setupObserver() {

    }

    private fun initRecycleView() {
        mReportStockAdapter = ReportStockAdapter(mListReportStock) {
            binding.viewReport.setData(it.data.stringToData(), false, it.time)
            binding.viewReport.visible()
            binding.rcvList.gone()
        }
        binding.rcvList.adapter = mReportStockAdapter
    }

    override fun onBackPressed() {
        if (binding.viewReport.isShowing()) {
            binding.viewReport.gone()
            binding.rcvList.visible()
        } else {
            finish()
        }
    }
}