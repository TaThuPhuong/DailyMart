package net.fpoly.dailymart

import android.app.Application
import android.content.Context
import net.fpoly.dailymart.data.AppModule

class DailySmartApp : Application() {

    val taskRepository = AppModule.providerTaskRepository()
    val userRepository = AppModule.providerUserRepository()
    val productRepository = AppModule.providerProductRepository()
    val categoryRepository = AppModule.providerCategoryRepository()
    val context: Context
        get() = applicationContext
}
