package net.fpoly.dailymart.data.model

data class ReportResponse(
    val status: String,
    val message: String,
    val data: ArrayList<ReportData>,
)

data class ReportData(
    val date: Long,
    val data: Long
)

