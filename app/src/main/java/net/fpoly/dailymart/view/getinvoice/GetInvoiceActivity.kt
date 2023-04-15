package net.fpoly.dailymart.view.getinvoice

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.gson.Gson
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.model.param.InvoiceParam
import net.fpoly.dailymart.databinding.ActivityGetInvoiceBinding
import net.fpoly.dailymart.extension.showSnackbar
import net.fpoly.dailymart.extension.view_extention.hideKeyboard
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity
import net.fpoly.dailymart.view.payment.PaymentActivity
import java.net.URLDecoder

class GetInvoiceActivity : AppCompatActivity() {

    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource

    private lateinit var binding: ActivityGetInvoiceBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) setupScanner()
        }
    private var scan = true

    private val surfaceCallBack = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {}

        @SuppressLint("MissingPermission")
        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            try {
                if (isPermissionDenied()) return
                cameraSource.start(holder)
            } catch (ex: Exception) {
                binding.root.showSnackbar(AddInvoiceExportActivity.PERMISSION_DENIED)

            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            cameraSource.stop()
        }
    }

    private val processor = object : Detector.Processor<Barcode> {
        override fun release() {}
        override fun receiveDetections(detections: Detector.Detections<Barcode>) {
            if (scan && detections.detectedItems.isNotEmpty()) {
                val qrCode = detections.detectedItems
                val code = qrCode.valueAt(0)
                binding.root.showSnackbar(code.displayValue)
                try {
                    val json = URLDecoder.decode(code.displayValue, "UTF-8")
                    val invoice = Gson().fromJson(json, InvoiceParam::class.java)
                    scan = false
                    Intent(this@GetInvoiceActivity, PaymentActivity::class.java ).also {
                        it.putExtra(AddInvoiceExportActivity.TAG_FINAL_INVOICE, invoice)
                        startActivity(it)
                        finish()
                    }
                }catch (ex: Exception) {
                    binding.root.showSnackbar("Mã QR không đúng")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.navigationBarColor = Color.WHITE
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            val navBarHeight = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            findViewById<View>(android.R.id.content).setPadding(0, 0, 0, navBarHeight)
            windowInsetsController.let {
                it.isAppearanceLightStatusBars = true
                it.isAppearanceLightNavigationBars = true
            }
            windowInsets
        }

        detector = BarcodeDetector.Builder(this).build()
        cameraSource = CameraSource.Builder(this, detector)
            .setAutoFocusEnabled(true)
            .build()

        binding.root.showSnackbar("Quét mã QR để nhận Hóa đơn")
        setupCheckPermission()
        setupBtnBack()
    }

    private fun setupBtnBack() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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

    class RequirePermission(private val requestPermission: ActivityResultLauncher<String>) :
        DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle(AddInvoiceExportActivity.DIALOG_TITLE)
                    setMessage(AddInvoiceExportActivity.DIALOG_MESSAGE).setPositiveButton(
                        AddInvoiceExportActivity.DIALOG_ACCEPT
                    ) { dialog, _ ->
                        requestPermission.launch(Manifest.permission.CAMERA)
                        dialog.dismiss()
                    }
                    setNegativeButton(AddInvoiceExportActivity.DIALOG_CANCEL) { dialog, _ ->
                        dialog.dismiss()
                    }
                }.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}