package net.fpoly.dailymart.view.order

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import gun0912.tedimagepicker.util.ToastUtil
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.data.database.OrderInfo
import net.fpoly.dailymart.data.model.param.OrderParam
import net.fpoly.dailymart.data.model.param.ProductByOrder
import net.fpoly.dailymart.databinding.ActivityOrderBinding
import net.fpoly.dailymart.extention.view_extention.gone
import net.fpoly.dailymart.extention.view_extention.visible

class OrderActivity :
    BaseActivity<ActivityOrderBinding>(ActivityOrderBinding::inflate),
    View.OnClickListener {

    private val viewModel: OrderViewModel by viewModels { AppViewModelFactory }
    private lateinit var mOrderAdapter: OrderAdapter
    private var mListOrder: List<OrderInfo> = ArrayList()
    private val TAG = "OrderActivity"
    private lateinit var codeScanner: CodeScanner
    private var token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoibWFuYWdlciIsImlhdCI6MTY4MDM2MzY5MCwiZXhwIjoxNjgwNDUwMDkwfQ.WXpnhDjUdkI_JQyTSQ9Jyz2QWaX6fN3G57YQF8HDrc8"

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
//        mOrderAdapter = OrderAdapter(this, mListOrder)
//        binding.rcvImportProduct.adapter = mOrderAdapter
        viewModel.getOrders(token)
    }

    override fun setupObserver() {
        viewModel.listOrder.observe(this) {
            Log.d(TAG, "setupObserver: $it")
            if (it != null) {
                it.data.forEach {
                    it.invoiceDetails.forEach {
                        viewModel.getProduct(it.product, token)
                        viewModel.product.observe(this) {
                            Log.d(TAG, "setupObserver: product $it")
                        }
                    }
                }
            }
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        } else {
            startScanning()
        }
    }

    private fun newOrder() {
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
//        viewModel.getAllOrder()
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

    override fun setOnClickListener() {
        super.setOnClickListener()
        binding.imvScan.setOnClickListener(this)
        binding.btnAdd.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imvScan -> {
                binding.cvScanner.visible()
                checkPermission()
            }
            binding.btnAdd -> {
                val idProduct = binding.edId.text.toString()
                val quantity = Integer.parseInt(binding.edQuantity.text.toString())
                val expiryDate = binding.edExpiryDate.toString()
                val product = ProductByOrder(idProduct, 2000, quantity, expiryDate)
                val order = OrderParam("640c20d2151e0d8e2339166b", listOf(product), "IMPORT", expiryDate)
                if (viewModel.insertOrder(order, token) != null) {
                    binding.edId.setText("")
                    binding.edQuantity.setText("")
                    binding.edExpiryDate.setText("")
                    ToastUtil.showToast("Insert new order successfully")
                } else {
                    ToastUtil.showToast("Insert order failed")
                }
            }
        }
    }
}
