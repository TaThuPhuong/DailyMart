package net.fpoly.dailymart.view.pay

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
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

class AddInvoiceExportActivity : BaseActivity<ActivityPayBinding>(ActivityPayBinding::inflate) {

    private val viewModel: AddInvoiceExportViewModel by viewModels { AppViewModelFactory }
    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                setupScanner()
            }
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
            if (detections.detectedItems.isNotEmpty()) {
                val qrCode = detections.detectedItems
                val code = qrCode.valueAt(0)
                viewModel.showSnackbar.postValue(code.displayValue)
            }
        }

    }

    override fun setupData() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupShowSnackbar()
        setupCheckPermission()

    }

    private fun setupShowSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    private fun setupCheckPermission() {
        when {
            isPermissionDenied() -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            isShouldShowRequest() -> {
                RequirePermission().show(supportFragmentManager, TAG_DIALOG)
            }
            else -> {
                setupScanner()
            }
        }
    }

    private fun setupScanner() {
        detector = BarcodeDetector.Builder(this).build()
        cameraSource = CameraSource.Builder(this, detector)
            .setAutoFocusEnabled(true)
            .build()
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

    inner class RequirePermission : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle(DIALOG_TITLE)
                    setMessage(DIALOG_MESSAGE).setPositiveButton(DIALOG_ACCEPT) { dialog, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
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
        const val TAG_DIALOG = "TAG1"
        const val DIALOG_TITLE = "Cấp quyền truy cập Camera"
        const val DIALOG_MESSAGE = "Daily Mart cần quyền truy cập Camera để quét mã Barcode"
        const val DIALOG_ACCEPT = "Cấp quyền"
        const val DIALOG_CANCEL = "Hủy bỏ"
        const val PERMISSION_DENIED = "Quyền Camera bị từ chối"


    }
}