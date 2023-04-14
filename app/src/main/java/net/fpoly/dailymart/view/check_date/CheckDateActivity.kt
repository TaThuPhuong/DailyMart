package net.fpoly.dailymart.view.check_date

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.ExpiryCheck
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ActivityCheckDateBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.setMarginsStatusBar
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.utils.CheckDateFilter
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.view.message.MessageActivity
import java.text.SimpleDateFormat

class CheckDateActivity :
    BaseActivity<ActivityCheckDateBinding>(ActivityCheckDateBinding::inflate) {

    private val viewModel: CheckDateViewModel by viewModels { AppViewModelFactory }

    private val TAG = "YingMing"
    private var mFilter = CheckDateFilter.SOON

    private var mListExpiry: List<ExpiryCheck> = ArrayList()
    private var mListProduct: List<Product> = ArrayList()
    private lateinit var mAdapter: ProductDateAdapter

    private var mLoadingDialog: LoadingDialog? = null
    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mLoadingDialog = LoadingDialog(this)
        mLoadingDialog?.showLoading()
        binding.layoutToolbar.setMarginsStatusBar(this)
        binding.imvBack.setOnClickListener { finish() }
        viewModel.getListProduct()
        initRecycleView()
        setSearch()
        binding.imvFilter.setOnClickListener {
            FilterCheckDateDialog(this, mFilter) {
                binding.tvFilter.text = getTextFilter(it)
                mFilter = it
                mAdapter.setData(getListData(mListProduct, it))
            }.show()
        }
        binding.imvClear.setOnClickListener {
            binding.imvClear.gone()
            binding.edSearch.setText("")
            mAdapter.setData(getListData(mListProduct, mFilter))
        }
    }

    override fun setupObserver() {
        viewModel.listProduct.observe(this) {
            mListProduct = it
            mAdapter.setData(getListData(it, mFilter))
            binding.pbLoading.gone()
            mLoadingDialog?.hideLoading()
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

    private fun setSearch() {
        binding.edSearch.getTextOnChange { value ->
            mListExpiry = getListData(mListProduct, mFilter)
            val listFilter = mListExpiry.filter {
                it.id.contains(value, true) || it.productName.contains(
                    value,
                    true
                )
            }
            mAdapter.setData(listFilter)
        }
    }

    private fun initRecycleView() {
        mAdapter = ProductDateAdapter(this, getListData(mListProduct, mFilter)) {
            CheckDateDetailDialog(this, it,
                onDestroy = {
                    ConfirmDeleteExpiryDialog(this, it) {
                        viewModel.onDestroyProduct(it)
                    }.show()
                }, onMakeMessage = {
                    val intent = Intent(this, MessageActivity::class.java)
                    intent.putExtra(Constant.MESSAGE, makeMessage(it))
                    startActivity(intent)
                }).show()
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
                            barcode = product.barcode,
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

    private fun makeMessage(expiry: ExpiryCheck): String {
        return "Có ${expiry.quantity} ${expiry.productName} đã hết hạn ngày ${expiry.expiryDate.date2String()}!!!"
    }
}