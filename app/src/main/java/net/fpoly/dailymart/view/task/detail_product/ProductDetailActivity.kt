package net.fpoly.dailymart.view.task.detail_product

import android.annotation.SuppressLint
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ActivityProductDetailBinding
import net.fpoly.dailymart.utils.Constant
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
        binding.imvBack.setOnClickListener { finish() }
        binding.btnClose.setOnClickListener { finish() }
    }

    override fun setupObserver() {

    }

    @SuppressLint("SetTextI18n")
    private fun setData(product: Product) {
        binding.tvId.text = "Barcode: ${product.barcode}"
        binding.tvName.text = "Tên: ${product.name}"
        binding.tvCategory.text = "Loại hàng: ${product.category.name}"
        binding.tvSupplier.text = "Nhà CC: ${product.supplier.supplierName}"
        binding.tvUnit.text = "ĐVT: ${product.unit}"
        binding.tvImportPrice.text = "Nhập: ${product.importPrice.toMoney()}"
        binding.tvSellPrice.text = "Bán: ${product.sellPrice.toMoney()}"
        Glide.with(this).load(product.img_product).placeholder(R.drawable.img_default)
            .into(binding.imvImage)
    }
}