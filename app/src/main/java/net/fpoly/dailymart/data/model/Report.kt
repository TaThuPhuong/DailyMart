package net.fpoly.dailymart.data.model

data class ReportDataByMonth(
    val totalMonth: Long,
    val totalByDay: List<ReportDataByDayInMonth>,
)

data class ReportDataByDayInMonth(
    val date: Long,
    val data: Long,
)

data class ReportDataByDay(
    val type: String,
    val date: String,
    val total: Long,
)

data class ReportDataByYear(
    val type: String,
    val totalAmountYear: Long,
    val dataByMonth: List<ReportDataByMonthInYear>,
)

data class ReportDataByMonthInYear(
    val month: Long,
    val data: Long,
)

data class ReportDataByCustomDate(
    val time: Long,
    val data: Long,
)

enum class ReportType {
    IMPORT, EXPORT
}
