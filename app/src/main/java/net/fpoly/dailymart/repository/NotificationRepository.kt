package net.fpoly.dailymart.repository

import net.fpoly.dailymart.data.model.RecentNotification

interface NotificationRepository {
    fun insertNotification(notification: RecentNotification)

    fun getNotificationSeen(seen: Boolean = false): List<RecentNotification>

    fun deleteSeenNotification()
}