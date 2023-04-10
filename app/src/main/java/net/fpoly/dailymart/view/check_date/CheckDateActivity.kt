package net.fpoly.dailymart.view.check_date

import android.util.Log
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.ExpiryCheck
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ActivityCheckDateBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.setMarginsStatusBar
import net.fpoly.dailymart.utils.CheckDateFilter
import net.fpoly.dailymart.utils.Constant
import java.text.SimpleDateFormat

class CheckDateActivity :
    BaseActivity<ActivityCheckDateBinding>(ActivityCheckDateBinding::inflate) {

    private val viewModel: CheckDateViewModel by viewModels { AppViewModelFactory }

    private val TAG = "YingMing"
    private var mFilter = CheckDateFilter.SOON

    private var mListProduct: List<Product> = ArrayList()
    private var mListFilter: List<Product> = ArrayList()
    private lateinit var mAdapter: ProductDateAdapter
    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.layoutToolbar.setMarginsStatusBar(this)
        binding.imvBack.setOnClickListener { finish() }
        binding.imvFilter.setOnClickListener {
            FilterCheckDateDialog(this, mFilter) {
                binding.tvFilter.text = getTextFilter(it)
                mFilter = it
                mAdapter.setData(getListData(mListProduct,it))
            }.show()
        }
        viewModel.getListProduct()
        initRecycleView()
    }

    override fun setupObserver() {
        viewModel.listProduct.observe(this) {
            mListProduct = it
            mAdapter.setData(getListData(it,mFilter))
            Log.e(TAG, "getListData: ${getListData(it,mFilter)}")
        }
        viewModel.message.observe(this) {
            if (it.isNotEmpty()) {
                showToast(this, it)
            }
        }
    }

    private fun getTextFilter(type: CheckDateFilter): String {
        return when (type) {
            CheckDateFilter.SOON -> "Sắp hết hạn"
            CheckDateFilter.SEVEN_DAY -> "Hết hạn 7 ngày tới"
            CheckDateFilter.CATEGORY -> "Loại hàng"
        }
    }

    private fun initRecycleView() {
        mAdapter = ProductDateAdapter(getListData(mListProduct,mFilter)) {

        }
        binding.rcvListProducts.adapter = mAdapter
    }

    private fun getListData(list: List<Product>, type: CheckDateFilter): List<ExpiryCheck> {
        val listData = ArrayList<ExpiryCheck>()

        for (product in list) {
            if (product.expires.isNotEmpty()) {
                product.expires.forEach {
                    listData.add(
                        ExpiryCheck(
                            id = it.id,
                            productId = it.productId,
                            productName = product.name,
                            expiryDate = it.expiryDate,
                            quantity = it.quantity,
                            image = product.img_product
                        )
                    )
                }
            }
        }
        return when (type) {
            CheckDateFilter.SOON -> listData.sortedBy { it.expiryDate }
            CheckDateFilter.SEVEN_DAY -> listData.filter { it.expiryDate - System.currentTimeMillis() <= Constant.SEVEN_DAY }
            CheckDateFilter.CATEGORY -> {
                listData
            }
        }
    }
}