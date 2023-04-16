package net.fpoly.dailymart.data.model

data class ReportResponse(
    val status: String,
    val message: String,
    val data: ReportData,
)

data class ReportData(
    val totalMonth: Long,
    val totalByDay: ArrayList<ReportPrice>
)

data class ReportPrice(
    val date: Long,
    val data: Long,
)

