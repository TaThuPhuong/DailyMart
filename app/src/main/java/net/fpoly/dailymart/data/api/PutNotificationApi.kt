package net.fpoly.dailymart.data.api

import net.fpoly.dailymart.data.model.NotificationData
import net.fpoly.dailymart.utils.Constant.Companion.CONTENT_TYPE
import net.fpoly.dailymart.utils.Constant.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PutNotificationApi {
    @Headers("Authorization:key = $SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body
        data: NotificationData,
    ): Response<ResponseBody>
}