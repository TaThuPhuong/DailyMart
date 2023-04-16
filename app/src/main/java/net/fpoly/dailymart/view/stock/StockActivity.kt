package net.fpoly.dailymart.view.stock

import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ActivityStockBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.setMarginsStatusBar
import net.fpoly.dailymart.extension.view_extention.visible

class StockActivity : BaseActivity<ActivityStockBinding>(ActivityStockBinding::inflate) {

    private val viewModel: StockViewModel by viewModels { AppViewModelFactory }

    private var mListProduct: List<Product> = ArrayList()
    private lateinit var mStockAdapter: StockAdapter

    override fun setOnClickListener() {
        binding.imvBack.setOnClickListener { finish() }
    }

    override fun setupData() {
        binding.layoutToolbar.setMarginsStatusBar(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.pbLoading.visible()
        initRecycleView()
        viewModel.getListProduct()
    }

    override fun setupObserver() {
        viewModel.listProduct.observe(this) {
            mListProduct = it
            mStockAdapter.setData(it)
            binding.pbLoading.gone()
        }
    }

    private fun initRecycleView() {
        mStockAdapter = StockAdapter(this, mListProduct)
        binding.rcvListProducts.adapter = mStockAdapter
    }
}