package net.fpoly.dailymart.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BankApiInstance {
    companion object {
        private val BANK_URL = "https://api.vietqr.io/v2/"
        private val client = OkHttpClient().newBuilder().readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build()

        private val retrofit by lazy {
            Retrofit.Builder()
                .client(client)
                .baseUrl(BANK_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val apiBank : BankApi by lazy {
            retrofit.create(BankApi::class.java)
        }
    }
}