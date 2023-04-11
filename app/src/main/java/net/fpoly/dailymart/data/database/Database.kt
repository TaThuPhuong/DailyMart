package net.fpoly.dailymart.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.fpoly.dailymart.data.model.RecentNotification

@Database(entities = [RecentNotification::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract val notificationDao: NotificationDao

    companion object {
        const val DATABASE_NAME = "daily_mart_db"
    }
}