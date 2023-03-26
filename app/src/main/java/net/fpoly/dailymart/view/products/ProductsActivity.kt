package net.fpoly.dailymart.view.products

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.database.ProductInfo
import net.fpoly.dailymart.databinding.ActivityProductsBinding
import net.fpoly.dailymart.view.add_product.AddProductActivity

class ProductsActivity : BaseActivity<ActivityProductsBinding>(ActivityProductsBinding::inflate) {

    private val TAG = "YingMing"

    private val viewModel: ProductsViewModel by viewModels { AppViewModelFactory }

    private lateinit var mProductAdapter: ProductAdapter

    private var mListProduct: List<ProductInfo> = ArrayList()

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.imvBack.setOnClickListener { finish() }
        binding.tvAddNew.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
        mProductAdapter = ProductAdapter(this, mListProduct)
        binding.rcvProducts.adapter = mProductAdapter
        viewModel.getListProduct()
    }

    override fun setupObserver() {
        viewModel.listProduct.observe(this) {
            Log.d(TAG, "setupObserver: $it")
            mProductAdapter.setData(it)
            mListProduct = it
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListProduct()
    }
}