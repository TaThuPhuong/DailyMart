package net.fpoly.dailymart.view.products

import android.content.Intent
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.databinding.ActivityProductsBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.*
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.view.products.add_product.AddProductActivity
import net.fpoly.dailymart.view.products.adapter.ProductAdapter
import net.fpoly.dailymart.view.products.adapter.ProductDisableAdapter
import net.fpoly.dailymart.view.products.edit_product.ProductEditActivity
import net.fpoly.dailymart.view.products.detail_product.ProductDetailActivity

class ProductsActivity : BaseActivity<ActivityProductsBinding>(ActivityProductsBinding::inflate) {

    private val TAG = "YingMing"

    private val viewModel: ProductsViewModel by viewModels { AppViewModelFactory }

    private lateinit var mProductAdapter: ProductAdapter
    private lateinit var mProductDisableAdapter: ProductDisableAdapter

    private var mListProduct: List<Product> = ArrayList()
    private var mListProductDisable: List<Product> = ArrayList()

    private var mUser: User? = null

    private var isShowActiveProduct = true

    override fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.imvClear.setOnClickListener {
            binding.edSearch.setText("")
            binding.imvClear.gone()
            mProductAdapter.setData(mListProduct)
        }
        binding.tvAddNew.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
        binding.imvBin.setOnClickListener {
            showProductDisable()
        }
    }

    override fun setupData() {
        mUser = SharedPref.getUser(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.pbLoading.visible()
        viewModel.getListProducts()
        initRecycleProducts()
        setSearch(mListProduct) {
            mProductAdapter.setData(it)
            binding.tvNoData.setVisible(it.isEmpty())
            binding.rcvProducts.setVisible(it.isNotEmpty())
        }
        binding.rcvProducts.adapter = mProductAdapter
    }

    override fun setupObserver() {
        viewModel.listProductActive.observe(this) {
            mProductAdapter.setData(it)
            mListProduct = it
        }
        viewModel.getListSuccess.observe(this) {
            if (it) binding.pbLoading.gone()
        }
        viewModel.listProductDisable.observe(this) {
            mListProductDisable = it
            mProductDisableAdapter.setData(it)
        }
        viewModel.message.observe(this) {
            if (it.isNotEmpty()) showToast(this, it)
        }
    }

    private fun initRecycleProducts() {
        mProductAdapter = ProductAdapter(this, mListProduct) {
            ProductOptionDialog(this, mUser!!.role != ROLE.staff,
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

                        }
                    }.show()
                }).show()
        }
        mProductDisableAdapter = ProductDisableAdapter(this, mListProductDisable,
            onClick = {
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra(Constant.PRODUCT, it)
                startActivity(intent)
            }, onRestore = {
                viewModel.onRestore(it)
            })
        binding.rcvProducts.adapter = mProductAdapter
    }

    private fun setSearch(list: List<Product>, onFilter: (List<Product>) -> Unit) {
        binding.edSearch.getTextOnChange {
            val listFilter = list.filter { product ->
                product.name.contains(it, true) ||
                        product.barcode.contains(it, true)
            }
            onFilter(listFilter)
        }
    }

    private fun showProductDisable() {
        isShowActiveProduct = !isShowActiveProduct
        binding.tvAddNew.setVisibility(isShowActiveProduct)
        binding.imvBin.setVisibility(isShowActiveProduct)
        if (isShowActiveProduct) {
            binding.tvTitle.text = "Sản phẩm"
            setSearch(mListProduct) {
                mProductAdapter.setData(it)
                binding.tvNoData.setVisible(it.isEmpty())
                binding.rcvProducts.setVisible(it.isNotEmpty())
            }
            binding.rcvProducts.adapter = mProductAdapter
        } else {
            binding.tvTitle.text = "Sản phẩm ngừng bán"
            setSearch(mListProductDisable) {
                mProductDisableAdapter.setData(it)
                binding.tvNoData.setVisible(it.isEmpty())
                binding.rcvProducts.setVisible(it.isNotEmpty())
            }
            binding.rcvProducts.adapter = mProductDisableAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListProducts()
    }

    override fun onBackPressed() {
        if (!isShowActiveProduct) {
            showProductDisable()
        } else {
            finish()
        }
    }
}