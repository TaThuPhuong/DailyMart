package net.fpoly.dailymart.utils

import java.text.SimpleDateFormat
import java.util.*


fun fromMillisToStringDate(time: Long , divided: String = "/"): String {
    val dateFormat = SimpleDateFormat("dd${divided}MM${divided}yyyy", Locale.getDefault())
    return dateFormat.format(Date(time))
}

fun convertTotalInvoiceNumber(invoiceTotal: Double): String {
    return if(invoiceTotal >= 0){
        "+${String.format("%.3f", invoiceTotal)}".replace(",", ".")
    }else{
        "-${String.format("%.3f", invoiceTotal)}".replace(",", ".")
    }
}