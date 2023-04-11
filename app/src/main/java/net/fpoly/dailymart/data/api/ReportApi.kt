package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.ReportResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ReportApi {
    @GET("stats/revenue/month/{month}&IMPORT")
    fun revenueMonth(
        @Header("Authorization") token: String,
        @Path("month") month: Int,
    ): Call<ReportResponse>

    @GET("stats/revenue/month/{month}&{type}}")
    fun revenueMonthWithType(
        @Header("Authorization") token: String,
        @Path("month") month: Int,
        @Path("type") type: String,
    ): Call<ReportResponse>

    @GET("stats/revenue/date/{date}&{type}")
    fun revenueDateWithType(
        @Header("Authorization") token: String,
        @Path("date") date: String,
        @Path("type") type: String,
    ): Call<ReportResponse>

    @GET("stats/revenue/year/{year}&{type}")
    fun revenueYearWithType(
        @Header("Authorization") token: String,
        @Path("year") year: String,
        @Path("type") type: String,
        ): Call<ReportResponse>

    @GET("stats/revenueByCustomDate/{startDate}&{endDate}&{type}")
    fun revenueCustomDateWithType(
        @Header("Authorization") token: String,
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String,
        @Path("type") type: String,
    )
}
