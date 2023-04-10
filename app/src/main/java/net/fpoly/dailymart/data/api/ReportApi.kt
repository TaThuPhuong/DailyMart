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
}