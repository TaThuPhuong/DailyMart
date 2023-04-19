package net.fpoly.dailymart.data.model

import com.google.gson.annotations.SerializedName

data class ReportDataByDay(
    @SerializedName("tongDoanhThu") val revenue: Long,
    @SerializedName("tienNhap") val totalImport: Long,
    @SerializedName("tienBan") val totalExport: Long,
    @SerializedName("soHoaDonNhap") val quantityImport: Long,
    @SerializedName("soHoDonBan") val quantityExport: Long,
)

data class ReportDataByMonth(
    @SerializedName("tongDoanhThu") val revenue: Long,
    @SerializedName("tienNhap") val totalImport: Long,
    @SerializedName("tienBan") val totalExport: Long,
    @SerializedName("soHoaDonNhap") val quantityImport: Long,
    @SerializedName("soHoaDonBan") val quantityExport: Long,
    @SerializedName("dataNgay") val listData: List<ReportDayData>,
)

data class ReportDataByYear(
    @SerializedName("tongDoanhThu") val revenue: Long,
    @SerializedName("tienNhap") val totalImport: Long,
    @SerializedName("tienBan") val totalExport: Long,
    @SerializedName("soHoaDonNhap") val quantityImport: Long,
    @SerializedName("soHoaDonBan") val quantityExport: Long,
    @SerializedName("dataThang") val listData: List<ReportMonthData>,
)

data class ReportDayData(
    @SerializedName("time") val date: Long,
    @SerializedName("data") val data: DataDetail,
)

data class ReportMonthData(val month: Int, val data: DataDetail)

data class DataDetail(var tienNhap: Long = 0, var tienBan: Long = 0, var tongThuNhapNgay: Long = 0)
