package net.fpoly.dailymart.utils

import java.text.DecimalFormat

fun Int.toMoney(): String {
    val df = DecimalFormat("###,###,###.## ")
    return df.format(this) + " vnđ"
}

fun moneyFormatter(money: Int): String {
    val formatter = DecimalFormat("###,###,###,###,###")
    return "${formatter.format(money)} đ"
}
