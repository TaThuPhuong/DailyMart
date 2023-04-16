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

    override suspend fun getReportByMonth(token: String, month: Int) =
        withContext(ioDispatcher) {
            try {
                val res = reportApi.revenueMonth(token, month)
                if (res.isSuccess()) {
                    Response.Success(res.result, res.message)
                } else {
                    Response.Error(res.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Response.Error(e.message.toString())
            }
        }

    override suspend fun getReportByDay(token: String, date: String) =
        withContext(ioDispatcher) {
            try {
                val res = reportApi.revenueDate(token, date)
                if (res.isSuccess()) {
                    Response.Success(res.result, res.message)
                } else {
                    Response.Error(res.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Response.Error(e.message.toString())
            }
        }

    override suspend fun getReportByYear(token: String, year: Int) =
        withContext(ioDispatcher) {
            try {
                val res = reportApi.revenueYear(token, year)
                if (res.isSuccess()) {
                    Response.Success(res.result, res.message)
                } else {
                    Response.Error(res.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Response.Error(e.message.toString())
            }
        }
}