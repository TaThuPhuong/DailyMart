package net.fpoly.dailymart.extension.time_extention

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


fun Date.date2String(pattern: String = "dd/MM/yyyy"): String {
    return try {
        val format = SimpleDateFormat(pattern, Locale.ENGLISH)
        format.format(this.time)
    } catch (e: Exception) {
        ""
    }
}

fun Long.date2String(pattern: String = "dd/MM/yyyy"): String {
    return try {
        val format = SimpleDateFormat(pattern, Locale.ENGLISH)
        format.format(this)
    } catch (e: Exception) {
        ""
    }
}

fun Date.string2Date(sDate: String, pattern: String = "dd/MM/yyyy"): Date {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
        sdf.parse(sDate)
    } catch (e: Exception) {
        this
    }
}

fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

fun Long.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.timeInMillis = this
    return cal
}

fun Long.toDate(): Date {
    return Date(this)
}

fun Long.year(): Int {
    val cal = Calendar.getInstance()
    cal.timeInMillis = this
    return cal.get(Calendar.YEAR)
}

@SuppressLint("SimpleDateFormat")
fun today(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    return sdf.format(Calendar.getInstance().time)
}

@SuppressLint("SimpleDateFormat")
fun toDay(sDate: String): Date {
    return SimpleDateFormat("dd/MM/yyyy").parse(sDate) as Date
}