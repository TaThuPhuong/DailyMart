package net.fpoly.dailymart.view.report

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.fpoly.dailymart.base.LoadingDialog
import net.fpoly.dailymart.data.model.ReportResponse
import net.fpoly.dailymart.repository.ReportRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportViewModel : ViewModel() {
    private val TAG = "ReportViewModel"
    private val reportRepository = ReportRepository()
    private val _listImport = MutableLiveData<ReportResponse?>()
    val listImport: LiveData<ReportResponse?> = _listImport
    private val _listExport = MutableLiveData<ReportResponse?>()
    val listExport: LiveData<ReportResponse?> = _listExport
    private val _listRevenue = MutableLiveData<ReportResponse?>()
    val listRevenue: LiveData<ReportResponse?> = _listRevenue
    private val _totalRevenue = MutableLiveData<ReportResponse?>()
    val totalRevenue: LiveData<ReportResponse?> = _totalRevenue
    private val _totalImport = MutableLiveData<ReportResponse?>()
    val totalImport: LiveData<ReportResponse?> = _totalImport
    private val _totalExport = MutableLiveData<ReportResponse?>()
    val totalExport: LiveData<ReportResponse?> = _totalExport
    private val _quantityImport = MutableLiveData<ReportResponse?>()
    val quantityImport: LiveData<ReportResponse?> = _quantityImport
    private val _quantityExport = MutableLiveData<ReportResponse?>()
    val quantityExport: LiveData<ReportResponse?> = _quantityExport
    private lateinit var mLoadingDialog: LoadingDialog

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context = context)
    }

    fun getImport(token: String, month: Int) {
        viewModelScope.launch {
//            mLoadingDialog.showLoading()
            try {
                reportRepository.getReportByMonth(token, month).enqueue(object :
                    Callback<ReportResponse> {
                    override fun onResponse(
                        call: Call<ReportResponse>,
                        response: Response<ReportResponse>,
                    ) {
//                        _listReport.value = response.body()
//                        mLoadingDialog.hideLoading()
                    }
                    override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
//                        _listReport.value = null
                        Log.e(TAG, "onFailure: data: $t")
//                        mLoadingDialog.hideLoading()
                    }
                })
            } catch (e: Exception) {
//                _listReport.value = null
                e.printStackTrace()
            }
        }
    }

    fun showDialogFilter(context: Context){
        FilterDialog(context, this).show()
    }
}
