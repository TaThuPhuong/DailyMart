package net.fpoly.dailymart.view.tab.goods

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseFragment
import net.fpoly.dailymart.databinding.ProductFragmentBinding
import net.fpoly.dailymart.extention.view_extention.setMarginsStatusBar
import net.fpoly.dailymart.view.category.CategoryActivity
import net.fpoly.dailymart.view.check_date.CheckDateActivity
import net.fpoly.dailymart.view.order.OrderActivity
import net.fpoly.dailymart.view.stock.StockActivity
import net.fpoly.dailymart.view.supplier.SupplierActivity

class GoodsFragment : BaseFragment<ProductFragmentBinding>(ProductFragmentBinding::inflate),
    View.OnClickListener {

    private val viewModel: GoodsViewModel by viewModels { AppViewModelFactory }

    override fun setOnClickListener() {
        binding.layoutCategory.setOnClickListener(this)
        binding.layoutSupplier.setOnClickListener(this)
        binding.layoutProducts.setOnClickListener(this)
        binding.layoutImportGoods.setOnClickListener(this)
        binding.layoutCheckStock.setOnClickListener(this)
        binding.layoutCheckDate.setOnClickListener(this)
    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.layoutToolbar.setMarginsStatusBar(mContext)
    }

    override fun setupObserver() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.layoutCategory -> openActivity(CategoryActivity::class.java)
            binding.layoutSupplier -> openActivity(SupplierActivity::class.java)
            binding.layoutProducts -> {}
            binding.layoutImportGoods -> openActivity(OrderActivity::class.java)
            binding.layoutCheckStock -> openActivity(StockActivity::class.java)
            binding.layoutCheckDate -> openActivity(CheckDateActivity::class.java)
        }
    }

    private fun openActivity(c: Class<*>) {
        startActivity(Intent(mContext, c))
    }
}