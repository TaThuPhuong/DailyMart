package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.*


interface ReportRepository {

    suspend fun getReportByDay(token: String, date: String): Response<ReportDataByDay>
    suspend fun getReportByMonth(token: String, month: Int, year: Int): Response<ReportDataByMonth>
    suspend fun getReportByYear(token: String, year: Int): Response<ReportDataByYear>
    suspend fun getBestSeller(token: String): Response<List<BestSeller>>
}
