package net.fpoly.dailymart.view.pay

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import androidx.fragment.app.DialogFragment
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityPayBinding
import net.fpoly.dailymart.extension.setupSnackbar
import net.fpoly.dailymart.view.tab.invoice.InvoiceProductAdapter

class AddInvoiceExportActivity : BaseActivity<ActivityPayBinding>(ActivityPayBinding::inflate) {

    private val viewModel: AddInvoiceExportViewModel by viewModels { AppViewModelFactory }
    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private var barCodeScanner = true
    private lateinit var invoiceAdapter: InvoiceProductAdapter

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
                viewModel.showSnackbar.postValue(PERMISSION_DENIED)
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
                viewModel.getInvoiceDetail(code.displayValue)
                viewModel.showSnackbar.postValue(code.displayValue)
                barCodeScanner = false
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

        setupShowSnackbar()
        setupCheckPermission()
        setupBtnPayment()
        setupSearchBarcode()
        setupListInvoiceDetail()
    }

    private fun setupListInvoiceDetail() {
        invoiceAdapter = InvoiceProductAdapter(viewModel)
        binding.listInvoiceDetail.adapter = invoiceAdapter
    }

    private fun setupSearchBarcode() {
        viewModel.listProductName.observe(this) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, it)
            binding.edSearchBarcode.setAdapter(adapter)
            binding.edSearchBarcode.setOnItemClickListener { parent, view, position, id ->
                viewModel.getInvoiceDetail(parent.getItemAtPosition(position).toString())
            }
        }
    }

    private fun setupBtnPayment() {
        binding.btnPayment.setOnClickListener {
            setupScanner()
        }
    }

    private fun setupShowSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    private fun setupCheckPermission() {
        when {
            isShouldShowRequest() -> {
                RequirePermission(requestPermissionLauncher).show(
                    supportFragmentManager,
                    TAG_DIALOG
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

    override fun setupObserver() {

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

    class RequirePermission(private val requestPermission: ActivityResultLauncher<String>) :
        DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle(DIALOG_TITLE)
                    setMessage(DIALOG_MESSAGE).setPositiveButton(DIALOG_ACCEPT) { dialog, _ ->
                        requestPermission.launch(Manifest.permission.CAMERA)
                        dialog.dismiss()
                    }
                    setNegativeButton(DIALOG_CANCEL) { dialog, _ ->
                        dialog.dismiss()
                    }
                }.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    companion object {
        const val TAG = "TAG11"
        const val TAG_DIALOG = "TAG1"

        const val DIALOG_TITLE = "Cấp quyền truy cập Camera"
        const val DIALOG_MESSAGE = "Daily Mart cần quyền truy cập Camera để quét mã Barcode"

        const val DIALOG_ACCEPT = "Cấp quyền"
        const val DIALOG_CANCEL = "Hủy bỏ"

        const val PERMISSION_DENIED = "Quyền Camera bị từ chối"


    }
}