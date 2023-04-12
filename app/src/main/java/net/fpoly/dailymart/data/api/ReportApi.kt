package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.data.model.response.ResponseResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ReportApi {
    @GET("stats/revenue/month/{month}&EXPORT")
    fun revenueMonthWithType(
        @Header("Authorization") token: String,
        @Path("month") month: Int,
//        @Path("type") type: ReportType,
    ): ResultData<ReportDataByMonth>

    @GET("stats/revenue/date/{date}&{type}")
    fun revenueDateWithType(
        @Header("Authorization") token: String,
        @Path("date") date: String,
        @Path("type") type: ReportType,
    ): ResultData<ReportDataByDay>

    @GET("stats/revenue/year/{year}&{type}")
    fun revenueYearWithType(
        @Header("Authorization") token: String,
        @Path("year") year: String,
        @Path("type") type: ReportType,
    ): ResultData<ReportDataByYear>

    @GET("stats/revenueByCustomDate/{startDate}&{endDate}&{type}")
    fun revenueCustomDateWithType(
        @Header("Authorization") token: String,
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String,
        @Path("type") type: ReportType,
    ): ResultData<ReportDataByCustomDate>
}
