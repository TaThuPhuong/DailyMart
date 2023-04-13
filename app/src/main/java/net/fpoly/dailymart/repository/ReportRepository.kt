package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.*


interface ReportRepository {

    suspend fun getReportByDate(token: String, date: String, type: ReportType): Response<ReportDataByDay>

    suspend fun getReportByMonth(token: String, month: String, type: ReportType): Response<ReportDataByMonth>

    suspend fun getReportByYear(token: String, year: String, type: ReportType): Response<ReportDataByYear>

}
