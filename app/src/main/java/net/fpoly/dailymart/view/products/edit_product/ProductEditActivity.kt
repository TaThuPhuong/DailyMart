package net.fpoly.dailymart.view.products.edit_product

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.bumptech.glide.Glide
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.ActivityEditProductBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.*
import net.fpoly.dailymart.firbase.storege.Images
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ImagesUtils
import net.fpoly.dailymart.view.products.add_product.ChoseCategoryDialog
import net.fpoly.dailymart.view.products.add_product.ChoseSupplierDialog
import net.fpoly.dailymart.view.products.add_product.ProductEvent

class ProductEditActivity :
    BaseActivity<ActivityEditProductBinding>(ActivityEditProductBinding::inflate),
    View.OnClickListener {

    private val viewModel: ProductEditViewModel by viewModels { AppViewModelFactory }

    private var mProduct: Product? = null

    private var mListCategory: List<Category> = ArrayList()
    private var mListSupplier: List<Supplier> = ArrayList()

    private lateinit var codeScanner: CodeScanner

    private var onImageChange = false

    private var mLoadingDialog: LoadingDialog? = null
    override fun setOnClickListener() {
        super.setOnClickListener()
        binding.btnBack.setOnClickListener(this)
        binding.imvScan.setOnClickListener(this)
        binding.tvCategory.setOnClickListener(this)
        binding.tvSupplier.setOnClickListener(this)
        binding.imvImage.setOnClickListener(this)
        binding.btnAddProduct.setOnClickListener(this)
    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        mProduct = intent.getSerializableExtra(Constant.PRODUCT) as Product
        mProduct?.let {
            setData(it)
        }
        setEditTextChange()
        checkPermission()
        mLoadingDialog = LoadingDialog(this)
    }

    override fun setupObserver() {
        viewModel.actionSuccess.observe(this) {
            mLoadingDialog?.hideLoading()
            if (it) {
                finish()
            }
        }
        viewModel.message.observe(this) {
            if (it.isNotBlank()) {
                showToast(this, it)
            }
        }
        viewModel.listCategory.observe(this) {
            mListCategory = it
        }
        viewModel.listSupplier.observe(this) {
            mListSupplier = it
        }
    }

    private fun setData(product: Product) {
        binding.edId.setText(product.barcode)
        binding.edName.setText(product.name)
        binding.tvCategory.text = product.category.name
        binding.tvSupplier.text = product.supplier.supplierName
        binding.edImportPrice.setText(product.importPrice.toString())
        binding.edSellPrice.setText(product.sellPrice.toString())
        binding.edUnit.setText(product.unit)
        Glide.with(this).load(product.img_product).placeholder(R.drawable.img_default)
            .into(binding.imvImage)
        viewModel.setProduct(product)

    }

    override fun onClick(view: View) {
        when (view) {
            binding.btnBack -> finish()
            binding.imvScan -> {
                if (binding.cvScanner.isShowing()) {
                    binding.cvScanner.gone()
                } else {
                    binding.cvScanner.visible()
                }
            }
            binding.tvCategory -> {
                if (mListCategory.isNotEmpty()) {
                    ChoseCategoryDialog(this, mListCategory) {
                        viewModel.onEvent(ProductEvent.IdCategoryChange(it.id))
                        binding.tvCategory.text = it.name
                    }.show()
                }
            }
            binding.tvSupplier -> {
                if (mListSupplier.isNotEmpty()) {
                    ChoseSupplierDialog(this, mListSupplier) {
                        viewModel.onEvent(ProductEvent.IdSupplierChange(it.id))
                        binding.tvSupplier.text = it.supplierName
                    }.show()
                }
            }
            binding.imvImage -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkPermissionImage()
                } else {
                    ImagesUtils.checkPermissionPickImage(this, binding.imvImage) {
                        onImageChange = true
                    }
                }
            }
            binding.btnAddProduct -> {
                if (onImageChange) {
                    mLoadingDialog?.showLoading()
                    Images.uploadImage(binding.imvImage, Product.TABLE_NAME, mProduct?.barcode!!,
                        onSuccess = {
                            viewModel.onEvent(ProductEvent.AddProduct(it))
                        }, onFail = {
                            mProduct?.img_product?.let {
                                viewModel.onEvent(ProductEvent.AddProduct(it))
                            }
                        })
                } else {
                    mProduct?.img_product?.let {
                        viewModel.onEvent(ProductEvent.AddProduct(it))
                    }
                }
            }
        }
    }

    private fun setEditTextChange() {
        binding.edId.getTextOnChange {
            viewModel.onEvent(ProductEvent.IdChange(it))
        }
        binding.edName.getTextOnChange {
            viewModel.onEvent(ProductEvent.NameChange(it))
        }
        binding.edImportPrice.getTextOnChange {
            viewModel.onEvent(ProductEvent.ImportPriceChange(it))
        }
        binding.edSellPrice.getTextOnChange {
            viewModel.onEvent(ProductEvent.SellPriceChange(it))
        }
        binding.edUnit.getTextOnChange {
            viewModel.onEvent(ProductEvent.UnitChange(it))
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        } else {
            startScanning()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissionImage() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                2
            )
        } else {
            ImagesUtils.openImagesPicker(this, binding.imvImage) {
                onImageChange = true
            }
        }
    }

    private fun startScanning() {
        codeScanner = CodeScanner(this, binding.scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                binding.edId.setText(it.text)
                codeScanner.startPreview()
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Result${it.message}", Toast.LENGTH_LONG).show()
                binding.cvScanner.gone()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning()
            } else {
                Toast.makeText(this, "Chưa có quyền", Toast.LENGTH_LONG).show()
            }
        }
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImagesUtils.openImagesPicker(this, binding.imvImage) {
                    onImageChange = true
                }
            } else {
                Toast.makeText(this, "Vui lòng câps quyền", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }
}