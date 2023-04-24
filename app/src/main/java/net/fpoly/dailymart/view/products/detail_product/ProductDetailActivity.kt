package net.fpoly.dailymart.view.products.detail_product

import android.annotation.SuppressLint
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ActivityProductDetailBinding
import net.fpoly.dailymart.extension.view_extention.setVisibility
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ROLE
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.utils.toMoney

class ProductDetailActivity :
    BaseActivity<ActivityProductDetailBinding>(ActivityProductDetailBinding::inflate) {

    private var mProduct: Product? = null

    private val viewModel: ProductDetailViewModel by viewModels { AppViewModelFactory }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mProduct = intent.getSerializableExtra(Constant.PRODUCT) as Product
        mProduct?.let {
            setData(it)
        }
        binding.btnBack.setOnClickListener { finish() }
        binding.btnClose.setOnClickListener { finish() }
    }

    override fun setupObserver() {

    }

    @SuppressLint("SetTextI18n")
    private fun setData(product: Product) {
        val role = SharedPref.getUser(this).role
        binding.tvImportPrice.setVisibility(role == ROLE.manager)
        binding.tvId.text = product.barcode
        binding.tvName.text = product.name
        binding.tvQuantity.text = product.totalQuantity.toString()
        binding.tvCategory.text = product.category.name
        binding.tvSupplier.text = product.supplier.supplierName
        binding.tvUnit.text = product.unit
        binding.tvImportPrice.text = product.importPrice.toMoney()
        binding.tvSellPrice.text = product.sellPrice.toMoney()
        binding.tvStatus.text = product.getStatus()
        Glide.with(this).load(product.img_product).placeholder(R.drawable.img_default)
            .into(binding.imvImage)
    }
}