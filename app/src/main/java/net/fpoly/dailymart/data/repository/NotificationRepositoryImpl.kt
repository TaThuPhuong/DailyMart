package net.fpoly.dailymart.data.repository

import net.fpoly.dailymart.data.database.NotificationDao
import net.fpoly.dailymart.data.model.RecentNotification
import net.fpoly.dailymart.repository.NotificationRepository

class NotificationRepositoryImpl(private val dao: NotificationDao) : NotificationRepository {
    override fun insertNotification(notification: RecentNotification) {
        dao.insertNotification(notification)
    }

    override fun getNotificationSeen(seen: Boolean): List<RecentNotification> {
        return dao.getNotificationSeen()
    }

    override fun deleteSeenNotification() {
        return dao.deleteSeenNotification()
    }
}