package net.fpoly.dailymart.utils

import java.text.DecimalFormat

fun Int.toMoney(): String {
    val df = DecimalFormat("###,###,###")
    return df.format(this) + " VNĐ"
}

fun Long.toMoney(): String {
    val df = DecimalFormat("###,###,###,###,###")
    return df.format(this) + " vnđ"
}


//fun Long.toMoney(): String {
//    val df = DecimalFormat("###,###.###")
//    return when (this) {
//        in 0L..999999L -> df.format(this) + " vnđ"
//        in 1000000L..999999999 -> df.format(this / 1000000f) + " triệu vnđ"
//        in 1000000000..999999999999 -> df.format(this / 1000000000f) + " tỷ vnđ"
//        else -> {
//            df.format(this) + " vnđ"
//        }
//    }
//}

fun moneyFormatter(money: Int): String {
    val formatter = DecimalFormat("###,###,###,###,###")
    return "${formatter.format(money)} đ"
}
