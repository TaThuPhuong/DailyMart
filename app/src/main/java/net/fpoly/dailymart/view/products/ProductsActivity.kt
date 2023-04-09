package net.fpoly.dailymart.view.products

import android.content.Intent
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityProductsBinding
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.products.add_product.AddProductActivity
import net.fpoly.dailymart.view.products.adapter.ProductAdapter
import net.fpoly.dailymart.view.products.edit_product.ProductEditActivity
import net.fpoly.dailymart.view.task.detail_product.ProductDetailActivity

class ProductsActivity : BaseActivity<ActivityProductsBinding>(ActivityProductsBinding::inflate) {

    private val TAG = "YingMing"

    private val viewModel: ProductsViewModel by viewModels { AppViewModelFactory }

    private lateinit var mProductAdapter: ProductAdapter

    private var mListProduct: List<Product> = ArrayList()

    private var mUser: User? = null

    override fun setupData() {
        mUser = SharedPref.getUser(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getListProducts()
        mProductAdapter = ProductAdapter(this, mListProduct) {
            if (mUser!!.role != ROLE.staff) {
                ProductOptionDialog(this,
                    onDetail = {
                        val intent = Intent(this, ProductDetailActivity::class.java)
                        intent.putExtra(Constant.PRODUCT, it)
                        startActivity(intent)
                    }, onEdit = {
                        val intent = Intent(this, ProductEditActivity::class.java)
                        intent.putExtra(Constant.PRODUCT, it)
                        startActivity(intent)
                    }, onDelete = {
                        DeleteProductConfirmDialog(this) {
                            viewModel.onDelete(it) {
                                val snack =
                                    Snackbar.make(binding.root, "Đã xóa", Snackbar.LENGTH_LONG)
                                snack.setAction("Hoàn tác") {
                                    viewModel.onRestore()
                                }
                                snack.show()
                            }
                        }.show()
                    }).show()
            } else {

            }
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
        viewModel.getListProducts()
    }
}