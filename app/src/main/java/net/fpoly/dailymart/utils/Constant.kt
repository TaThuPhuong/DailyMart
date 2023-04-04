package net.fpoly.dailymart.utils

class Constant {

    companion object {
        const val BASE_URL = "https://fcm.googleapis.com"
        const val BASE_API = "https://serverdatn.onrender.com/api/"
        const val SERVER_KEY =
            "AAAA6O-N_sk:APA91bFoWt30PPdi-OAsIS8WzkmiqQY5p-1l2WOxIpxBK2ib2sYtQYWRh9eXRaZcS9wwtKWk5Wkp2xLEhFDMMiBN-9pUCs9bl2yvzGhMGtBeQATS5reNnNPF-QNdIqh77dWvhXmZ8ATJ"
        const val CONTENT_TYPE = "application/json"
        const val CHANNEL_ID = "Daily mart"
        const val TASK_ID = "task_id"
    }
}

enum class ROLE(val value: String) {
    admin("Admin"), manager("Quản lý"), staff("Nhân viên")
}

enum class CheckDateFilter {
    SOON, SEVEN_DAY, CATEGORY
}