package net.fpoly.dailymart.utils

import android.annotation.SuppressLint
import android.app.Application
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

@SuppressLint("SimpleDateFormat")
fun convertTimeInMillisToLastTimeString(timeInMillis: Long): String {
    val timePassed = (System.currentTimeMillis() - timeInMillis) / 1000L
    val timePassedText: String = if (timePassed < 60L) {
        "Bây giờ"
    } else if (timePassed < 3600L) {
        "${(timePassed.toInt() / 60L)} phút trước"
    } else if (timePassed < 3600L * 24L) {
        "${(timePassed.toInt() / 3600L)} giờ trước"
    } else {
        SimpleDateFormat("dd/MM/yyyy").format(timeInMillis)
    }
    return timePassedText
}