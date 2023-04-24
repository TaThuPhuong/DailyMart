package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.Customer
import net.fpoly.dailymart.data.model.CustomerParam
import net.fpoly.dailymart.data.model.ResultData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CustomerApi {

    @GET("customer/getAll")
    suspend fun getCustomers(@Header("Authorization") token: String): ResultData<ArrayList<Customer>>

    @POST("customer")
    suspend fun addCustomer(
        @Header("Authorization") token: String,
        @Body customer: CustomerParam
    ): ResultData<Unit>
}