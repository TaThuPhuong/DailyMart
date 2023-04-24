package net.fpoly.dailymart.data.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.fpoly.dailymart.view.stock.StockCheck

data class ReportCheckStock(
    var time: Long = 0L,
    var data: String = "",
)

fun List<StockCheck>.dataToString(): String {
    return try {
        if (this.isEmpty()) ""
        else {
            Gson().toJson(this)
        }
    } catch (e: Exception) {
        ""
    }
}

fun String.stringToData(): List<StockCheck> {
    val type = object : TypeToken<List<StockCheck>>() {}.type
    return if (this.isEmpty()) ArrayList()
    else {
        Gson().fromJson(this, type)
    }
}