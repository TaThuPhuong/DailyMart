package net.fpoly.dailymart

import android.app.Application
import android.content.Context
import net.fpoly.dailymart.data.AppModule
import net.fpoly.dailymart.repository.NotificationRepository

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
}
