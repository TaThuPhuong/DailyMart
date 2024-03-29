package net.fpoly.dailymart.view.report

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.extension.time_extention.date2String
import net.fpoly.dailymart.firbase.database.LossesDao
import net.fpoly.dailymart.repository.ReportRepository
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.utils.toMoney
import java.text.SimpleDateFormat
import java.util.Calendar

class ReportViewModel(val app: Application, private val reportRepository: ReportRepository) :
    ViewModel() {

    private val TAG = "YinMing"
    val listRevenueByMonth = MutableLiveData<List<ReportDayData>>(ArrayList())
    val listRevenueByYear = MutableLiveData<List<ReportMonthData>>(ArrayList())
    val listBestSeller = MutableLiveData<List<BestSeller>>(ArrayList())
    val pieChartData = MutableLiveData<PieData>(null)

    val totalRevenue = MutableLiveData("0 vnđ")
    val totalImport = MutableLiveData("0 vnđ")
    val totalExport = MutableLiveData("0 vnđ")
    val quantityImport = MutableLiveData("0")
    val quantityExport = MutableLiveData("0")
    val totalLosses = MutableLiveData("0 vnđ")
    val reportDay = MutableLiveData(ReportDay())
    val onGetDone = MutableLiveData(false)


    private val mToken = SharedPref.getAccessToken(app)
    private val mCalender = Calendar.getInstance()

    val timeReport = MutableLiveData("")

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    init {
        timeReport.value = mCalender.timeInMillis.date2String()
        getRevenueByMonth(mCalender.timeInMillis)
        getListBestSeller()
    }

    private fun getListBestSeller() = viewModelScope.launch(Dispatchers.IO) {
        when (val res = reportRepository.getBestSeller(mToken)) {
            is Response.Error -> return@launch
            is Response.Success -> {
                listBestSeller.postValue(res.data.filter { it.soLuongDaBan > 0 }.take(10))
            }
        }
    }

    fun getRevenueByMonth(time: Long) {
        mCalender.timeInMillis = time
        timeReport.value = time.date2String("MM/yyyy")
        var losses = 0L
        viewModelScope.launch {
            LossesDao.getLossesByMonth(mCalender[Calendar.MONTH] + 1, mCalender[Calendar.YEAR]) {
                totalLosses.postValue(it.toMoney())
                losses = it
            }
            when (val res =
                reportRepository.getReportByMonth(
                    mToken,
                    mCalender[Calendar.MONTH] + 1,
                    mCalender[Calendar.YEAR]
                )) {
                is Response.Success -> {
                    listRevenueByMonth.postValue(res.data.listData)
                    totalRevenue.postValue((res.data.revenue - losses).toMoney())
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
        var losses = 0L
        viewModelScope.launch {
            LossesDao.getLossesByYear(mCalender[Calendar.YEAR]) {
                totalLosses.postValue(it.toMoney())
                losses = it
            }
            when (val res = reportRepository.getReportByYear(mToken, mCalender[Calendar.YEAR])) {
                is Response.Success -> {
                    listRevenueByYear.postValue(res.data.listData)
                    totalRevenue.postValue((res.data.revenue - losses).toMoney())
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
        mCalender.timeInMillis = time
        timeReport.value = time.date2String("dd/MM/yyyy")
        viewModelScope.launch {
            when (val res =
                reportRepository.getReportByDay(mToken, time.date2String("yyyy-MM-dd"))) {
                is Response.Success -> {
                    totalRevenue.postValue(res.data.revenue.toMoney())
                    totalImport.postValue(res.data.totalImport.toMoney())
                    totalExport.postValue(res.data.totalExport.toMoney())
                    quantityImport.postValue(res.data.quantityImport.toString())
                    quantityExport.postValue(res.data.quantityExport.toString())
                    reportDay.postValue(
                        ReportDay(
                            totalExport = res.data.totalExport,
                            totalImport = res.data.totalImport
                        )
                    )
                    onGetDone.postValue(true)
                    pieChartData.postValue(genPieData(res.data.totalImport, res.data.totalExport))
                }
                is Response.Error -> {
                    onGetDone.postValue(true)
                    Log.e(TAG, "getRevenueByDate: err: ${res.message}")
                }
            }
        }
    }

    private fun genPieData(import: Long, export: Long): PieData {
        val entries = listOf(
            PieEntry(import.toFloat(), "Nhập"),
            PieEntry(export.toFloat(), "Bán")
        )
        val dataSet = PieDataSet(entries, "")
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = ContextCompat.getColor(app, R.color.white)
        dataSet.colors = listOf(
            ContextCompat.getColor(app, R.color.pink_medium),
            ContextCompat.getColor(app, R.color.green_light)
        )
        return PieData(dataSet)
    }
}

data class ReportDay(
    var totalImport: Long = 0,
    var totalExport: Long = 0,
)
