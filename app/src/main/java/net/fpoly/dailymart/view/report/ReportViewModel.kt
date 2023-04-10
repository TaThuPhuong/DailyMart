package net.fpoly.dailymart.view.report

import android.content.Context
import android.util.Log
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
    private val _listReport = MutableLiveData<ReportResponse?>()
    val listReport: MutableLiveData<ReportResponse?> = _listReport
    private lateinit var mLoadingDialog: LoadingDialog

    fun initLoadDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context = context)
    }

    fun getReport(token: String, month: Int) {
        viewModelScope.launch {
            mLoadingDialog.showLoading()
            try {
                reportRepository.getReportByMonth(token, month).enqueue(object :
                    Callback<ReportResponse> {
                    override fun onResponse(
                        call: Call<ReportResponse>,
                        response: Response<ReportResponse>,
                    ) {
                        response.body()!!.data.forEach {
                            Log.d(TAG, "onResponse: data: ${it.data}")
                        }
                        _listReport.value = response.body()
                        mLoadingDialog.hideLoading()
                    }
                    override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                        _listReport.value = null
                        Log.e(TAG, "onFailure: data: $t")
                        mLoadingDialog.hideLoading()
                    }
                })
            } catch (e: Exception) {
                _listReport.value = null
                e.printStackTrace()
            }
        }
    }
}
