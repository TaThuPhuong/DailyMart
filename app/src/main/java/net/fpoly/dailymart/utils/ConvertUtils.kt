package net.fpoly.dailymart.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun fromMillisToStringDate(time: Long, divided: String = "/"): String {
    val dateFormat = SimpleDateFormat("dd${divided}MM${divided}yyyy", Locale.getDefault())
    return dateFormat.format(Date(time))
}

fun fromMillisToStringDateTime(time: Long): String {
    val sdf = SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault())
    val date = Date(time)
    return sdf.format(date)
}

fun convertTotalInvoiceNumber(invoiceTotal: Long): String {
    return DecimalFormat("#,##0").format(invoiceTotal).replace(",", ".")
}
