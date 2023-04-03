package net.fpoly.dailymart.service

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import net.fpoly.dailymart.utils.SharedPref
import net.fpoly.dailymart.utils.createNotification

open class FirebaseMessageService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val map: Map<String, String> = message.data
        val title = map["title"]
        val body = map["body"]
        val taskId = map["taskId"]?.toLong() ?: 0L
        if (title != null && body != null) {
            createNotification(applicationContext, title, body, taskId)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        SharedPref.setTokenNotification(applicationContext, token)
    }
}