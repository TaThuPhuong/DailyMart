package net.fpoly.dailymart.utils

import java.text.DecimalFormat

fun Double.toMoney(): String {
    val df = DecimalFormat("###,###,###.## ")
    return df.format(this) + " vnđ"
}