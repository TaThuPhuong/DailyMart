package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.repository.ReportRepository

class ReportRepositoryImpl : ReportRepository {

    private val remoteData = ServerInstance.apiReport
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun getReportByDate(
        token: String,
        date: String,
        type: ReportType
    ) = withContext(ioDispatcher) {
        try {
            val res = remoteData.getRevenueDate(token, date, type.name)
            if (res.isSuccess()) {
                Response.Success(res.result)
            } else {
                Response.Error(res.message)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Response.Error(ex.message.toString())
        }
    }

    override suspend fun getReportByMonth(
        token: String,
        month: String,
        type: ReportType
    ) = withContext(ioDispatcher) {
        try {
            val res = remoteData.getRevenueMonth(token, month, type.name)
            if (res.isSuccess()) {
                Response.Success(res.result)
            } else {
                Response.Error(res.message)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Response.Error(ex.message.toString())
        }
    }

    override suspend fun getReportByYear(
        token: String,
        year: String,
        type: ReportType
    ) = withContext(ioDispatcher) {
        try {
            val res = remoteData.getRevenueYear(token, year, type.name)
            if (res.isSuccess()) {
                Response.Success(res.result)
            } else {
                Response.Error(res.message)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Response.Error(ex.message.toString())
        }
    }

    companion object {
        const val TAG = "ReportRepositoryImpl"
    }

}