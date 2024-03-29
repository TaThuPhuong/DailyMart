package net.fpoly.dailymart.utils

import android.annotation.SuppressLint
import android.app.Application
import net.fpoly.dailymart.data.model.Invoice
import net.fpoly.dailymart.data.model.ProductInvoiceParam
import net.fpoly.dailymart.data.model.param.InvoiceParam
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

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

fun convertTotalInvoice(invoice: ProductInvoiceParam): String {
    val value = DecimalFormat("#,##0").format(invoice.total.absoluteValue).replace(",", ".")
    return if (invoice.quantity <= 0) "-$value" else value
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
        SimpleDateFormat("HH:mm dd/MM/yyyy").format(timeInMillis)
    }
    return timePassedText
}

fun takeLastId(id: String): String {
    return id.takeLast(10)
}

fun toString(number: Long) = number.toString()

fun Float.round(pattern: String = "#"): String {
    val df = DecimalFormat(pattern)
    return df.format(this)
}
