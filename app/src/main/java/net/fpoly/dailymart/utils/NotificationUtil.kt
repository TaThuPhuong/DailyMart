package net.fpoly.dailymart.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fpoly.dailymart.R
import net.fpoly.dailymart.data.api.RetrofitInstance
import net.fpoly.dailymart.data.model.Data
import net.fpoly.dailymart.data.model.NotificationData
import net.fpoly.dailymart.utils.Constant.Companion.CHANNEL_ID
import net.fpoly.dailymart.view.task.TaskActivity
import net.fpoly.dailymart.view.task.task_detail.TaskDetailActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext

@RequiresApi(Build.VERSION_CODES.M)
fun createNotification(context: Context, title: String, message: String, value: String?) {

    val intent = Intent(context, TaskDetailActivity::class.java)
    value?.let {
        intent.putExtra(Constant.TASK_ID, it)
    }
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(message)
        .setContentIntent(pendingIntent)
        .setWhen(System.currentTimeMillis())
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setSmallIcon(R.drawable.ic_launcher)
        .build()
    SharedPref.setNotificationId(context)
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(SharedPref.getNotificationId(context), notification)
}

suspend fun sendNotification(
    title: String,
    message: String,
    value: String,
    user: String,
    to: String,
) =
    try {
        val data = NotificationData(Data(title, message, user, value), to)
        val notificationApi = RetrofitInstance.apiPutNotification
        Log.e("YingMing", "data: $data")
        Log.e("YingMing", "notificationApi: $notificationApi")
        notificationApi.postNotification(data).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {
                Log.e("YingMing", "sendNotification body: ${response.body()?.string()}")
                Log.e("YingMing", "sendNotification error: ${response.errorBody()?.string()}")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("YingMing", "sendNotification onFailure: $t")
            }
        })
    } catch (e: Exception) {
        Log.e("YingMing", "sendNotification: $e")
    }
