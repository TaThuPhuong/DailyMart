package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.*


interface ReportRepository {

    suspend fun getReportByMonth(token: String, month: Int): Response<ReportDataByMonth>
    suspend fun getReportByDate(token: String, date: String, type: ReportType): Response<ReportDataByDay>

    suspend fun getReportByYear(token: String, year: String, type: ReportType): Response<ReportDataByYear>
}
