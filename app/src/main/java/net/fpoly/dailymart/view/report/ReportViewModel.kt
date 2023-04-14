package net.fpoly.dailymart.view.report

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.data.repository.ReportRepositoryImpl
import net.fpoly.dailymart.repository.ReportRepository
import net.fpoly.dailymart.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback

class ReportViewModel(context: Context) : ViewModel() {
    private val TAG = "ReportViewModel"
    private val reportRepository: ReportRepository = ReportRepositoryImpl()
    private val _listRevenueByYear = MutableLiveData<List<ReportDataByMonthInYear?>>()
    val listRevenueByYear: LiveData<List<ReportDataByMonthInYear?>> = _listRevenueByYear
    private val _listRevenueByMonth = MutableLiveData<List<ReportDataByDayInMonth?>>()
    val listRevenueByMonth: LiveData<List<ReportDataByDayInMonth?>> = _listRevenueByMonth
    private val _revenueByDay = MutableLiveData<ReportDataByDay>()
    val revenueByDay: LiveData<ReportDataByDay> = _revenueByDay
    private val _importByDay = MutableLiveData<ReportDataByDay>()
    val importByDay: LiveData<ReportDataByDay> = _importByDay
    private val _exportByDay = MutableLiveData<ReportDataByDay>()
    val exportByDay: LiveData<ReportDataByDay> = _exportByDay
    private val _totalRevenue = MutableLiveData<Long>()
    val totalRevenue: LiveData<Long> = _totalRevenue
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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun getRevenueByMonth(month: Int) {
        viewModelScope.launch {
            mLoadingDialog.showLoading()
            when(val res = reportRepository.getReportByMonth(token, month)){
                is Response.Success -> {
                    _listRevenueByMonth.value = res.data.totalByDay
                    _totalImport.value = res.data.totalImport
                    _totalExport.value = res.data.totalExport
                    _totalRevenue.value = res.data.revenue
                    _quantityExport.value = res.data.quantityExport
                    _quantityImport.value = res.data.quantityImport
                    mLoadingDialog.hideLoading()
                }
                is Response.Error -> Log.e(TAG, "getRevenueByMonth: err: ${res.message}", )
            }
        }
    }

    fun getRevenueByYear(year: Int) {
        viewModelScope.launch {
            mLoadingDialog.showLoading()
            when (val res = reportRepository.getReportByYear(token, year)) {
                is Response.Success -> {
                    _totalRevenue.value = res.data.revenue
                    _totalImport.value = res.data.totalImport
                    _totalExport.value = res.data.totalExport
                    _quantityImport.value = res.data.quantityImport
                    _quantityExport.value = res.data.quantityExport
                    _listRevenueByYear.value = res.data.totalByMonth
                    mLoadingDialog.hideLoading()
                }
                is Response.Error -> Log.e(TAG, "getRevenueByDate: err: ${res.message}")
            }
        }
    }

    fun getRevenueByDate(date: String) {
        viewModelScope.launch {
            mLoadingDialog.showLoading()
            when (val res = reportRepository.getReportByDay(token, date)) {
                is Response.Success -> {
                    _totalRevenue.value = res.data.revenue
                    _totalImport.value = res.data.totalImport
                    _totalExport.value = res.data.totalExport
                    _quantityImport.value = res.data.quantityImport
                    _quantityExport.value = res.data.quantityExport
                    mLoadingDialog.hideLoading()
                }
                is Response.Error -> Log.e(TAG, "getRevenueByDate: err: ${res.message}")
            }
        }
    }
}
