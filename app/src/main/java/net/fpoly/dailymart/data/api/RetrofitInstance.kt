package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.utils.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object {
        private val client = OkHttpClient().newBuilder().readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).build()

        private val retrofit by lazy {
            Retrofit.Builder()
                .client(client)
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val apiPutNotification : PutNotificationApi by lazy {
            retrofit.create(PutNotificationApi::class.java)
        }
    }
}