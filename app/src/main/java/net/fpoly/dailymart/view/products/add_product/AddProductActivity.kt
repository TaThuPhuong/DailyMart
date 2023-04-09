package net.fpoly.dailymart.view.products.add_product

import android.Manifest
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.bumptech.glide.Glide
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.R
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Category
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.data.model.Supplier
import net.fpoly.dailymart.databinding.ActivityAddProductBinding
import net.fpoly.dailymart.extension.showToast
import net.fpoly.dailymart.extension.view_extention.getTextOnChange
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.hide
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.firbase.storege.Images
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.ImagesUtils

class AddProductActivity :
    BaseActivity<ActivityAddProductBinding>(ActivityAddProductBinding::inflate),
    View.OnClickListener {

    private val viewModel by viewModels<AddProductViewModel> { AppViewModelFactory }

    private var id = ""

    private var mListCategory: List<Category> = ArrayList()
    private var mListSupplier: List<Supplier> = ArrayList()

    private lateinit var codeScanner: CodeScanner

    override fun setOnClickListener() {
        super.setOnClickListener()
        binding.imvBack.setOnClickListener(this)
        binding.imvScan.setOnClickListener(this)
        binding.tvCategory.setOnClickListener(this)
        binding.tvSupplier.setOnClickListener(this)
        binding.imvAddImage.setOnClickListener(this)
        binding.imvImage.setOnClickListener(this)
        binding.btnAddProduct.setOnClickListener(this)
    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setEditTextChange()
    }

    override fun setupObserver() {
        viewModel.actionSuccess.observe(this) {
            if (it) {
                resetLayout()
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

    override fun onClick(v: View?) {
        when (v) {
            binding.imvBack -> finish()
            binding.imvScan -> {
                binding.cvScanner.visible()
                checkPermission()
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
            binding.imvAddImage,binding.imvImage -> {
                ImagesUtils.checkPermissionPickImage(this, binding.imvImage) {
                    binding.imvAddImage.hide()
                }
            }
            binding.btnAddProduct -> {
                Images.uploadImage(binding.imvImage, Product.TABLE_NAME, id,
                    onSuccess = {
                        viewModel.onEvent(ProductEvent.AddProduct(it))
                    }, onFail = {
                        viewModel.onEvent(ProductEvent.AddProduct(Constant.IMAGE_DEFAULT))
                    })
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

    private fun resetLayout() {
        binding.edId.setText("")
        binding.edName.setText("")
        binding.edUnit.setText("")
        binding.edSellPrice.setText("")
        binding.edImportPrice.setText("")
        binding.tvSupplier.text = ""
        binding.tvCategory.text = ""
        binding.imvAddImage.visible()
        Glide.with(this).load(R.drawable.img_default).into(binding.imvImage)
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

    private fun startScanning() {
        codeScanner = CodeScanner(this, binding.scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false
        codeScanner.decodeCallback = DecodeCallback {
            id = it.text
            binding.edId.setText(it.text)
            binding.cvScanner.gone()
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
                Toast.makeText(this, "Đã cấp quyền", Toast.LENGTH_LONG).show()
                startScanning()
            } else {
                Toast.makeText(this, "Chưa có quyền", Toast.LENGTH_LONG).show()
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