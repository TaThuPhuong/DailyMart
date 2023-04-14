package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.*


interface ReportRepository {

    suspend fun getReportByMonth(token: String, month: Int): Response<ReportDataByMonth>
    suspend fun getReportByDay(token: String, date: String): Response<ReportDataByDay>
    suspend fun getReportByYear(token: String, year: Int): Response<ReportDataByYear>
}
