package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName

data class ReportDataByMonth(
    @SerializedName("tongDoanhThu") val revenue: Long,
    @SerializedName("tienNhap") val totalImport: Long,
    @SerializedName("tienBan") val totalExport: Long,
    @SerializedName("soHoaDonNhap") val quantityImport: Long,
    @SerializedName("soHoaDonBan") val quantityExport: Long,
    @SerializedName("dataNgay") val totalByDay: List<ReportDataByDayInMonth>,
)

data class ReportDataByDayInMonth(
    @SerializedName("time") val date: Long,
    @SerializedName("data") val data: Long,
)

data class ReportDataByDay(
    @SerializedName("tongDoanhThu") val revenue: Long,
    @SerializedName("tienNhap") val totalImport: Long,
    @SerializedName("tienBan") val totalExport: Long,
    @SerializedName("soHoaDonNhap") val quantityImport: Long,
    @SerializedName("soHoDonBan") val quantityExport: Long,
)

data class ReportDataByYear(
    @SerializedName("tongDoanhThu") val revenue: Long,
    @SerializedName("tienNhap") val totalImport: Long,
    @SerializedName("tienBan") val totalExport: Long,
    @SerializedName("soHoaDonNhap") val quantityImport: Long,
    @SerializedName("soHoaDonBan") val quantityExport: Long,
    @SerializedName("dataThang") val totalByMonth: List<ReportDataByMonthInYear>,
)

data class ReportDataByMonthInYear(
    @SerializedName("month") val month: Long,
    @SerializedName("data") val data: Long,
)
