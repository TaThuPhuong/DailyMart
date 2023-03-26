package net.fpoly.dailymart.view.add_product

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
import com.google.android.material.snackbar.Snackbar
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.model.Product
import net.fpoly.dailymart.databinding.ActivityAddProductBinding
import net.fpoly.dailymart.extention.view_extention.getTextOnChange
import net.fpoly.dailymart.extention.view_extention.gone
import net.fpoly.dailymart.extention.view_extention.visible
import net.fpoly.dailymart.firbase.storege.Images
import net.fpoly.dailymart.utils.ImagesUtils

class AddProductActivity :
    BaseActivity<ActivityAddProductBinding>(ActivityAddProductBinding::inflate),
    View.OnClickListener {

    private val viewModel by viewModels<AddProductViewModel> { AppViewModelFactory }

    private val _id = MutableLiveData("")

    private lateinit var codeScanner: CodeScanner

    override fun setOnClickListener() {
        super.setOnClickListener()
        binding.imvBack.setOnClickListener(this)
        binding.imvScan.setOnClickListener(this)
        binding.tvCategory.setOnClickListener(this)
        binding.imvImage.setOnClickListener(this)
        binding.btnAddProduct.setOnClickListener(this)
    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setEditTextChange()
    }

    override fun setupObserver() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imvBack -> finish()
            binding.imvScan -> {
                binding.cvScanner.visible()
                checkPermission()
            }
            binding.tvCategory -> {}
            binding.imvImage -> {
                ImagesUtils.checkPermissionPickImage(this, binding.imvImage)
            }
            binding.btnAddProduct -> {
                Images.uploadImage(binding.imvImage, Product.TABLE_NAME, _id.value!!) {
                    viewModel.onEvent(ProductEvent.AddProduct(it))
                }
            }
        }
    }

    private fun setEditTextChange() {
        binding.edId.getTextOnChange {
            viewModel.onEvent(ProductEvent.IdChange(it))
            _id.value = it
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