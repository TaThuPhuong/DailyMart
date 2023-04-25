package net.fpoly.dailymart.view.stock

import android.util.Log
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ActivityStockBinding
import net.fpoly.dailymart.extension.view_extention.*
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref

class StockActivity : BaseActivity<ActivityStockBinding>(ActivityStockBinding::inflate) {

    private val TAG = "YingMing"
    private val viewModel: StockViewModel by viewModels { AppViewModelFactory }

    private var mListProduct: List<Product> = ArrayList()
    private lateinit var mStockAdapter: StockAdapter
    private var mListStockCheck: MutableMap<String, StockCheck> = mutableMapOf()

    override fun setOnClickListener() {
        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.layoutRefresh.setOnRefreshListener {
            viewModel.getListProduct()
        }
        binding.tvCreateReport.setOnClickListener {
            Log.e(TAG, "setOnClickListener: ")
            val listData = mListStockCheck.values.toList()
            binding.viewCreateReport.setData(listData)
            binding.viewCreateReport.visible()
        }
        binding.imvClear.setOnClickListener {
            binding.imvClear.gone()
            binding.edSearch.setText("")
        }
    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.pbLoading.visible()
        initRecycleView()
        viewModel.getListProduct()
        setSearch()
        binding.viewCreateReport.sendSuccess.observe(this) {
            if (it) {
                binding.tvCreateReport.gone()
                mListStockCheck.clear()
            }
        }
    }

    override fun setupObserver() {
        viewModel.listProducts.observe(this) {
            mListProduct = it
            mStockAdapter.setData(it)
        }
        viewModel.getProductSuccess.observe(this) {
            if (it) {
                binding.layoutRefresh.isRefreshing = false
                binding.pbLoading.gone()
            }
        }
    }

    private fun initRecycleView() {
        val role = SharedPref.getUser(this).role
        mStockAdapter = StockAdapter(this, mListProduct) {
            Log.e(TAG, "StockCheck: $it")
            mListStockCheck[it.expiryId] = it
            binding.tvCreateReport.setVisible(mListStockCheck.isNotEmpty() && role == ROLE.staff)
        }
        binding.rcvListProducts.adapter = mStockAdapter
    }

    private fun setSearch() {
        binding.edSearch.getTextOnChange { value ->
            val listFilter = mListProduct.filter {
                it.barcode.contains(value, true) || it.name.contains(
                    value,
                    true
                )
            }
            if (value.isEmpty()) {
                binding.imvClear.gone()
                mStockAdapter.setData(mListProduct)
            } else {
                binding.imvClear.visible()
            }
            if (listFilter.isEmpty()) {
                binding.tvNoData.visible()
            } else {
                binding.tvNoData.gone()
            }
            mStockAdapter.setData(listFilter)
        }
    }

    override fun onBackPressed() {
        if (binding.viewCreateReport.isShowing()) {
            binding.viewCreateReport.gone()
        } else {
            finish()
        }
    }
}