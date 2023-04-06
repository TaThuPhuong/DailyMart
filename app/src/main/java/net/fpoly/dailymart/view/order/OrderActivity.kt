package net.fpoly.dailymart.view.order

import android.Manifest
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextWatcher
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
import net.fpoly.dailymart.data.model.OrderResponse
import net.fpoly.dailymart.data.model.param.OrderParam
import net.fpoly.dailymart.data.model.param.ProductByOrder
import net.fpoly.dailymart.databinding.ActivityOrderBinding
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.visible
import java.util.*
import kotlin.collections.ArrayList

class OrderActivity :
    BaseActivity<ActivityOrderBinding>(ActivityOrderBinding::inflate),
    View.OnClickListener {

    private val viewModel: OrderViewModel by viewModels { AppViewModelFactory }
    private lateinit var mOrderAdapter: OrderAdapter
    private var mListOrder = ArrayList<OrderResponse>()
    private val TAG = "OrderActivity"
    private lateinit var codeScanner: CodeScanner
    private var productName = ""
    private var token =
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoibWFuYWdlciIsImlhdCI6MTY4MDYyMzk5NCwiZXhwIjoxNzY2OTM3NTk0fQ.W6G-sBuJ_ySLD-iL-9e9dW8OjUHrHCd0AqMlO35XI6M"

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.initLoadDialog(this)
        viewModel.getOrders(token)
        viewModel.getAllProduct(token)
        setupProductName()
        setupExpiryDate()
    }

    override fun setupObserver() {
//        viewModel.listOrder.observe(this) { listOrder ->
//            Log.d(TAG, "setupObserver: listorder: ${listOrder.data}")
// //            listOrder?.data?.forEach { orderResponse ->
// //                Log.d(TAG, "setupObserver: orderresponse: ${orderResponse.invoiceDetails}")
// //                orderResponse.invoiceDetails.forEach { invoiceDetails ->
// //                    viewModel.getProduct(invoiceDetails.product, token)
// //                    viewModel.product.observe(this) { product ->
// //                        if (product == null) {
// //                            val orderInfo = OrderInfo(
// //                                "null",
// //                                0,
// //                                "null",
// //                            )
// //                            mListOrder.add(orderInfo)
// //                        } else {
// //                            val orderInfo = OrderInfo(
// //                                product.data.productName,
// //                                invoiceDetails.quantity,
// //                                orderResponse.dateCreated,
// //                            )
// //                            mListOrder.add(orderInfo)
// //                            mOrderAdapter = OrderAdapter(this, mListOrder)
// //                            binding.rcvImportProduct.adapter = mOrderAdapter
// //                            Log.d(TAG, "setupObserver: mlistorderINfo: $mListOrder")
// //                            Log.d(TAG, "setupObserver: product: ${product.data}")
// //                        }
// //                    }
// //                }
// //                if (product == null) {
// //                    val orderInfo = OrderInfo(
// //                        "null",
// //                        0,
// //                        "null",
// //                    )
// //                    mListOrder.add(orderInfo)
// //                } else {
// //                    val orderInfo = OrderInfo(
// //                        product.data.productName,
// //                        invoiceDetails.quantity,
// //                        orderResponse.dateCreated,
// //                    )
// //                    mListOrder.add(orderInfo)
// //                    mOrderAdapter = OrderAdapter(this, mListOrder)
// //                    binding.rcvImportProduct.adapter = mOrderAdapter
// //                    Log.d(TAG, "setupObserver: mlistorderINfo: $mListOrder")
// //                    Log.d(TAG, "setupObserver: product: ${product.data}")
// //                }
// //            mListOrder = listOrder.data
// //            Log.d(TAG, "setupObserver: mlistorder: $mListOrder")
//
//        }
        if (mListOrder.isEmpty()) {
            binding.imgListEmpty.visible()
        }

        viewModel.product.observe(this) {
            if (it != null) {
                productName = it.data.productName
                binding.tvName.text = it.data.productName
            } else {
                binding.tvName.text = ""
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
                val quantity = binding.edQuantity.text.toString()
                val expiryDate = binding.edExpiryDate.toString()
                if (validate()) {
                    val product =
                        ProductByOrder(idProduct, 2000, Integer.parseInt(quantity), expiryDate)
                    val order =
                        OrderParam(
                            "640c20d2151e0d8e2339166b",
                            listOf(product),
                            "IMPORT",
                            expiryDate,
                        )
                    viewModel.insertOrder(order, token)
                    viewModel.newOrder.observe(this) {
                        if (it != null) {
                            binding.edId.setText("")
                            binding.edQuantity.setText("")
                            binding.edExpiryDate.setText("")
                            binding.tvName.text = ""
                            ToastUtil.showToast("Insert new order successfully")
                        } else {
                            ToastUtil.showToast("Insert order failed")
                        }
                    }
                }
            }
        }
    }

    private fun setupProductName() {
        binding.edId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG, "onTextChanged: $s")
                viewModel.getProduct(s.toString().trim(), token)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun setupExpiryDate() {
        binding.edExpiryDate.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val ddmmyyyy = "DDMMYYYY"
            private val cal = Calendar.getInstance()
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    var clean = s.toString().replace("[^\\d.]".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]".toRegex(), "")
                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    // Fix for pressing delete next to a forward slash
                    if (clean == cleanC) sel--
                    if (clean.length < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length)
                    } else {
                        // This part makes sure that when we finish entering numbers
                        // the date is correct, fixing it otherwise
                        var day = clean.substring(0, 2).toInt()
                        var mon = clean.substring(2, 4).toInt()
                        var year = clean.substring(4, 8).toInt()
                        if (mon > 12) mon = 12
                        cal[Calendar.MONTH] = mon - 1
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal[Calendar.YEAR] = year
                        // ^ first set year for the line below to work correctly
                        // with leap years - otherwise, date e.g. 29/02/2012
                        // would be automatically corrected to 28/02/2012
                        day = if (day > cal.getActualMaximum(Calendar.DATE)) {
                            cal.getActualMaximum(
                                Calendar.DATE,
                            )
                        } else {
                            day
                        }
                        clean = String.format("%02d%02d%02d", day, mon, year)
                    }
                    clean = String.format(
                        "%s-%s-%s",
                        clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8),
                    )
                    sel = if (sel < 0) 0 else sel
                    current = clean
                    binding.edExpiryDate.setText(current)
                    binding.edExpiryDate.setSelection(if (sel < current.length) sel else current.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun validate(): Boolean {
        if (binding.edId.text.isEmpty()) {
            binding.edId.error = "Mã sản phẩm không được để trống"
            return false
        }
        if (binding.edQuantity.text.toString().isEmpty()) {
            binding.edQuantity.error = "Số lượng không được để trống"
            return false
        }
        if (binding.edExpiryDate.text.isEmpty()) {
            binding.edExpiryDate.error = "Hạn sử dụng không được để trống"
            return false
        }
        if (productName.isEmpty()) {
            binding.edId.error = "Mã sản phẩm không tồn tại"
            return false
        }
        return true
    }
}
