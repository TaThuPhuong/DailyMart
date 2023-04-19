package net.fpoly.dailymart.view.report

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.repository.ReportRepository
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.utils.toMoney
import java.text.SimpleDateFormat
import java.util.Calendar

class ReportViewModel(context: Context, private val reportRepository: ReportRepository) :
    ViewModel() {

    private val TAG = "YinMing"
    val listRevenueByMonth = MutableLiveData<List<ReportDayData>>(ArrayList())
    val listRevenueByYear = MutableLiveData<List<ReportMonthData>>(ArrayList())

    val totalRevenue = MutableLiveData("0 vnđ")
    val totalImport = MutableLiveData("0 vnđ")
    val totalExport = MutableLiveData("0 vnđ")
    val quantityImport = MutableLiveData("0")
    val quantityExport = MutableLiveData("0")
    val onGetDone = MutableLiveData(false)


    private val mToken = SharedPref.getAccessToken(context)
    private val mCalender = Calendar.getInstance()

    val timeReport = MutableLiveData("")

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    init {
        timeReport.value = mCalender.timeInMillis.date2String()
        getRevenueByMonth(mCalender.timeInMillis)
    }

    fun getRevenueByMonth(time: Long) {
        mCalender.timeInMillis = time
        timeReport.value = time.date2String("MM/yyyy")
        viewModelScope.launch {
            when (val res =
                reportRepository.getReportByMonth(
                    mToken,
                    mCalender[Calendar.MONTH] + 1,
                    mCalender[Calendar.YEAR]
                )) {
                is Response.Success -> {
                    listRevenueByMonth.postValue(res.data.listData)
                    totalRevenue.postValue(res.data.revenue.toMoney())
                    totalImport.postValue(res.data.totalImport.toMoney())
                    totalExport.postValue(res.data.totalExport.toMoney())
                    quantityImport.postValue(res.data.quantityImport.toString())
                    quantityExport.postValue(res.data.quantityExport.toString())
                    onGetDone.postValue(true)
                }
                is Response.Error -> {
                    Log.e(TAG, "getRevenueByMonth: err: ${res.message}")
                    onGetDone.postValue(true)
                }
            }
        }
    }

    fun getRevenueByYear(time: Long) {
        mCalender.timeInMillis = time
        timeReport.value = time.date2String("yyyy")
        viewModelScope.launch {
            when (val res = reportRepository.getReportByYear(mToken, mCalender[Calendar.YEAR])) {
                is Response.Success -> {
                    listRevenueByYear.postValue(res.data.listData)
                    totalRevenue.postValue(res.data.revenue.toMoney())
                    totalImport.postValue(res.data.totalImport.toMoney())
                    totalExport.postValue(res.data.totalExport.toMoney())
                    quantityImport.postValue(res.data.quantityImport.toString())
                    quantityExport.postValue(res.data.quantityExport.toString())
                    onGetDone.postValue(true)
                }
                is Response.Error -> {
                    onGetDone.postValue(true)
                    Log.e(TAG, "getRevenueByDate: err: ${res.message}")
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getRevenueByDate(time: Long) {
        val date = SimpleDateFormat("yyyy-MM-dd").format(time)
        mCalender.timeInMillis = time
        timeReport.value = time.date2String("dd/MM/yyyy")
        viewModelScope.launch {
            when (val res = reportRepository.getReportByDay(mToken, date)) {
                is Response.Success -> {
                    totalRevenue.postValue(res.data.revenue.toMoney())
                    totalImport.postValue(res.data.totalImport.toMoney())
                    totalExport.postValue(res.data.totalExport.toMoney())
                    quantityImport.postValue(res.data.quantityImport.toString())
                    quantityExport.postValue(res.data.quantityExport.toString())
                    onGetDone.postValue(true)
                }
                is Response.Error -> {
                    onGetDone.postValue(true)
                    Log.e(TAG, "getRevenueByDate: err: ${res.message}")
                }
            }
        }
    }
}
