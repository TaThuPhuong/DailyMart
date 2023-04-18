package net.fpoly.dailymart.utils

class Constant {

    companion object {
        const val BASE_URL = "https://fcm.googleapis.com/"
//        const val BASE_API = "https://serverdatn-production.up.railway.app/api/"
        const val BASE_API = "https://dailymart.sinhvien.app/api/"
        const val SERVER_KEY =
            "AAAA6O-N_sk:APA91bFoWt30PPdi-OAsIS8WzkmiqQY5p-1l2WOxIpxBK2ib2sYtQYWRh9eXRaZcS9wwtKWk5Wkp2xLEhFDMMiBN-9pUCs9bl2yvzGhMGtBeQATS5reNnNPF-QNdIqh77dWvhXmZ8ATJ"
        const val CONTENT_TYPE = "application/json"
        const val CHANNEL_ID = "Daily mart"
        const val TASK = "task"
        const val TASK_ID = "task_id"
        const val PRODUCT = "product"
        const val TIME = "time"
        const val WORK_SHEET = "work_sheet"
        const val IMAGE_DEFAULT =
            "https://firebasestorage.googleapis.com/v0/b/dailysmart-7f19c.appspot.com/o/products%2Fimg_default.png?alt=media&token=819a99fe-355c-4012-87f5-4507db7d7727"
        const val AVATAR_DEFAULT =
            "https://is1-ssl.mzstatic.com/image/thumb/Purple113/v4/8e/cf/03/8ecf03d8-9e2f-84f8-b13a-e39ba9f217bd/AppIcon-0-0-1x_U007emarketing-0-0-0-10-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/1200x630wa.png"
        const val SEVEN_DAY = 604800000L
        const val MESSAGE = "message"

    }
}

enum class ROLE(val value: String) {
    admin("Admin"), manager("Quản lý"), staff("Nhân viên")
}

enum class CheckDateFilter {
    SOON, SEVEN_DAY, CATEGORY
}