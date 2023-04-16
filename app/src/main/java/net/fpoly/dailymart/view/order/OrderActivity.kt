package net.fpoly.dailymart.view.order

import android.Manifest
import net.fpoly.dailymart.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.SurfaceHolder
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import androidx.core.view.isVisible
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import net.fpoly.dailymart.AppViewModelFactory
import net.fpoly.dailymart.base.BaseActivity
import net.fpoly.dailymart.databinding.ActivityOrderBinding
import net.fpoly.dailymart.extension.setupSnackbar
import net.fpoly.dailymart.extension.view_extention.gone
import net.fpoly.dailymart.extension.view_extention.hideKeyboard
import net.fpoly.dailymart.extension.view_extention.visible
import net.fpoly.dailymart.view.main.MainActivity
import net.fpoly.dailymart.view.pay.AddInvoiceExportActivity
import net.fpoly.dailymart.view.payment.PaymentActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderActivity : BaseActivity<ActivityOrderBinding>(ActivityOrderBinding::inflate) {

    private val viewModel: OrderViewModel by viewModels { AppViewModelFactory }

    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private var barCodeScanner = false
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var adapter: OrderAdapter

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
                viewModel.getProduct(code.displayValue)
                barCodeScanner = true
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

        setupBtnBack()
        setupSnackbar()
        setupCheckPermission()
        setupBtnScan()
        setupSearchBarcode()
        setupCalenderPicker()
        setupOrderList()
    }

    private fun setupBtnBack() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupOrderList() {
        adapter = OrderAdapter(this, viewModel)
        binding.listProducts.adapter = adapter
        viewModel.invoiceProducts.observe(this) {
            barCodeScanner = true
            val new = it.toMutableList()
            adapter.submitList(new)
            this.hideKeyboard()
        }
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
        calendar.set(year, month + 1, dayOfMonth)
        val selectedDate =
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)
        binding.edExpiry.setText(selectedDate)
    }

    private fun setupBtnScan() {
        binding.openScanner.setOnClickListener {
            if (binding.cardScan.isVisible) {
                binding.cardScan.gone()
                return@setOnClickListener
            }
            barCodeScanner = true
            binding.cardScan.visible()
        }
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(this, viewModel.showSnackbar)
    }

    override fun setupObserver() {
        viewModel.currentProduct.observe(this) {
            binding.edBarcode.setText(it.barcode)
            binding.edUnitPrice.setText(it.importPrice.toString())
            binding.edName.setText(it.name)
            binding.edQuantity.setText("1")
        }
        viewModel.eventDone.observe(this) {
            Intent(this, MainActivity::class.java).also {
                it.putExtra(MainActivity.MAIN_EVENT, PaymentActivity.NEW_INVOICE_CREATE)
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
            }
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
        viewModel.products.observe(this) { list ->
            val barcodes = list.map { it.barcode }.toList()

            val adapter = FilterAdapter(this, barcodes)
            binding.edBarcode.setAdapter(adapter)
            binding.edBarcode.setOnItemClickListener { parent, _, position, _ ->
                viewModel.getProduct(parent.getItemAtPosition(position).toString())
                this.hideKeyboard()
            }
        }
    }

    class FilterAdapter(context: Context, private val list: List<String>) :
        ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list) {

        private val listRoot = ArrayList(list)

        override fun getFilter(): Filter {
            return filterCustom()
        }

        private fun filterCustom(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val filterResults = FilterResults()
                    val results = ArrayList<String>()
                    if (constraint.isNullOrEmpty()) {
                        results.addAll(listRoot)
                    } else {
                        for (item in list) {
                            if (item.contains(constraint.toString())) {
                                results.add(item)
                            }
                        }
                    }
                    filterResults.values = results
                    filterResults.count = results.size
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    clear()
                    if (results != null && results.count > 0) {
                        setNotifyOnChange(false)
                        addAll(results.values as MutableList<String>)
                        notifyDataSetChanged()
                    } else {
                        notifyDataSetInvalidated()
                    }
                }

                override fun convertResultToString(resultValue: Any?): CharSequence {
                    return resultValue as String
                }
            }
        }
    }
}

