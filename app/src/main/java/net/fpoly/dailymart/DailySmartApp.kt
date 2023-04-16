package net.fpoly.dailymart

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import net.fpoly.dailymart.data.AppModule
import net.fpoly.dailymart.repository.NotificationRepository
import net.fpoly.dailymart.utils.Constant
import net.fpoly.dailymart.utils.Constant.Companion.CHANNEL_ID

class DailySmartApp : Application() {

    val taskRepository = AppModule.providerTaskRepository()
    val userRepository = AppModule.providerUserRepository()
    val productRepository = AppModule.providerProductRepository()
    val categoryRepository = AppModule.providerCategoryRepository()
    val supplierRepository = AppModule.providerSupplierRepository()
    val notificationRepo: NotificationRepository
        get() = AppModule.providerNotificationRepository(this)
    val context: Context
        get() = applicationContext

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Daily Mart"
            val descriptionText ="Thông báo"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
