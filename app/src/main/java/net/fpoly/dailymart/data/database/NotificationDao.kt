package net.fpoly.dailymart.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.fpoly.dailymart.data.model.RecentNotification

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notification: RecentNotification)

    @Query("select * from recent_notification where seen =:seen")
    fun getNotificationSeen(seen: Boolean = false): List<RecentNotification>

    @Query("delete from recent_notification")
    fun deleteSeenNotification()
}