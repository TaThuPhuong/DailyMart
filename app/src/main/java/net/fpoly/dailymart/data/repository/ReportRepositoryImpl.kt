package net.fpoly.dailymart.data.repository

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.data.model.response.ResponseResult
import net.fpoly.dailymart.repository.ReportRepository

class ReportRepositoryImpl : ReportRepository {

    private val reportApi = ServerInstance.apiReport
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val TAG = "ReportRepositoryImpl"
    override suspend fun getReportByMonth(
        token: String,
        month: Int,
//        type: ReportType
    ): Response<ReportDataByMonth> =
        withContext(ioDispatcher) {
            try {
                val res = reportApi.revenueMonthWithType(token, month)
                if (res.isSuccess()) {
                    Log.d(TAG, "getReportByMonth: res: ${res}")
                    Response.Success(res.result)
                } else {
                    Response.Error(res.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Response.Error(e.message.toString())
            }
        }

    override suspend fun getReportByDate(
        token: String,
        date: String,
        type: ReportType
    ): Response<ReportDataByDay> =
        withContext(ioDispatcher) {
            try {
                val res = reportApi.revenueDateWithType(token, date, type)
                if (res.isSuccess()) {
                    Response.Success(res.result)
                } else {
                    Response.Error(res.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Response.Error(e.message.toString())
            }
        }

    override suspend fun getReportByYear(
        token: String,
        year: String,
        type: ReportType
    ): Response<ReportDataByYear> =
        withContext(ioDispatcher) {
            try {
                val res = reportApi.revenueYearWithType(token, year, type)
                if (res.isSuccess()) {
                    Response.Success(res.result)
                } else {
                    Response.Error(res.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Response.Error(e.message.toString())
            }
        }

}