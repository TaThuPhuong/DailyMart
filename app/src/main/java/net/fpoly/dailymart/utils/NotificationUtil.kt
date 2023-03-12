package net.fpoly.dailymart.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import net.fpoly.dailymart.R
import net.fpoly.dailymart.utils.Constant.Companion.CHANNEL_ID
import net.fpoly.dailymart.utils.Constant.Companion.TASK_ID
import net.fpoly.dailymart.view.task.TaskActivity

@RequiresApi(Build.VERSION_CODES.M)
fun createNotification(context: Context, title: String, message: String, taskId: Long) {

    val intent = Intent(context, TaskActivity::class.java)
    intent.putExtra(TASK_ID, taskId)
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