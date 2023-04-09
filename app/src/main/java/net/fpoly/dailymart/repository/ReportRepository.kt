package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.api.ServerInstance
import net.fpoly.dailymart.data.model.ReportResponse
import retrofit2.Call

class ReportRepository {
    private val reportService = ServerInstance.apiReport

    suspend fun getReportByMonth(token: String, month: Int): Call<ReportResponse> {
        return reportService.revenueMonth(token, month)
    }
}
