package net.fpoly.dailymart.view.report

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.data.repository.ReportRepositoryImpl
import net.fpoly.dailymart.repository.ReportRepository
import net.fpoly.dailymart.utils.SharedPref

class ReportViewModel(context: Context) : ViewModel() {
    private val TAG = "ReportViewModel"
    private val reportRepository: ReportRepository = ReportRepositoryImpl()
    private val _listImport = MutableLiveData<ReportDataByMonth?>()
    val listImport: LiveData<ReportDataByMonth?> = _listImport
    private val _listExport = MutableLiveData<ReportDataByMonth?>()
    val listExport: LiveData<ReportDataByMonth?> = _listExport
    private val _listRevenue = MutableLiveData<ReportDataByMonth?>()
    val listRevenue: LiveData<ReportDataByMonth?> = _listRevenue
    private val _totalRevenue = MutableLiveData<ReportResponse?>()
    val totalRevenue: LiveData<ReportResponse?> = _totalRevenue
    private val _totalImport = MutableLiveData<Long?>()
    val totalImport: LiveData<Long?> = _totalImport
    private val _totalExport = MutableLiveData<Long?>()
    val totalExport: LiveData<Long?> = _totalExport
    private val _quantityImport = MutableLiveData<Long?>()
    val quantityImport: LiveData<Long?> = _quantityImport
    private val _quantityExport = MutableLiveData<Long?>()
    val quantityExport: LiveData<Long?> = _quantityExport
    private lateinit var mLoadingDialog: LoadingDialog
    private val token = SharedPref.getAccessToken(context)
    val showSnackbar = MutableLiveData<String>()

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context = context)
    }

    fun getReportImportByMonth(month: Int) {
        viewModelScope.launch {
            when (val res = reportRepository.getReportByMonth(token, month)) {
                is Response.Success -> {
                    _listImport.postValue(res.data)
                    _totalImport.postValue(res.data.totalMonth)
                    Log.d(TAG, "getReportImportByMonth: res: $res")
                }
                is Response.Error -> {
                    Log.d(TAG, "getReportImportByMonth: error: ${res.message}")
//                    showSnackbar.postValue(res.message)
                }
            }
        }
    }

    fun showDialogFilter(context: Context) {
        FilterDialog(context, this).show()
    }
}
