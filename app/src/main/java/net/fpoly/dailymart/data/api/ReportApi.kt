package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.*
import net.fpoly.dailymart.data.model.response.ResponseResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ReportApi {
    @GET("stats/revenue/month/{month}&{year}")
    suspend fun revenueMonth(
        @Header("Authorization") token: String,
        @Path("month") month: Int,
        @Path("year") year: Int,
    ): ResultData<ReportDataByMonth>

    @GET("stats/revenue/date/{date}")
    suspend fun revenueDate(
        @Header("Authorization") token: String,
        @Path("date") date: String,
    ): ResultData<ReportDataByDay>

    @GET("stats/revenue/year/{year}")
    suspend fun revenueYear(
        @Header("Authorization") token: String,
        @Path("year") year: Int,
    ): ResultData<ReportDataByYear>

    @GET("stats/bestsellerProduct")
    suspend fun getBestSeller(@Header("Authorization") token: String): ResultData<List<BestSeller>>
}
