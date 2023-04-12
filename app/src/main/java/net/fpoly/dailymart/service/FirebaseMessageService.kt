package net.fpoly.dailymart.service

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import net.fpoly.dailymart.data.AppModule
import net.fpoly.dailymart.data.model.RecentNotification
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.utils.createNotification

class FirebaseMessageService : FirebaseMessagingService() {

    private val notificationRepo = AppModule.providerNotificationRepository(this)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val map: Map<String, String> = message.data
        val title = map["title"] ?: ""
        val body = map["body"] ?: ""
        val value = map["value"] ?: ""
        createNotification(applicationContext, title, body, value)
        notificationRepo.insertNotification(
            RecentNotification(
                time = System.currentTimeMillis(),
                title = title,
                value = value,
                message = body,
            )
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        SharedPref.setTokenNotification(applicationContext, token)
    }
}