package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.BankAccountCheck
import net.fpoly.dailymart.data.model.BankAccountRequest
import net.fpoly.dailymart.data.model.root.BankRoot
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface BankApi {
    @GET("banks")
    fun getListBank(): Call<BankRoot>

    @Headers(
        "Content-Type:application/json",
        "x-client-id:eb19ea19-a7b3-4ca0-8073-4a1ba3b53935",
        "x-api-key:99715fad-7b5b-4845-8dd6-9b08e712e6ba"
    )
    @POST("lookup")
    fun checkAccount(@Body accountCheck: BankAccountCheck): Call<BankAccountRequest>
}