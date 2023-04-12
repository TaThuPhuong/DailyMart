package net.fpoly.dailymart.data.model

data class NotificationData(
    var data: Data,
    var to: String,
)

data class Data(
    var title: String = "",
    var body: String = "",
)