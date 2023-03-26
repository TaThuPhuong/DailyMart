package net.fpoly.dailymart.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.data.model.User

@Database(
    entities = [User::class, Task::class, Invoice::class],
    version = 1,
    exportSchema = false
)
abstract class DailyMartDataBase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val taskDao: TaskDao
    abstract val invoiceDao: InvoiceDao

    companion object {
        const val DATABASE_NAME = "daily_mart_db"
    }
}