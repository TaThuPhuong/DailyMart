package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName


data class ReportDataByDay(
    val type: String,
    val date: String,
    val total: Long,
)

enum class ReportType {
    IMPORT, EXPORT
}

data class ReportDataByMonth(
    @SerializedName("totalMonth") val total: Long = 0,
    @SerializedName("totalByDay") val days: List<DayReport> = listOf()
)

data class ReportDataByYear(
    @SerializedName("type") val type: String = "",
    @SerializedName("totalAmountYear") val total: Long = 0,
    @SerializedName("dataByMonth") val months: List<MonthReport> = listOf()
)

data class MonthReport(
    val month: Long = 0,
    val data: Long = 0
)

data class DayReport(
    val date: Long = 0,
    val data: Long = 0
)
