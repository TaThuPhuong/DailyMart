package net.fpoly.dailymart.view.products

import android.content.Intent
import androidx.activity.viewModels
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ActivityProductsBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.view.products.add_product.AddProductActivity
import net.fpoly.dailymart.view.products.adapter.ProductAdapter

class ProductsActivity : BaseActivity<ActivityProductsBinding>(ActivityProductsBinding::inflate) {

    private val TAG = "YingMing"

    private val viewModel: ProductsViewModel by viewModels { AppViewModelFactory }

    private lateinit var mProductAdapter: ProductAdapter

    private var mListProduct: List<Product> = ArrayList()

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getListProduct()
        mProductAdapter = ProductAdapter(this, mListProduct) {

        }
        binding.rcvProducts.adapter = mProductAdapter
        binding.imvBack.setOnClickListener { finish() }
        binding.imvClear.setOnClickListener {
            binding.edSearch.setText("")
            binding.imvClear.gone()
            mProductAdapter.setData(mListProduct)
        }
        binding.tvAddNew.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
        setSearch()
    }

    override fun setupObserver() {
        viewModel.listProduct.observe(this) {
            mProductAdapter.setData(it)
            mListProduct = it
        }
    }

    private fun setSearch() {
        binding.edSearch.getTextOnChange {
            val listFilter = mListProduct.filter { product ->
                product.name.contains(it, true) ||
                        product.barcode.contains(it, true)
            }
            mProductAdapter.setData(listFilter)
            if (listFilter.isEmpty()) {
                binding.tvNoData.visible()
            } else {
                binding.tvNoData.gone()
            }
            if (it.isEmpty()) {
                binding.imvClear.gone()
                mProductAdapter.setData(mListProduct)
            } else {
                binding.imvClear.visible()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListProduct()
    }
}