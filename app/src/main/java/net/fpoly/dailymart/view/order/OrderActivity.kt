package net.fpoly.dailymart.view.order

import android.Manifest
import net.fpoly.dailymart.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.view.SurfaceHolder
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityOrderBinding
import net.fpoly.dailymart.extension.setupSnackbar
import net.fpoly.dailymart.extension.view_extention.hideKeyboard
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity
import java.text.SimpleDateFormat
import java.util.*

class OrderActivity : BaseActivity<ActivityOrderBinding>(ActivityOrderBinding::inflate) {

    private val viewModel: OrderViewModel by viewModels { AppViewModelFactory }

    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private var barCodeScanner = false
    private lateinit var datePickerDialog: DatePickerDialog

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) setupScanner()
        }

    private val surfaceCallBack = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {}

        @SuppressLint("MissingPermission")
        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            try {
                if (isPermissionDenied()) return
                cameraSource.start(holder)
            } catch (ex: Exception) {
                viewModel.showSnackbar.postValue(AddInvoiceExportActivity.PERMISSION_DENIED)
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            cameraSource.stop()
        }
    }

    private val processor = object : Detector.Processor<Barcode> {
        override fun release() {}
        override fun receiveDetections(detections: Detector.Detections<Barcode>) {
            if (barCodeScanner && detections.detectedItems.isNotEmpty()) {
                val qrCode = detections.detectedItems
                val code = qrCode.valueAt(0)
                this@OrderActivity.hideKeyboard()
            }
        }
    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        detector = BarcodeDetector.Builder(this).build()
        cameraSource = CameraSource.Builder(this, detector)
            .setAutoFocusEnabled(true)
            .build()

        setupSnackbar()
        setupCheckPermission()
        setupBtnScan()
        setupSearchBarcode()
        setupCalenderPicker()
    }

    private fun setupCalenderPicker() {
        binding.edExpiry.setOnClickListener {
            val calendar = Calendar.getInstance()
            val theme = R.style.DatePickerTheme
            datePickerDialog = DatePickerDialog(this, theme, { _, year, month, dayOfMonth ->
                onSelectedDate(calendar, year, month, dayOfMonth)
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DATE])
            datePickerDialog.show()
        }
    }

    private fun onSelectedDate(calendar: Calendar, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month+1, dayOfMonth)
        val selectedDate =
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)
        binding.edExpiry.setText(selectedDate)
    }

    private fun setupBtnScan() {
        binding.openScanner.setOnClickListener {
            barCodeScanner = true
            binding.cardScan.visible()
        }
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    override fun setupObserver() {
        viewModel.currentProduct.observe(this) {
            binding.edName.setText(it.name)
            binding.edBarcode.setText(it.barcode)
            binding.edQuantity.setText("1")
        }
    }

    private fun setupCheckPermission() {
        when {
            isShouldShowRequest() -> {
                AddInvoiceExportActivity.RequirePermission(requestPermissionLauncher).show(
                    supportFragmentManager,
                    AddInvoiceExportActivity.TAG_DIALOG
                )
            }
            isPermissionDenied() -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                setupScanner()
            }
        }
    }

    private fun setupScanner() {
        binding.scannerZone.holder.addCallback(surfaceCallBack)
        detector.setProcessor(processor)
    }

    private fun isPermissionDenied(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_DENIED
    }

    private fun isShouldShowRequest(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.CAMERA
        )
    }

    private fun setupSearchBarcode() {
        viewModel.products.observe(this) { products ->
            val barcodes = products.map { it.barcode }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, barcodes)
            binding.edBarcode.setAdapter(adapter)
            binding.edBarcode.setOnItemClickListener { parent, _, position, _ ->
                viewModel.getProduct(parent.getItemAtPosition(position).toString())
                barCodeScanner = true
                this.hideKeyboard()
            }
        }
    }

}
