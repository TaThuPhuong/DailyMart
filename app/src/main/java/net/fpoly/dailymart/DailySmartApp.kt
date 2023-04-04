package net.fpoly.dailymart

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import net.fpoly.dailymart.utils.Constant.Companion.CHANNEL_ID

class DailySmartApp : Application() {

    val context: Context
        get() = applicationContext

    val userRepository: UserRepository
        get() = ServiceLocator.providerUserRepository(this)

    val taskRepository: TaskRepository
        get() = ServiceLocator.providerTaskRepository(this)
    val supplierRepository: SupplierRepository
        get() = ServiceLocator.providerSupplierRepository(this)
    val invoiceRepository: InvoiceRepository
        get() = ServiceLocator.providerInvoiceRepository(this)

    val categoryRepository: CategoryRepository
        get() = ServiceLocator.providerCategoryRepository(this)

    val productPriceRepository: ProductPriceRepository
        get() = ServiceLocator.providerProductPriceRepository(this)

    val productRepository: ProductRepository
        get() = ServiceLocator.providerProductRepository(this)

    val expiryRepository: ExpiryRepository
        get() = ServiceLocator.providerExpiryRepository(this)

//    val invoiceDetailRepository: InvoiceDetailRepository
//        get() = ServiceLocator.providerExpiryRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "DailyMart"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
