package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.ReportDataByDay
import net.fpoly.dailymart.data.model.ReportDataByMonth
import net.fpoly.dailymart.data.model.ReportDataByYear
import net.fpoly.dailymart.data.model.ResultData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface ReportApi {

    @GET("stats/revenue/date/{date}&{type}")
    suspend fun getRevenueDate(@Header("Authorization") token: String,@Path("date") date: String, @Path("type") type: String) : ResultData<ReportDataByDay>

    @GET("stats/revenue/month/{month}&{type}")
    suspend fun getRevenueMonth(@Header("Authorization") token: String,@Path("month") month: String, @Path("type") type: String) : ResultData<ReportDataByMonth>

    @GET("stats/revenue/year/{year}&{type}")
    suspend fun getRevenueYear(@Header("Authorization") token: String,@Path("year") year: String, @Path("type") type: String) : ResultData<ReportDataByYear>

}
